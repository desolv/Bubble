package gg.desolve.commons.relevance;

import lombok.Getter;

@Getter
public class Duration {

    private final long duration;

    public Duration(long duration) {
        this.duration = duration;
    }

    public static Duration duration(String duration) {
        long time = Converter.duration(duration);
        return time == 0 ? null : new Duration(time);
    }

}
