package com.vaadin.example.ui

import com.vaadin.example.search.Categories
import com.vaadin.flow.component.checkbox.Checkbox
import com.vaadin.flow.component.orderedlayout.HorizontalLayout

class FilterCheckboxes : HorizontalLayout() {
    val checkboxes = HashMap<Categories, Checkbox>()

    init {
        setWidthFull()
    }

    fun refresh(categories : Map<Categories, Int>) {
        checkboxes.clear()
        removeAll()
        categories.forEach {
            val checkbox = Checkbox("${it.key.label} (${it.value})")
            checkboxes.put(it.key, checkbox)
            add(checkbox)
        }
    }

    fun getFilter() : Set<Categories> {
        return checkboxes.entries.filter { it.value.value }.map { it.key }.toSet()
    }
}