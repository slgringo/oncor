package com.vaadin.example.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

public class Config {
    private static final Logger LOG = LoggerFactory.getLogger(Config.class);

    private static Config config;

    private Properties properties = new Properties();

    public static Config getInstance() {
        if (config == null)
            config = new Config();
        return config;
    }

    private Config() {
        String configPath = System.getenv("ONCOR_CONFIG_PATH");
        if (configPath == null)
            configPath = "../webapps/oncor/";
        File file = new File(configPath + "oncor.properties");
        try (FileInputStream stream = new FileInputStream(file)) {
            properties.load(new InputStreamReader(stream, StandardCharsets.UTF_8));

        } catch (FileNotFoundException e) {
            LOG.info("External configuration file {} is not found, skipping", file);

        } catch (IOException e) {
            LOG.error("Failed to load configuration file: {}", file, e);
            throw new UncheckedIOException(e);
        }
    }

    public String get(String key) {
        return properties.getProperty(key);
    }

    public String get(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }
}
