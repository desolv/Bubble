package gg.desolve.bubble.relevance;

public record Duration(long duration) {

    public static Duration duration(String duration) {
        return new Duration(Converter.duration(duration));
    }

}
