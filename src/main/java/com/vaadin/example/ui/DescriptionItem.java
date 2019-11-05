package com.vaadin.example.ui;

import com.vaadin.example.search.Categories;
import com.vaadin.example.utils.Config;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.server.StreamResourceWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.IOException;

public class DescriptionItem extends VerticalLayout {
    private static final Logger LOG = LoggerFactory.getLogger(DescriptionItem.class);
    private static final Config config = Config.getInstance();

    private VerticalLayout viewer;

    public DescriptionItem(Categories category, String title, String description, String resourceFilename, VerticalLayout viewer) {
        this.viewer = viewer;
        setWidth("100%");
        setAlignItems(Alignment.CENTER);
        addClassName("descriptionItem");
        H3 titleLabel = new H3(title);
        add(titleLabel);
        Label descriptionLabel = new Label(description);
        add(descriptionLabel);
        addClickListener(event -> showDocument(resourceFilename));
    }

    /**
     * Показать документ
     * @param filename имя файла
     */
    private void showDocument(String filename) {
        viewer.removeAll();
        byte[] data;
        try (FileInputStream inputStream = new FileInputStream(getDocDir() + filename)) {
            data = new byte[inputStream.available()];
            inputStream.read(data);
        } catch (IOException e) {
            LOG.error("Failed to open document {}", filename);
            data = null;
        }
        final byte [] data2 = data;
        StreamResource streamResource = new StreamResource(filename, (StreamResourceWriter) (outputStream, vaadinSession) -> {
            if (data2 != null)
                outputStream.write(data2);
        });
        viewer.add(new EmbeddedPdfDocument(streamResource));
    }

    private String getDocDir() {
        return config.get("app.root") + "docs/";
    }
}
