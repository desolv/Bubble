package gg.desolve.commons.config;

import gg.desolve.commons.Commons;
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

        // Ensure the configuration file exists
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
            Commons.getInstance().getLogger().warning("Failed to reload configuration: " + file.getName());
            e.printStackTrace();
        }
    }

    public void save() {
        try {
            if (config != null) {
                config.save(file);
            }
        } catch (Exception e) {
            Commons.getInstance().getLogger().warning("Failed to save configuration: " + file.getName());
            e.printStackTrace();
        }
    }

    public String getString(String path) {
        return getString(path, "Unknown");
    }

    public String getString(String path, String def) {
        return config.getString(path, def);
    }

    public int getInt(String path) {
        return getInt(path, 0);
    }

    public int getInt(String path, int def) {
        return config.getInt(path, def);
    }

    public boolean getBoolean(String path) {
        return getBoolean(path, false);
    }

    public boolean getBoolean(String path, boolean def) {
        return config.getBoolean(path, def);
    }

}
