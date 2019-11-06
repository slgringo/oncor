package com.vaadin.example.utils

import org.slf4j.Logger
import org.slf4j.LoggerFactory

import java.io.File
import java.io.IOException
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Paths

/**
 * Класс для работы с файлами
 */
object FileUtils {
    private val LOG = LoggerFactory.getLogger(FileUtils::class.java)
    private val config = Config.instance

    /**
     * Получить файл как строку
     * @param filename имя файла
     * @return строковое содержимое файла
     */
    fun getFileData(filename: String): String {
        try {
            val file = File(config.get("app.root") + filename)
            return String(Files.readAllBytes(Paths.get(file.canonicalPath)), StandardCharsets.UTF_8)
        } catch (e: IOException) {
            LOG.error("Failed to open document {}", filename)
            return ""
        }

    }
}
