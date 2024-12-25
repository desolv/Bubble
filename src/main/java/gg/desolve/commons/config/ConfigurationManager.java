package gg.desolve.commons.config;

import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class ConfigurationManager {

    private final Map<JavaPlugin, Map<String, Config>> configurations = new HashMap<>();

    public void initialize(JavaPlugin plugin, String... names) {
        configurations.putIfAbsent(plugin, new HashMap<>());

        Arrays.stream(names).sequential().forEach(file -> {
            if (!configurations.get(plugin).containsKey(file))
                configurations.get(plugin).put(file, new Config(plugin, file));
        });
    }

    public Config getConfig(JavaPlugin plugin, String name) {
        return (configurations.get(plugin) != null) ?
                configurations.get(plugin).get(name) :
                null;
    }

    public void reload(JavaPlugin plugin, String name) {
        Config config = getConfig(plugin, name);
        if (config != null)
            config.reload();
    }

    public void save(JavaPlugin plugin, String name) {
        Config config = getConfig(plugin, name);
        if (config != null)
            config.save();
    }
}
