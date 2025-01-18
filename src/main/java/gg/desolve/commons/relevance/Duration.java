package gg.desolve.commons.relevance;

import lombok.Getter;

@Getter
public record Duration(long duration) {

    public static Duration duration(String duration) {
        long time = Converter.duration(duration);
        return time == 0 ? null : new Duration(time);
    }

}
