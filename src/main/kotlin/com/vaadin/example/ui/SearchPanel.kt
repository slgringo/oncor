package com.vaadin.example.ui

import com.vaadin.example.search.Categories
import com.vaadin.example.search.MdProcessor
import com.vaadin.example.search.Searcher
import com.vaadin.example.utils.FileUtils
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.html.H1
import com.vaadin.flow.component.orderedlayout.FlexComponent
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.component.textfield.TextField

class SearchPanel : VerticalLayout() {
    private val searcher = Searcher()
    private val resultItems : MutableList<DescriptionItem> = mutableListOf()
    private val textLayout = VerticalLayout()
    val viewer = VerticalLayout()
    private val filterCheckboxes = FilterCheckboxes()

    init {
        setSizeFull()
        alignItems = FlexComponent.Alignment.CENTER
        val title = H1("Поиск документов")
        add(title)
        val buttonLayout = HorizontalLayout()
        add(buttonLayout)
        val filterString = TextField()
        filterString.addKeyPressListener { event ->
            if (event.key.matches("Enter")) search(filterString.value) }
        val button = Button("Найти") {search(filterString.value)}
        buttonLayout.add(filterString)
        buttonLayout.setWidthFull()
        buttonLayout.alignItems = FlexComponent.Alignment.CENTER
        buttonLayout.add(button)
        add(filterCheckboxes)
        val resultLayout = HorizontalLayout()
        add(resultLayout)
        resultLayout.add(textLayout)
        resultLayout.addAndExpand(viewer)
        resultLayout.setSizeFull()
        textLayout.setFlexGrow(1.0, filterString)
        textLayout.setWidthFull()
        setHeightFull()
    }

    fun search(searchText : String) {
        viewer.removeAll()
        resultItems.forEach { l -> textLayout.remove(l) }
        resultItems.clear()
        val results = searcher.getMatchesFilenames(searchText)
        var foundCategories = HashMap<Categories, Int>()
        results.forEach{
            val fileData = FileUtils.getFileData("md/$it")
            val categoryName = MdProcessor.getCategory(fileData)
            val category = Categories.getByLabel(categoryName)
            val resourceFilename = MdProcessor.getResourceFilename(fileData)
            val item = DescriptionItem(category, MdProcessor.getTitle(fileData), MdProcessor.getContent(fileData),
                    resourceFilename, viewer)
            resultItems.add(item)
            textLayout.add(item)
            if (category != null) {
                val itemCount : Int? = foundCategories[category]
                if (itemCount != null)
                    foundCategories.put(category, itemCount)
                else
                    foundCategories.put(category, 1)
            }
        }
        filterCheckboxes.refresh(foundCategories)
    }
}