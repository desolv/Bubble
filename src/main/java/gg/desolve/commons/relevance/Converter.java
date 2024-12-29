package gg.desolve.commons.relevance;

import java.util.UUID;

public class Converter {

    public static String generateId() {
        String randomId = UUID.randomUUID().toString()
                .replaceAll("-", "")
                .toLowerCase();
        return randomId.substring(0, Math.min(12, randomId.length()));
    }

    public static long seconds(long millis) {
        return millis / 1000;
    }

}
