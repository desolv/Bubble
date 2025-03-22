package gg.desolve.bubble.config;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Configuration {

    private final JavaPlugin plugin;
    private final Map<String, FileConfiguration> configs = new HashMap<>();

    public Configuration(JavaPlugin plugin, String... files) {
        this.plugin = plugin;

        Arrays.stream(files).toList().forEach(this::load);
    }

    private void load(String fileName) {
        File file = new File(plugin.getDataFolder(), fileName);

        if (!file.exists()) {
            file.getParentFile().mkdirs();
            plugin.saveResource(fileName, false);
        }

        configs.put(fileName, YamlConfiguration.loadConfiguration(file));
    }

    public FileConfiguration getConfig(String fileName) {
        return configs.get(fileName);
    }

    public void save(String fileName) {
        File file = new File(plugin.getDataFolder(), fileName);
        FileConfiguration config = configs.get(fileName);

        if (config == null) return;

        try {
            config.save(file);
        } catch (IOException e) {
            plugin.getLogger().warning("Failed to save config: " + fileName);
            e.printStackTrace();
        }
    }

    public void save() {
        configs.keySet().forEach(this::save);
    }
}
