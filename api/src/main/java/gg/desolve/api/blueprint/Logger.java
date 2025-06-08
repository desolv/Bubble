package gg.desolve.api.blueprint;

import lombok.Data;

@Data
public class Logger {

    public static String name;

    public static void info(String message) {
        System.out.println("[" + name + "] " + message);
    }

    public static void warning(String message) {
        System.out.println("[" + name + "] " + message);
    }

    public static void error(String message) {
        System.err.println("[" + name + "] " + message);
        throw new IllegalStateException(message);
    }
}
