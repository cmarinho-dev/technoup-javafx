package br.com.cmarinho.technoupjavafx.presentation.shared.crud;

import java.io.Serializable;
import java.util.List;

import br.com.cmarinho.technoupjavafx.persistence.FileRepository;
import br.com.cmarinho.technoupjavafx.persistence.Repository;
import br.com.cmarinho.technoupjavafx.presentation.shared.crud.CrudFormDialog.Action;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

public class CrudTab<T extends Serializable> extends BorderPane {
    private final CrudConfig<T> config;
    private final Repository<T> repository;
    private final ObservableList<T> entities = FXCollections.observableArrayList();
    private final CrudFormDialog<T> formDialog;

    public CrudTab(CrudConfig<T> config) {
        this.config = config;
        this.repository = new FileRepository<>(config.entityClass());
        this.formDialog = new CrudFormDialog<>(config);

        Button addButton = new Button("Adicionar novo");
        addButton.setOnAction(event -> openForm(null));

        Label title = new Label(config.tabTitle());
        title.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        HBox header = new HBox(12, title, addButton);
        header.setAlignment(Pos.CENTER_LEFT);
        header.setPadding(new Insets(16));

        setTop(header);
        setCenter(new CrudTable<>(config, entities, entity -> openForm(entity)));

        refresh();
    }

    private void openForm(T entity) {
        formDialog.show(getScene().getWindow(), entity)
                .ifPresent(result -> {
                    if (result.action() == Action.DELETE) {
                        repository.delete(result.entity());
                    } else if (entity == null) {
                        repository.save(result.entity());
                    } else {
                        repository.update(result.entity());
                    }

                    refresh();
                });
    }

    private void refresh() {
        List<T> savedEntities = repository.findAll();
        config.runAfterLoad(savedEntities);
        entities.setAll(savedEntities);
    }
}
