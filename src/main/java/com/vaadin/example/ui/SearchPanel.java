package com.vaadin.example.ui;

import com.vaadin.example.utils.FileUtils;
import com.vaadin.example.search.MdProcessor;
import com.vaadin.example.search.Searcher;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Input;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Панель с инструментами поиска и вывода результатов
 */
public class SearchPanel extends VerticalLayout {
    private Searcher searcher = new Searcher();
    private List<DescriptionItem> resultItems = new ArrayList<>();
    private VerticalLayout textLayout = new VerticalLayout();
    private VerticalLayout viewer = new VerticalLayout();
    private FilterCheckboxes filterCheckboxes = new FilterCheckboxes();

    public SearchPanel() {
        setSizeFull();
        setAlignItems(Alignment.CENTER);
        H1 title = new H1("Поиск документов");
        title.setClassName("lumo-success-text-color");
        add(title);
        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.addClassName("searchPanel");
        add(buttonLayout);
        Input filterString = new Input();
        Button button = new Button("Найти",
                event -> search(filterString.getValue()));
        button.addClassName("searchButton");
        buttonLayout.add(filterString);
        buttonLayout.setWidth("100%");
        filterString.setWidth("90%");
        filterString.setHeight("30px");
        buttonLayout.setAlignItems(Alignment.CENTER);
        buttonLayout.add(button);
        add(filterCheckboxes);
        HorizontalLayout resultLayout = new HorizontalLayout();
        add(resultLayout);
        resultLayout.add(textLayout);
        resultLayout.addAndExpand(viewer);
        resultLayout.setSizeFull();
        textLayout.addClassName("resultLayout");
        setHeight("100%");
    }

    /**
     * Запустить поиск по документам
     * @param searchText текст для поиска
     */
    private void search(String searchText) {
        viewer.removeAll();
        resultItems.forEach(l -> textLayout.remove(l));
        resultItems.clear();
        List<String> results = searcher.getMatchesFilenames(searchText);
        results.forEach(r -> {
            String fileData = FileUtils.getFileData("md/" + r);
            String category = MdProcessor.getCategory(fileData);
            String resourceFilename = MdProcessor.getResourceFilename(fileData);
            DescriptionItem item = new DescriptionItem(/*Categories.valueOf(category)*/null, MdProcessor.getTitle(fileData), //todo resolve encoding bug
                    MdProcessor.getContent(fileData), resourceFilename, viewer);
            resultItems.add(item);
            textLayout.add(item);
        });
    }
}
