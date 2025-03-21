package gg.desolve.bubble.config;

import gg.desolve.bubble.Bubble;
import lombok.Getter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

@Getter
public class Config {

    private final File file;
    private FileConfiguration config;

    public Config(JavaPlugin plugin, String name) {
        this.file = new File(plugin.getDataFolder(), name);

        if (!file.exists()) {
            file.getParentFile().mkdirs();
            plugin.saveResource(name, false);
        }

        config = YamlConfiguration.loadConfiguration(file);
    }

    public void reload() {
        try {
            config = YamlConfiguration.loadConfiguration(file);
        } catch (Exception e) {
            Bubble.getInstance().getLogger().warning("Failed to reload configuration: " + file.getName());
            e.printStackTrace();
        }
    }

    public void save() {
        try {
            if (config != null) {
                config.save(file);
            }
        } catch (Exception e) {
            Bubble.getInstance().getLogger().warning("Failed to save configuration: " + file.getName());
            e.printStackTrace();
        }
    }

    public String getString(String path) {
        return config.getString(path, "Unknown message");
    }

    public int getInt(String path) {
        return config.getInt(path, 0);
    }

    public boolean getBoolean(String path) {
        return config.getBoolean(path, false);
    }

}
