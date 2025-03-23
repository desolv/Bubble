package gg.desolve.bubble;

import lombok.Getter;

@Getter
public class Bubble {

    private static Bubble instance;

    public Bubble() {
        instance = this;
    }

}
