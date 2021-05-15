package ru.sbt.hw;


import ru.sbt.hw.app.TokenRingApplication;

public class Main {

    public static void main(String[] args) {
        TokenRingApplication app = new TokenRingApplication(5, 10, 5, 10, 20, 5);
        app.runApp(10, "/home/anastasia/Documents/mipt/concurency/TokenRing/d4.txt");
    }
}
