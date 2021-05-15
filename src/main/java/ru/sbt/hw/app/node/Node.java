package ru.sbt.hw.app.node;

import java.util.LinkedList;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


public class Node implements Runnable {
    private final int nodeId;
    private final int allDataAmount;
    private final LinkedList<DataPackage> initialData;
    private final LinkedList<DataPackage> dataQueue;
    private int finishedDataAmount;
    private long processingTime;
    private Node nextNode;
    public Lock lock;
    public Condition condition;

    public Node (int nodeId, int size, int pathLength) {
        this.nodeId = nodeId;
        this.allDataAmount = size * (pathLength);
        this.initialData = new LinkedList<>();
        this.dataQueue = new LinkedList<>();
        this.finishedDataAmount = 0;
        this.lock = new ReentrantLock();
        this.condition = lock.newCondition();
    }

    public void run() {
        long startTime = System.nanoTime();
        lock.lock();
        try {
            while (!initialData.isEmpty() || finishedDataAmount < allDataAmount) {
                while (initialData.isEmpty() && dataQueue.isEmpty()) {
                    condition.await();
                }
                packageProcessing();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
        processingTime += System.nanoTime() - startTime;
    }

    private void packageProcessing() {
        if (!dataQueue.isEmpty()) {
            DataPackage dataPackage = dataQueue.poll();
            finishedDataAmount++;
            if (dataPackage.getId() == nodeId) {
                dataPackage.setFinishTime();
                return;
            }
            if (!dataPackage.getWasSent()) {
                dataPackage.setStartTime();
            }
            addDataToNextNodeQueue(dataPackage);
            return;
        }
        DataPackage dataPackage = initialData.poll();
        addDataToNextNodeQueue(dataPackage);
    }

    private void addDataToNextNodeQueue(DataPackage dataPackage) {
        lock.unlock();
        nextNode.lock.lock();
        nextNode.dataQueue.add(dataPackage);
        nextNode.condition.signalAll();
        nextNode.lock.unlock();
        lock.lock();
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
