package ru.sbt.hw.app.node;

import java.util.LinkedList;
import java.util.concurrent.ConcurrentLinkedQueue;


public class Node implements Runnable {
    private final int nodeId;
    private final int allDataAmount;
    private final LinkedList<DataPackage> initialData;
    private final ConcurrentLinkedQueue<DataPackage> dataQueue;
    private int finishedDataAmount;
    private long processingTime;
    private Node nextNode;

    public Node (int nodeId, int size, int pathLength) {
        this.nodeId = nodeId;
        this.allDataAmount = size * (pathLength);
        this.initialData = new LinkedList<>();
        this.dataQueue = new ConcurrentLinkedQueue<>();
        this.finishedDataAmount = 0;
    }

    public void run() {
        long startTime = System.nanoTime();
        while (!initialData.isEmpty() || finishedDataAmount < allDataAmount) {
            packageProcessing();
        }
        processingTime += System.nanoTime() - startTime;
    }

    private void packageProcessing() {
        DataPackage dataPackage = dataQueue.poll();
        if (dataPackage != null) {
            finishedDataAmount++;
            if (dataPackage.getId() == this.nodeId) {
                dataPackage.setFinishTime();
                return;
            }
            if (!dataPackage.getWasSent()) {
                dataPackage.setStartTime();
            }
            nextNode.addDataToQueue(dataPackage);
            return;
        }
        if (!initialData.isEmpty()) {
            dataPackage = initialData.poll();
            nextNode.addDataToQueue(dataPackage);
        }
    }

    private void addDataToQueue(DataPackage dataPackage) {
        dataQueue.add(dataPackage);
    }

    public void addInitialData(DataPackage dataPackage) {
        initialData.add(dataPackage);
    }

    public long getProcessingTime() {
        return processingTime;
    }

    public void setNextNode(Node node) {
        nextNode = node;
    }

}
