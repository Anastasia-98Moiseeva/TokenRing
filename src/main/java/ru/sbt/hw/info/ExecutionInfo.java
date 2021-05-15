package ru.sbt.hw.info;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class ExecutionInfo {
    private final int nodesAmount;
    private final int dataAmount;
    private double time;
    private double throughput;
    private double latency;

    public ExecutionInfo(int nodesAmount, int dataAmount) {
        this.nodesAmount = nodesAmount;
        this.dataAmount = dataAmount;
    }

    public void saveInfoToFile(File file) {
        throughput = dataAmount / time;
        //System.out.println("throughput: " + throughput);
        String text = nodesAmount + " " + dataAmount + " " + time + " " + throughput + " " + latency + "\n";
        try {
            FileWriter fileWriter = new FileWriter(file,true);
            fileWriter.write(text);
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setProcessingTime(double time) {
        //System.out.println("time: " + time);
        this.time = time;
    }

    public void setLatency(double latency) {
        //System.out.println("latency: " + latency);
        this.latency = latency;
    }
}
