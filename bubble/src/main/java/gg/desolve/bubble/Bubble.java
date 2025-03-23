package gg.desolve.bubble;

import gg.desolve.bubble.toolkit.Logger;
import lombok.Getter;
import lombok.Setter;

public class Bubble {

    @Getter
    @Setter
    public static Logger logger;

    public Bubble() {
        logger = new Logger();
    }
}
