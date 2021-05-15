package ru.sbt.hw;


import ru.sbt.hw.app.TokenRingApplication;

public class Main {

    public static void main(String[] args) {
        TokenRingApplication app = new TokenRingApplication(5, 30, 5, 10000);
        app.runApp(10, "/home/anastasia/Documents/mipt/concurency/TokenRing/datarr.txt");
    }
}
