package gg.desolve.api.blueprint;

import lombok.Getter;

@Getter
public class Logger {

    public static void info(String message) {
        System.out.println("[Bubble] " + message);
    }

    public static void warning(String message) {
        System.out.println("[Bubble] " + message);
    }

    public static void error(String message) {
        System.err.println("[Bubble] " + message);
    }
}
