package gg.desolve.mithril.config;

import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class ConfigurationManager {

    private final Map<String, Config> configs = new HashMap<>();

    public ConfigurationManager(JavaPlugin plugin, String... names) {
        Arrays.stream(names).sequential().forEach(file -> {
            if (!configs.containsKey(file)) {
                configs.put(file, new Config(plugin, file));
            }
        });
    }

    public Config getConfig(String name) {
        return configs.get(name);
    }

    public void reload(String name) {
        Config config = getConfig(name);
        if (config != null) {
            config.reload();
        }
    }

    public void save(String name) {
        Config config = getConfig(name);
        if (config != null) {
            config.save();
        }
    }
}
