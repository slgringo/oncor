package com.vaadin.example.search

import com.vaadin.example.utils.FileUtils
import com.vladsch.flexmark.ast.Heading
import com.vladsch.flexmark.ast.Paragraph
import com.vladsch.flexmark.ext.yaml.front.matter.AbstractYamlFrontMatterVisitor
import com.vladsch.flexmark.ext.yaml.front.matter.YamlFrontMatterExtension
import com.vladsch.flexmark.parser.Parser
import com.vladsch.flexmark.util.ast.Node
import com.vladsch.flexmark.util.builder.Extension
import com.vladsch.flexmark.util.data.MutableDataSet

import java.io.File
import java.util.ArrayList
import java.util.Collections

/**
 * Класс для работы с markdown документами
 */
object MdProcessor {
    private val DEFAULT_HEADER = "(Без заголовка)"

    /**
     * Получить заголовок из документа
     * @param fileData строковое представление файла
     * @return заголовок
     */
    fun getCategory(fileData: String): String {
        val data = getYamlData(fileData)
        return (data as java.util.Map<String, List<String>>).getOrDefault("title", listOf<String>(""))[0]
    }

    /**
     * Получить имя файла ресурса из md файла
     * @param fileData строковое представление файла
     * @return имя файла
     */
    fun getResourceFilename(fileData: String): String {
        return (getYamlData(fileData) as java.util.Map<String, List<String>>).getOrDefault("resources", listOf<String>(""))[0]
    }

    /**
     * Получить регулярное выражение для проверки условия поиска
     * @param file файл md
     * @return регулярное выражение
     */
    fun getResourcePattern(file: File): String {
        return (getYamlData(FileUtils.getFileData("md/" + file.name)) as java.util.Map<String, List<String>>).getOrDefault("mkb", listOf<String>(""))[0]
    }

    private fun getYamlData(input: String): Map<String, List<String>> {
        val options = MutableDataSet()
        val parser = Parser.builder(options.set<Iterable<Extension>>(Parser.EXTENSIONS, listOf<YamlFrontMatterExtension>(YamlFrontMatterExtension.create()))).build()
        val document = parser.parse(input)
        val visitor = AbstractYamlFrontMatterVisitor()
        visitor.visit(document)
        return visitor.data
    }

    fun getTitle(input: String): String {
        val parser = Parser.builder().build()
        val document = parser.parse(input)
        val header = document.getFirstChildAny(Heading::class.java!!) ?: return DEFAULT_HEADER
        return header.chars.toString()
    }

    fun getContent(input: String): List<String> {
        val parser = Parser.builder().build()
        val document = parser.parse(input)
        val result = ArrayList<String>()
        val header = document.getFirstChildAny(Heading::class.java!!)
        if (header != null) {
            var paragraph: Node? = header.getNextAny(Paragraph::class.java!!)
            while (paragraph != null) {
                result.add(paragraph.chars.toString())
                paragraph = paragraph.getNextAny(Paragraph::class.java!!)
            }
        }
        return result
    }
}
