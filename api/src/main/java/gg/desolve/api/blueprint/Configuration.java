package gg.desolve.api.blueprint;

import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Configuration {

    private final File plugin;
    private final Map<String, Map<String, Object>> resources = new HashMap<>();
    private final Yaml yaml = new Yaml();

    public Configuration(String plugin, String... resources) {
        this.plugin = new File("plugins/" + plugin);

        Arrays.stream(resources).forEach(this::load);
    }

    private void load(String resource) {
        File instance = new File(plugin, resource);

        if (!instance.exists()) {
            instance.getParentFile().mkdirs();
            try (InputStream ignored = getClass().getClassLoader().getResourceAsStream(resource)) {
                if (ignored != null) Files.copy(ignored, instance.toPath());
            } catch (IOException e) {
                Logger.error("Could not copy default configuration: " + resource);
            }
        }

        try (InputStream input = new FileInputStream(instance)) {
            Map<String, Object> config = yaml.load(input);
            resources.put(resource, config != null ? config : new HashMap<>());
        } catch (IOException e) {
            Logger.error("Could not load configuration: " + resource);
        }
    }

    public String get(String resource, String path) {
        Map<String, Object> map = resources.get(resource);
        if (map == null) {
            Logger.error("Configuration file not loaded: " + resource);
        }

        Object value = map;
        for (String part : path.split("\\.")) {
            if (!(value instanceof Map)) {
                Logger.warning("Invalid path segment: " + part + " in " + path);
            }
            value = ((Map<?, ?>) value).get(part);
            if (value == null) {
                Logger.warning("Missing configuration value for key: " + path + " in " + resource);
            }
        }

        return value != null ? value.toString() : "Missing key for path";
    }
}
