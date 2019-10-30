package com.vaadin.example.search;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Класс для работы с markdown документами
 */
public class MdProcessor {
    private static final Logger LOG = LoggerFactory.getLogger(MdProcessor.class);

    private MdProcessor() {

    }

    /**
     * Получить заголовок из документа
     * @param fileData строковое представление файла
     * @return заголовок
     */
    public static String getTiltle(String fileData) {
        int index = fileData.indexOf("title: ");
        if (index > 0)
            return fileData.substring(index + 7).trim().split("\r")[0];
        else
            return "";
    }

    /**
     * Получить имя файла ресурса из md файла
     * @param fileData строковое представление файла
     * @return имя файла
     */
    public static String getResourceFilename(String fileData) {
        String[] parts = fileData.split("resources:\r\n.*?-");
        if (parts.length > 1) {
            return parts[1].trim().split("\r")[0];
        }
        return "";
    }

    /**
     * Получить регулярное выражение для проверки условия поиска
     * @param file файл md
     * @return регулярное выражение
     */
    static String getResourcePattern(File file) {
        try {
            String data = new String(Files.readAllBytes(Paths.get(file.getCanonicalPath())));
            int index = data.lastIndexOf("mkb:");
            if (index > 0) {
                String val = data.substring(index + 4).trim().split("\r")[0];
                if (val.length() > 2) {
                    return val.substring(1, val.length() - 2);
                }
            }
            return null;
        } catch (IOException e) {
            LOG.error("Failed to open document {}", file.getName());
            return null;
        }
    }
}
