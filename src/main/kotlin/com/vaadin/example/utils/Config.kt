package com.vaadin.example.utils

import org.slf4j.Logger
import org.slf4j.LoggerFactory

import java.io.*
import java.nio.charset.StandardCharsets
import java.util.Properties

class Config private constructor() {

    private val properties = Properties()

    init {
        var configPath: String? = System.getenv("ONCOR_CONFIG_PATH")
        if (configPath == null)
            configPath = "../webapps/oncor/"
        val file = File(configPath + "oncor.properties")
        try {
            FileInputStream(file).use { stream ->
                properties.load(InputStreamReader(stream, StandardCharsets.UTF_8))

            }
        } catch (e: FileNotFoundException) {
            LOG.info("External configuration file {} is not found, skipping", file)

        } catch (e: IOException) {
            LOG.error("Failed to load configuration file: {}", file, e)
            throw UncheckedIOException(e)
        }

    }

    operator fun get(key: String): String {
        return properties.getProperty(key)
    }

    operator fun get(key: String, defaultValue: String): String {
        return properties.getProperty(key, defaultValue)
    }

    companion object {
        private val LOG = LoggerFactory.getLogger(Config::class.java)

        private var config: Config? = null

        val instance: Config
            get() {
                if (config == null)
                    config = Config()
                return config!!
            }
    }
}
