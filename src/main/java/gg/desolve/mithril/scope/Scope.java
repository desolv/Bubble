package gg.desolve.mithril.scope;

import lombok.Data;

import java.util.List;

@Data
public class Scope {

    private final String name;
    private List<String> instances;

    public Scope(String name, List<String> instances) {
        this.name = name;
        this.instances = instances;
    }
}
