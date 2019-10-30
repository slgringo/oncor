package com.vaadin.example.ui;

import com.vaadin.example.search.FileUtils;
import com.vaadin.example.search.MdProcessor;
import com.vaadin.example.search.Searcher;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Input;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.server.StreamResourceWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Панель с инструментами поиска и вывода результатов
 */
public class SearchPanel extends VerticalLayout {
    private static final Logger LOG = LoggerFactory.getLogger(SearchPanel.class);

    private Searcher searcher = new Searcher();
    private List<Button> resultLLabels = new ArrayList<>();
    private VerticalLayout textLayout = new VerticalLayout();
    private VerticalLayout viewer = new VerticalLayout();
    private HorizontalLayout resultLayout = new HorizontalLayout();

    public SearchPanel() {
        setSizeFull();
        HorizontalLayout buttonLayout = new HorizontalLayout();
        add(buttonLayout);
        Input filterString = new Input();
        Button button = new Button("Search",
                event -> search(filterString.getValue()));
        buttonLayout.add(filterString);
        buttonLayout.setWidth("80%");
        filterString.setWidth("90%");
        buttonLayout.add(button);
        add(resultLayout);
        resultLayout.add(textLayout);
        resultLayout.addAndExpand(viewer);
        resultLayout.setSizeFull();
        setHeight("100%");
    }

    /**
     * Запустить поиск по документам
     * @param searchText текст для поиска
     */
    private void search(String searchText) {
        viewer.removeAll();
        resultLLabels.forEach(l -> textLayout.remove(l));
        resultLLabels.clear();
        List<String> results = searcher.getMatchesFilenames(searchText);
        results.forEach(r -> {
            String fileData = FileUtils.getFileData(r);
            String title = MdProcessor.getTiltle(fileData);
            Button button = new Button(title);
            String resourceFilename = MdProcessor.getResourceFilename(fileData);
            button.addClickListener(event -> showDocument(resourceFilename));
            resultLLabels.add(button);
            textLayout.add(button);
        });
    }

    /**
     * Показать документ
     * @param filename имя файла
     */
    private void showDocument(String filename) {
        viewer.removeAll();
        byte[] data;
        try (InputStream inputStream = getClass().getResourceAsStream("/" + filename)) {
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
}
