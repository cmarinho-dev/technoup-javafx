package br.com.cmarinho.technoupjavafx.presentation.shared.crud;

import java.io.Serializable;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

public record CrudConfig<T extends Serializable>(
        String tabTitle,
        String singularTitle,
        Class<T> entityClass,
        Function<T, String> idProvider,
        List<CrudField<T>> fields,
        Function<CrudFormData, T> createEntity,
        BiConsumer<T, CrudFormData> updateEntity,
        Consumer<List<T>> afterLoad) {
    public void runAfterLoad(List<T> entities) {
        if (afterLoad != null) {
            afterLoad.accept(entities);
        }
    }
}
