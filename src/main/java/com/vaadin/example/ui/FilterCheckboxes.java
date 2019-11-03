package com.vaadin.example.ui;

import com.vaadin.example.search.Categories;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;

import java.util.*;

public class FilterCheckboxes extends HorizontalLayout {
    private Map<Categories, Checkbox> checkboxes = new EnumMap<>(Categories.class);

    public FilterCheckboxes() {
        addClassName("searchPanel");
        setWidth("100%");
        for (Categories category : Categories.values()) {
            Checkbox checkbox = new Checkbox(category.getLabel());
            checkboxes.put(category, checkbox);
            add(checkbox);
        }
    }

    public List<String> getFilter() {
        List<String> result = new ArrayList<>();
        checkboxes.forEach((category, checkbox) -> {
            if (checkbox.getValue())
                result.add(category.getLabel());
        });
        return result;
    }
}
