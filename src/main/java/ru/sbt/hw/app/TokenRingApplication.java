package ru.sbt.hw.app;

import ru.sbt.hw.app.ring.TokenRing;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TokenRingApplication {

    private List<Configuration> configList;

    public TokenRingApplication(int minThreadNum, int maxThreadNum, int threadNumStep, int minDataNum, int maxDataNum, int dataNumStep) {
        generateConfigList(minThreadNum, maxThreadNum, threadNumStep, minDataNum, maxDataNum, dataNumStep);
    }

    private void generateConfigList(int minThreadNum, int maxThreadNum, int threadNumStep, int minDataNum, int maxDataNum, int dataNumStep) {
        this.configList = new ArrayList<>();
        for (int i = minThreadNum; i <= maxThreadNum; i += threadNumStep) {
            for (int j = minDataNum; j <= maxDataNum; j += dataNumStep) {
                if (j % i != 0) {
                    j += i - (j % i);
                }
                configList.add(new Configuration(i, j));
            }
        }
    }

    private void runWarming(int num, Configuration config, ExecutorService executorService) {
        for (int i = 0; i < num; i++) {
            TokenRing tokenRing = new TokenRing(config.getThreadAmount(), config.getDataAmount());
            tokenRing.runTokenRing(executorService);
        }
    }

    public void runApp(int num, String filePath) {
        for (Configuration config : configList) {
            ExecutorService executorService = Executors.newFixedThreadPool(config.getThreadAmount());
            runWarming(num, config, executorService);
            for (int i = 0; i < num; i++) {
                TokenRing tokenRing = new TokenRing(config.getThreadAmount(), config.getDataAmount());
                tokenRing.runTokenRing(executorService);
                tokenRing.writeInfoInFile(filePath);
            }
            executorService.shutdown();
        }
    }
}
