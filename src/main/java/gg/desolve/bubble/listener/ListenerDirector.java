package gg.desolve.bubble.listener;

import gg.desolve.bubble.Bubble;
import lombok.Getter;
import org.bukkit.event.Listener;

import java.util.Arrays;
import java.util.List;

@Getter
public class ListenerDirector {

    private final List<Listener> listeners;

    public ListenerDirector() {
        listeners = listeners();
    }

    private List<Listener> listeners() {
        List<Listener> listenerList = Arrays.asList(
                new PlayerListener()
        );

        listenerList.forEach(listener -> Bubble.getInstance().getServer().getPluginManager().registerEvents(listener, Bubble.getInstance()));
        return listenerList;
    }

}

