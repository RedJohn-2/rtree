package main;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        MainWindow mw = new MainWindow();
        mw.setSize(800, 600);
        mw.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        mw.setVisible(true);
    }
}
