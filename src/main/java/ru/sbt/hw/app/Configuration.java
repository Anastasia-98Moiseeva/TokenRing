package ru.sbt.hw.app;

class Configuration {
    private int threadAmount;
    private int dataAmount;

    Configuration(int threadAmount, int dataAmount) {
        this.threadAmount = threadAmount;
        this.dataAmount = dataAmount;
    }

    public int getDataAmount() {
        return dataAmount;
    }

    public int getThreadAmount() {
        return threadAmount;
    }
}

