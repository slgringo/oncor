package com.vaadin.example.search;

public enum Categories {
    CLINIC_RECOMMENDATIONS("Клиничесие рекомендации"),
    ALGORITHM("Алгоритм");

    private String label;

    private Categories(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
