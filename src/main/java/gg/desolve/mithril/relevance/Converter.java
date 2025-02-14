package gg.desolve.mithril.relevance;

import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    public static String time(long millis) {
        millis += 1L;

        long seconds = millis / 1000L;
        long minutes = seconds / 60L;
        long hours = minutes / 60L;
        long days = hours / 24L;
        long weeks = days / 7L;
        long months = weeks / 4L;
        long years = months / 12L;

        if (years > 0) {
            return years + " year" + (years == 1 ? "" : "s") +
                    (months % 12 > 0 ? ", " + (months % 12) + " month" + (months % 12 == 1 ? "" : "s") : "");
        } else if (months > 0) {
            return months + " month" + (months == 1 ? "" : "s") +
                    (weeks % 4 > 0 ? ", " + (weeks % 4) + " week" + (weeks % 4 == 1 ? "" : "s") : "");
        } else if (weeks > 0) {
            return weeks + " week" + (weeks == 1 ? "" : "s") +
                    (days % 7 > 0 ? ", " + (days % 7) + " day" + (days % 7 == 1 ? "" : "s") : "");
        } else if (days > 0) {
            return days + " day" + (days == 1 ? "" : "s") +
                    (hours % 24 > 0 ? ", " + (hours % 24) + " hour" + (hours % 24 == 1 ? "" : "s") : "");
        } else if (hours > 0) {
            return hours + " hour" + (hours == 1 ? "" : "s") +
                    (minutes % 60 > 0 ? ", " + (minutes % 60) + " minute" + (minutes % 60 == 1 ? "" : "s") : "");
        } else if (minutes > 0) {
            return minutes + " minute" + (minutes == 1 ? "" : "s");
        } else {
            return seconds + " second" + (seconds == 1 ? "" : "s");
        }
    }

    public static long duration(String duration) {
        if (duration.equalsIgnoreCase("perm") || duration.equalsIgnoreCase("permanent")) {
            return Long.MAX_VALUE;
        }

        long time = 0L;
        Matcher matcher = Pattern.compile("(\\d+)([smhdwMy])").matcher(duration);

        while (matcher.find()) {
            long value = Long.parseLong(matcher.group(1));
            String type = matcher.group(2);

            switch (type) {
                case "s":
                    time += value;
                    break;
                case "m":
                    time += value * 60;
                    break;
                case "h":
                    time += value * 60 * 60;
                    break;
                case "d":
                    time += value * 60 * 60 * 24;
                    break;
                case "w":
                    time += value * 60 * 60 * 24 * 7;
                    break;
                case "M":
                    time += value * 60 * 60 * 24 * 30;
                    break;
                case "y":
                    time += value * 60 * 60 * 24 * 365;
                    break;
            }
        }

        return time == 0 ? -1 : time * 1000;
    }

}
