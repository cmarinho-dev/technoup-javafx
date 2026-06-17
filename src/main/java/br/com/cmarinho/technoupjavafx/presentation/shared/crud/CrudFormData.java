package br.com.cmarinho.technoupjavafx.presentation.shared.crud;

import java.util.Map;

public record CrudFormData(Map<String, String> values) {
    public String get(String fieldLabel) {
        return values.getOrDefault(fieldLabel, "");
    }

    public int getInt(String fieldLabel) {
        return Integer.parseInt(get(fieldLabel));
    }

    public double getDouble(String fieldLabel) {
        return Double.parseDouble(get(fieldLabel));
    }
}
