package gg.desolve.commons.instance;

import lombok.Data;

@Data
public class Instance {

    private final String id;
    private final String name;
    private final String version;
    private long online;
    private final long booting;
    private long heartbeat;

    public Instance(String id, String name, String version, long online, long booting, long heartbeat) {
        this.id = id;
        this.name = name;
        this.version = version;
        this.online = online;
        this.booting = booting;
        this.heartbeat = heartbeat;
    }
}
