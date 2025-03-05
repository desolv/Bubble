package gg.desolve.mithril.relevance;

public record Duration(long duration) {

    public static Duration duration(String duration) {
        return new Duration(Converter.duration(duration));
    }

}
