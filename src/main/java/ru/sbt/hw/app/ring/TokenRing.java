package ru.sbt.hw.app.ring;

import ru.sbt.hw.app.node.DataPackage;
import ru.sbt.hw.app.node.Node;
import ru.sbt.hw.info.ExecutionInfo;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

public class TokenRing {
    private final List<Node> nodeList;
    private final List<DataPackage> dataList;
    private final ExecutionInfo executionInfo;

    public TokenRing(int nodesAmount, int dataAmount) {
        nodeList = new ArrayList<>();
        dataList = new ArrayList<>();
        if (dataAmount % nodesAmount != 0) {
            dataAmount += nodesAmount - (dataAmount % nodesAmount);
        }
        setNodeList(nodesAmount, dataAmount);
        setDataList(dataAmount, nodesAmount - 1);
        executionInfo = new ExecutionInfo(nodesAmount, dataAmount);
    }

    private void setNodeList(int nodesAmount, int dataAmount) {
        nodeList.add(new Node(0, dataAmount / nodesAmount, nodesAmount - 1));
        for (int i = 1; i < nodesAmount; i++) {
            Node node = new Node(i, dataAmount / nodesAmount, nodesAmount - 1);
            nodeList.get(i - 1).setNextNode(node);
            nodeList.add(node);
        }
        nodeList.get(nodeList.size() - 1).setNextNode(nodeList.get(0));
    }

    private void setDataList(int dataAmount, int length) {
        for (int i = 0; i < dataAmount; i++) {
            DataPackage dp = new DataPackage("id = " + (i % nodeList.size()), (length + i) % nodeList.size());
            dataList.add(dp);
            nodeList.get(i % nodeList.size()).addInitialData(dp);
        }
    }

    private void prepareInfo() {
        long time = 0;
        for (DataPackage dataPackage : dataList) {
            time += dataPackage.getFinishTime() - dataPackage.getStartTime();
        }
        executionInfo.setLatency((double)time / (1000000 * dataList.size()));
        time = 0;
        for (Node node : nodeList) {
            time += node.getProcessingTime();
        }
        executionInfo.setProcessingTime((double)time / (1000000 * nodeList.size()));
    }

    private List<Future<?>> prepareFutureTask(ExecutorService executorService) {
        List<Future<?>> taskList = new ArrayList<>();
        for (Node node: nodeList) {
            taskList.add(executorService.submit(node));
        }
        return taskList;
    }

    public void runTokenRing(ExecutorService executorService) {
        List<Future<?>> taskList = prepareFutureTask(executorService);
        for (Future<?> task : taskList) {
            try {
                task.get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
        prepareInfo();
    }

    public void writeInfoInFile(String filePath) {
        executionInfo.saveInfoToFile(new File(filePath));
    }
}
