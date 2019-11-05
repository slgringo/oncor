package com.vaadin.example.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Класс для работы с файлами
 */
public class FileUtils {
    private static final Logger LOG = LoggerFactory.getLogger(FileUtils.class);
    private static final Config config = Config.getInstance();

    private FileUtils() {

    }

    /**
     * Получить файл как строку
     * @param filename имя файла
     * @return строковое содержимое файла
     */
    public static String getFileData(String filename) {
        try {
            File file = new File(config.get("app.root") + filename);
            return new String(Files.readAllBytes(Paths.get(file.getCanonicalPath())), StandardCharsets.UTF_8);
        } catch (IOException e) {
            LOG.error("Failed to open document {}", filename);
            return "";
        }
    }
}
