package com.vaadin.example.ui

import com.vaadin.example.search.Categories
import com.vaadin.example.utils.Config
import com.vaadin.flow.component.html.H3
import com.vaadin.flow.component.html.Label
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.server.StreamResource
import org.slf4j.LoggerFactory
import java.io.FileInputStream
import java.io.IOException

class DescriptionItem(val category : Categories?, val title : String, val description : List<String>?,
                      val resourceFilename : String, var viewer : VerticalLayout) : VerticalLayout() {
    companion object {
        @Suppress("JAVA_CLASS_ON_COMPANION")
        @JvmStatic
        private val logger = LoggerFactory.getLogger(javaClass.enclosingClass)
        @JvmStatic
        val config = Config.instance
    }

    init {
        val titleLabel = H3(title)
        add(titleLabel)
        if (description != null) {
            description.forEach {
                val descriptionLabel = Label(it)
                add(descriptionLabel)
            }
        }
        addClickListener { showDocument(resourceFilename) }
    }

    fun showDocument(filename : String) {
        viewer.removeAll()
        try {
            FileInputStream(getDocDir() + filename).use {
                val data = ByteArray(it.available())
                it.read(data)
                val streamResource = StreamResource(filename, {outputStream, vaadinSession ->  outputStream.write(data)})
                viewer.add(EmbeddedPdfDocument(streamResource))
            }
        } catch (e : IOException) {
            logger.error(e.toString())
        }

    }

    fun getDocDir() : String = config.get("app.root") + "docs/"
}