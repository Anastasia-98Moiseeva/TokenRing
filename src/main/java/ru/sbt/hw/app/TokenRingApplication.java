package ru.sbt.hw.app;

import ru.sbt.hw.app.ring.TokenRing;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TokenRingApplication {

    private List<Configuration> configList;

    public TokenRingApplication(int minThreadNum, int maxThreadNum, int step, int dataAmount) {
        generateConfigList(minThreadNum, maxThreadNum, step, dataAmount);
    }

    private void generateConfigList(int minThreadNum, int maxThreadNum, int step, int dataAmount) {
        this.configList = new ArrayList<>();
        for (int i = minThreadNum; i <= maxThreadNum; i+=step) {
            configList.add(new Configuration(i, dataAmount));
        }
    }

    private void runWarming(int num, Configuration config, ExecutorService executorService) {
        for (int i = 0; i < num; i++) {
            TokenRing tokenRing = new TokenRing(config.getThreadAmount(), config.getDataAmount());
            tokenRing.runTokenRing(executorService);
            System.gc();
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
                System.gc();
            }
            executorService.shutdown();
        }
    }
}
