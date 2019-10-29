package com.vaadin.example;

import com.vaadin.flow.component.html.Input;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.server.StreamResourceWriter;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Route("")
public class MainView extends VerticalLayout {
    private Searcher searcher = new Searcher();
    List<Button> resultLLabels = new ArrayList<>();
    VerticalLayout textLayout = new VerticalLayout();
    VerticalLayout viewer = new VerticalLayout();
    HorizontalLayout buttonLayout = new HorizontalLayout();

    public MainView() {
        add(buttonLayout);
        Input filterString = new Input();
        Button button = new Button("Search",
                event -> search(filterString.getValue()));
        buttonLayout.add(filterString);
        buttonLayout.setWidth("80%");
        filterString.setWidth("90%");
        buttonLayout.add(button);
        add(textLayout);
        add(viewer);
        viewer.setHeight("70%");
        setHeight("100%");
    }

    private void search(String searchText) {
        viewer.removeAll();
        resultLLabels.forEach(l -> textLayout.remove(l));
        resultLLabels.clear();
        List<String> results = searcher.getMatchesFilenames(searchText);
        results.forEach(r -> {
            String fileData = getFileData(r);
            String title = getTiltle(fileData);
            Button button = new Button(title);
            String resourceFilename = getResourceFilename(fileData);
            button.addClickListener(event -> showDocument(resourceFilename));
            resultLLabels.add(button);
            textLayout.add(button);
        });
    }

    private void showDocument(String filename) {
        viewer.removeAll();
        byte[] data;
        try (InputStream inputStream = getClass().getResourceAsStream("/" + filename)) {
            data = new byte[inputStream.available()];
            inputStream.read(data);
        } catch (IOException e) {
            data = null;
        }
        final byte [] data2 = data;
        StreamResource streamResource = new StreamResource(filename, (StreamResourceWriter) (outputStream, vaadinSession) -> {
            if (data2 != null)
                outputStream.write(data2);
        });
        viewer.add(new EmbeddedPdfDocument(streamResource));
    }

    private String getTiltle(String fileData) {
        int index = fileData.indexOf("title: ");
        if (index > 0)
            return fileData.substring(index + 7).trim().split("\r")[0];
        else
            return "";
    }

        private String getResourceFilename(String fileData) {
        String[] parts = fileData.split("resources:\r\n.*?-");
        if (parts.length > 1) {
            return parts[1].trim().split("\r")[0];
        }
        return "";
    }

    private String getFileData(String filename) {
        try {
            File file = new File(getClass().getResource("/" + filename).toURI().getPath());
            return new String(Files.readAllBytes(Paths.get(file.getCanonicalPath())));
        } catch (URISyntaxException | IOException e) {
            return "";
        }
    }
}

