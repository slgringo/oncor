package com.vaadin.example.search

enum class Categories private constructor(val label: String) {
    CLINIC_RECOMMENDATIONS("Клиничесие рекомендации"),
    ALGORITHM("Алгоритм");

    companion object {
        fun getByLabel(label : String) : Categories? {
            val it = values().iterator()
            while (it.hasNext()) {
                val value = it.next();
                if (value.label.equals(label))
                    return value;
            }
            return null;
        }
    }
}
