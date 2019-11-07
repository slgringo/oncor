package com.vaadin.example.search

import com.vaadin.example.utils.Config

import java.io.File
import java.util.Arrays
import java.util.Collections
import java.util.regex.Pattern
import java.util.stream.Collectors

/**
 * Класс для поиска вхождений текста в файлах
 */
class Searcher {

    /**
     * Список файлов для поиска
     * @return список файлов
     */
    private val fileListForSearch: List<File>
        get() {
            val folder: File
            folder = File(config["app.root"] + "md")
            folder.walk()
            val files = folder.listFiles()
            return if (files != null) {
                Arrays.stream(files).filter { f ->
                    !f.isDirectory && PATTERN.matcher(f.name)
                            .matches()
                }.collect(Collectors.toList())
            } else emptyList()
        }

    /**
     * Получить список файлов, удовлетворяющих условию поиска
     * @param searchText текст для поиска
     * @return список имен файлов
     */
    fun getMatchesFilenames(searchText: String): List<String> {
        return fileListForSearch.stream().filter { f -> fileMathches(f, searchText) }.map<String>( { it.getName() }).collect(Collectors.toList())
    }

    /**
     * Проверка файла на соответствие условию поиска
     * @param file файл
     * @param searchText текст для поиска
     * @return true если файл удовлетворяет условию поиска
     */
    private fun fileMathches(file: File, searchText: String): Boolean {
        val patternString = MdProcessor.getResourcePattern(file)
        if (patternString != null) {
            val pattern = Pattern.compile(patternString, Pattern.CASE_INSENSITIVE)
            return pattern.matcher(searchText).matches()
        } else {
            return false
        }
    }

    companion object {
        private val config = Config.instance
        private val PATTERN = Pattern.compile(".*\\.md")
    }
}
