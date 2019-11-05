package com.vaadin.example.search;

import com.vaadin.example.utils.FileUtils;
import com.vladsch.flexmark.ast.Heading;
import com.vladsch.flexmark.ast.Paragraph;
import com.vladsch.flexmark.ext.yaml.front.matter.AbstractYamlFrontMatterVisitor;
import com.vladsch.flexmark.ext.yaml.front.matter.YamlFrontMatterExtension;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.ast.Node;
import com.vladsch.flexmark.util.data.MutableDataSet;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Класс для работы с markdown документами
 */
public class MdProcessor {
    private static final String DEFAULT_HEADER = "(Без заголовка)";

    private MdProcessor() {

    }

    /**
     * Получить заголовок из документа
     * @param fileData строковое представление файла
     * @return заголовок
     */
    public static String getCategory(String fileData) {
        Map<String, List<String>> data = getYamlData(fileData);
        return data.getOrDefault("title", Collections.singletonList("")).get(0);
    }

    /**
     * Получить имя файла ресурса из md файла
     * @param fileData строковое представление файла
     * @return имя файла
     */
    public static String getResourceFilename(String fileData) {
        return getYamlData(fileData).getOrDefault("resources", Collections.singletonList("")).get(0);
    }

    /**
     * Получить регулярное выражение для проверки условия поиска
     * @param file файл md
     * @return регулярное выражение
     */
    static String getResourcePattern(File file) {
        return getYamlData(FileUtils.getFileData("md/" + file.getName())).getOrDefault("mkb", Collections.singletonList("")).get(0);
    }

    private static Map<String, List<String>> getYamlData(String input) {
        MutableDataSet options = new MutableDataSet();
        Parser parser = Parser.builder(options.set(Parser.EXTENSIONS, Collections.singletonList(YamlFrontMatterExtension.create()))).build();
        Node document = parser.parse(input);
        AbstractYamlFrontMatterVisitor visitor = new AbstractYamlFrontMatterVisitor();
        visitor.visit(document);
        return visitor.getData();
    }

    public static String getTitle(String input) {
        Parser parser = Parser.builder().build();
        Node document = parser.parse(input);
        Node header = document.getFirstChildAny(Heading.class);
        if (header == null)
            return DEFAULT_HEADER;
        return header.getChars().toString();
    }

    public static List<String> getContent(String input) {
        Parser parser = Parser.builder().build();
        Node document = parser.parse(input);
        List<String> result = new ArrayList<>();
        Node header = document.getFirstChildAny(Heading.class);
        if (header != null) {
            Node paragraph = header.getNextAny(Paragraph.class);
            while (paragraph != null) {
                result.add(paragraph.getChars().toString());
                paragraph = paragraph.getNextAny(Paragraph.class);
            }
        }
        return result;
    }
}
