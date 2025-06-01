package gg.desolve.api.blueprint;

import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.loader.ConfigurationLoader;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class Configuration {

    private final Map<String, ConfigurationLoader<CommentedConfigurationNode>> loaders = new HashMap<>();
    private final Map<String, CommentedConfigurationNode> roots = new HashMap<>();

    public Configuration(String folder, String... files) {
        Path folderPath = Paths.get(folder);
        try {
            if (Files.notExists(folderPath)) {
                Files.createDirectories(folderPath);
            }
        } catch (IOException e) {
            throw new IllegalStateException("Could not create configuration folder: " + folderPath, e);
        }

        for (String fileName : files) {
            Path filePath = folderPath.resolve(fileName);

            try {
                if (Files.notExists(filePath)) {
                    Files.createFile(filePath);
                }
            } catch (IOException e) {
                throw new IllegalStateException("Could not create configuration file: " + filePath, e);
            }

            YamlConfigurationLoader loader = YamlConfigurationLoader.builder()
                    .path(filePath)
                    .indent(2)
                    .build();

            CommentedConfigurationNode rootNode;
            try {
                rootNode = loader.load();
            } catch (IOException ex) {
                ex.printStackTrace();
                rootNode = loader.createNode();
            }

            this.loaders.put(fileName, loader);
            this.roots.put(fileName, rootNode);
        }
    }

    public CommentedConfigurationNode getRoot(String fileName) {
        if (!roots.containsKey(fileName)) {
            throw new IllegalArgumentException("Configuration file not registered: " + fileName);
        }
        return roots.get(fileName);
    }

    public void save(String fileName) {
        ConfigurationLoader<CommentedConfigurationNode> loader = loaders.get(fileName);
        CommentedConfigurationNode root = roots.get(fileName);
        if (loader == null || root == null) throw new IllegalArgumentException("Cannot save unknown file: " + fileName);

        try {
            loader.save(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveAll() {
        for (String fileName : loaders.keySet()) {
            save(fileName);
        }
    }

    public void reload(String fileName) {
        ConfigurationLoader<CommentedConfigurationNode> loader = loaders.get(fileName);
        if (loader == null) throw new IllegalArgumentException("Cannot reload unknown file: " + fileName);

        try {
            CommentedConfigurationNode newRoot = loader.load();
            roots.put(fileName, newRoot);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void reloadAll() {
        for (String fileName : loaders.keySet()) {
            reload(fileName);
        }
    }
}
