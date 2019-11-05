package com.vaadin.example.search;

import com.vaadin.example.utils.Config;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Класс для поиска вхождений текста в файлах
 */
public class Searcher {
    private static final Config config = Config.getInstance();
    private static final Pattern PATTERN =  Pattern.compile(".*\\.md");

    /**
     * Получить список файлов, удовлетворяющих условию поиска
     * @param searchText текст для поиска
     * @return список имен файлов
     */
    public List<String> getMatchesFilenames(String searchText) {
        return getFileListForSearch().stream().filter(f -> fileMathches(f, searchText)).map(File::getName).collect(Collectors.toList());
    }

    /**
     * Список файлов для поиска
     * @return список файлов
     */
    private List<File> getFileListForSearch() {
        File folder;
        folder = new File(config.get("app.root") + "md");
        File [] files = folder.listFiles();
        if (files != null) {
            return Arrays.stream(files).filter(f -> !f.isDirectory() && PATTERN.matcher(f.getName())
                    .matches()).collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    /**
     * Проверка файла на соответствие условию поиска
     * @param file файл
     * @param searchText текст для поиска
     * @return true если файл удовлетворяет условию поиска
     */
    private boolean fileMathches(File file, String searchText) {
        String patternString = MdProcessor.getResourcePattern(file);
        if (patternString != null) {
            Pattern pattern = Pattern.compile(patternString, Pattern.CASE_INSENSITIVE);
            return pattern.matcher(searchText).matches();
        } else {
            return false;
        }
    }
}
