package gg.desolve.bubble.toolkit;

public class Logger {

    public void info(String message) {
        System.out.println("[Bubble] " + message);
    }

    public void warning(String message) {
        System.out.println("[Bubble] " + message);
    }

    public void error(String message) {
        System.err.println("[Bubble] " + message);
    }
}
