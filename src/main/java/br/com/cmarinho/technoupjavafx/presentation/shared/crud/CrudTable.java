package br.com.cmarinho.technoupjavafx.presentation.shared.crud;

import java.io.Serializable;
import java.util.function.Consumer;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;

public class CrudTable<T extends Serializable> extends TableView<T> {
    public CrudTable(CrudConfig<T> config, ObservableList<T> entities, Consumer<T> onEdit) {
        setItems(entities);
        setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);

        TableColumn<T, String> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(data -> new SimpleStringProperty(config.idProvider().apply(data.getValue())));
        idColumn.setMaxWidth(80);
        getColumns().add(idColumn);

        for (CrudField<T> field : config.fields()) {
            TableColumn<T, String> column = new TableColumn<>(field.label());
            column.setCellValueFactory(data -> new SimpleStringProperty(field.tableValue(data.getValue())));
            getColumns().add(column);
        }

        setRowFactory(table -> {
            TableRow<T> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && !row.isEmpty()) {
                    onEdit.accept(row.getItem());
                }
            });

            return row;
        });
    }
}
