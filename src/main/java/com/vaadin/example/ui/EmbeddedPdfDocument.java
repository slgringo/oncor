package com.vaadin.example.ui;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasSize;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.server.StreamResource;

/**
 * Компонент для отобрадения pdf
 */
@Tag("object")
class EmbeddedPdfDocument extends Component implements HasSize {

    EmbeddedPdfDocument(StreamResource resource) {
        this();
        getElement().setAttribute("data", resource);
    }

    private EmbeddedPdfDocument() {
        getElement().setAttribute("type", "application/pdf");
        setSizeFull();
    }
}
