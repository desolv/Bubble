package gg.desolve.bubble.toolkit;

import gg.desolve.bubble.Bubble;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Configuration {

    private final File folder;
    private final Map<String, Map<String, Object>> configs = new HashMap<>();
    private final Yaml yaml = new Yaml();

    public Configuration(String path, String... files) {
        this.folder = new File(path);

        Arrays.stream(files).forEach(this::load);
    }

    private void load(String fileName) {
        File file = new File(folder, fileName);

        if (!file.exists()) {
            file.getParentFile().mkdirs();
            try (InputStream in = getClass().getClassLoader().getResourceAsStream(fileName)) {
                if (in == null) {
                    Bubble.getLogger().error("Could not find resource file: " + fileName);
                    return;
                }

                Files.copy(in, file.toPath());
            } catch (IOException e) {
                Bubble.getLogger().error("Failed to copy default config: " + fileName);
                e.printStackTrace();
            }
        }

        try (InputStream input = new FileInputStream(file)) {
            Map<String, Object> config = (Map<String, Object>) yaml.load(input);
            configs.put(fileName, config != null ? config : new HashMap<>());
        } catch (IOException e) {
            Bubble.getLogger().error("Failed to load config: " + fileName);
            e.printStackTrace();
        }
    }

    public Object get(String fileName, String dottedPath) {
        Map<String, Object> current = configs.get(fileName);
        if (current == null) return null;

        String[] parts = dottedPath.split("\\.");
        Object value = current;

        for (String part : parts) {
            if (!(value instanceof Map<?, ?> map)) return null;
            value = map.get(part);
        }

        return value;
    }
}
