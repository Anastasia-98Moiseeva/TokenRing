package ru.sbt.hw.app.node;

public class DataPackage {
    private final String data;
    private final long dataId;
    private long startTime;
    private long finishTime;
    private boolean wasSent;

    public DataPackage(String data, long dataId) {
        this.data = data;
        this.dataId = dataId;
    }

    public long getId() {
        return dataId;
    }

    public long getStartTime() {
        return startTime;
    }

    public long getFinishTime() {
        return finishTime;
    }

    public boolean getWasSent() {
        return wasSent;
    }

    public void setFinishTime() {
        this.finishTime = System.nanoTime();
    }

    public void setStartTime() {
        this.startTime = System.nanoTime();
        this.wasSent = true;
    }
}
