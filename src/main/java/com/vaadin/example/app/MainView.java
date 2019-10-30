package com.vaadin.example.app;

import com.vaadin.example.ui.SearchPanel;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

/**
 * Главный вид
 */
@Route("")
@SuppressWarnings("unused")
public class MainView extends VerticalLayout {

    public MainView() {
        SearchPanel searchPanel = new SearchPanel();
        add(searchPanel);
        setSizeFull();
    }
}

