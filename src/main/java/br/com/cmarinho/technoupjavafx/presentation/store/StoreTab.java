package br.com.cmarinho.technoupjavafx.presentation.store;

import java.util.List;

import br.com.cmarinho.technoupjavafx.domain.entities.Store;
import br.com.cmarinho.technoupjavafx.persistence.FileRepository;
import br.com.cmarinho.technoupjavafx.persistence.Repository;
import br.com.cmarinho.technoupjavafx.presentation.store.StoreEditDialog.Action;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

public class StoreTab extends BorderPane {
    private final Repository<Store> repository = new FileRepository<>(Store.class);
    private final ObservableList<Store> stores = FXCollections.observableArrayList();
    private final StoreAddDialog addDialog = new StoreAddDialog();
    private final StoreEditDialog editDialog = new StoreEditDialog();

    public StoreTab() {
        Label title = new Label("Lojas");
        title.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        Button addButton = new Button("Adicionar novo");
        addButton.setOnAction(event -> openAddDialog());

        HBox header = new HBox(12, title, addButton);
        header.setAlignment(Pos.CENTER_LEFT);
        header.setPadding(new Insets(16));

        setTop(header);
        setCenter(createTable());

        refresh();
    }

    private TableView<Store> createTable() {
        TableView<Store> table = new TableView<>(stores);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);

        TableColumn<Store, Number> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getId()));
        idColumn.setMaxWidth(80);

        TableColumn<Store, String> nameColumn = new TableColumn<>("Nome");
        nameColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getName()));

        TableColumn<Store, String> addressColumn = new TableColumn<>("Endereco");
        addressColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getAddress()));

        TableColumn<Store, String> phoneColumn = new TableColumn<>("Telefone");
        phoneColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getPhone()));

        table.getColumns().add(idColumn);
        table.getColumns().add(nameColumn);
        table.getColumns().add(addressColumn);
        table.getColumns().add(phoneColumn);
        table.setRowFactory(tableView -> {
            TableRow<Store> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && !row.isEmpty()) {
                    openEditDialog(row.getItem());
                }
            });
            return row;
        });

        return table;
    }

    private void openAddDialog() {
        addDialog.show(getScene().getWindow())
                .ifPresent(store -> {
                    repository.save(store);
                    refresh();
                });
    }

    private void openEditDialog(Store store) {
        editDialog.show(getScene().getWindow(), store)
                .ifPresent(result -> {
                    if (result.action() == Action.DELETE) {
                        repository.delete(result.store());
                    } else {
                        repository.update(result.store());
                    }

                    refresh();
                });
    }

    private void refresh() {
        List<Store> savedStores = repository.findAll();
        Store.syncIdCounter(savedStores);
        stores.setAll(savedStores);
    }
}
