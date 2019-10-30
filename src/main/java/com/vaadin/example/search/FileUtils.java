package com.vaadin.example.search;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Класс для работы с файлами
 */
public class FileUtils {
    private static final Logger LOG = LoggerFactory.getLogger(FileUtils.class);

    private FileUtils() {

    }

    /**
     * Получить файл как строку
     * @param filename имя файла
     * @return строковое содержимое файла
     */
    public static String getFileData(String filename) {
        try {
            File file = new File(FileUtils.class.getResource("/" + filename).toURI().getPath());
            return new String(Files.readAllBytes(Paths.get(file.getCanonicalPath())));
        } catch (URISyntaxException | IOException e) {
            LOG.error("Failed to open document {}", filename);
            return "";
        }
    }
}
