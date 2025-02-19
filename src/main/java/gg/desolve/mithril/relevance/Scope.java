package gg.desolve.mithril.relevance;

import lombok.Data;

import java.util.Arrays;
import java.util.List;

@Data
public class Scope {

    private final String unformatted;
    private List<String> scopes;
    private String format;

    public Scope(String scope) {
        this.unformatted = scope;
        scopes = Arrays.asList(unformatted.split(","));
        format = String.join(", ", unformatted);
    }
}
