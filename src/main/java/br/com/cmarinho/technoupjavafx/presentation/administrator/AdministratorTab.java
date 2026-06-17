package br.com.cmarinho.technoupjavafx.presentation.administrator;

import java.util.List;

import br.com.cmarinho.technoupjavafx.domain.entities.Administrator;
import br.com.cmarinho.technoupjavafx.persistence.FileRepository;
import br.com.cmarinho.technoupjavafx.persistence.Repository;
import br.com.cmarinho.technoupjavafx.presentation.administrator.AdministratorEditDialog.Action;
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

public class AdministratorTab extends BorderPane {
    private final Repository<Administrator> repository = new FileRepository<>(Administrator.class);
    private final ObservableList<Administrator> administrators = FXCollections.observableArrayList();
    private final AdministratorAddDialog addDialog = new AdministratorAddDialog();
    private final AdministratorEditDialog editDialog = new AdministratorEditDialog();

    public AdministratorTab() {
        Label title = new Label("Administradores");
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

    private TableView<Administrator> createTable() {
        TableView<Administrator> table = new TableView<>(administrators);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);

        TableColumn<Administrator, Number> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getId()));
        idColumn.setMaxWidth(80);

        TableColumn<Administrator, String> nameColumn = new TableColumn<>("Nome");
        nameColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getName()));

        TableColumn<Administrator, String> emailColumn = new TableColumn<>("Email");
        emailColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getEmail()));

        TableColumn<Administrator, String> departmentColumn = new TableColumn<>("Departamento");
        departmentColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getDepartment()));

        table.getColumns().add(idColumn);
        table.getColumns().add(nameColumn);
        table.getColumns().add(emailColumn);
        table.getColumns().add(departmentColumn);
        table.setRowFactory(tableView -> {
            TableRow<Administrator> row = new TableRow<>();
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
                .ifPresent(administrator -> {
                    repository.save(administrator);
                    refresh();
                });
    }

    private void openEditDialog(Administrator administrator) {
        editDialog.show(getScene().getWindow(), administrator)
                .ifPresent(result -> {
                    if (result.action() == Action.DELETE) {
                        repository.delete(result.administrator());
                    } else {
                        repository.update(result.administrator());
                    }

                    refresh();
                });
    }

    private void refresh() {
        List<Administrator> savedAdministrators = repository.findAll();
        Administrator.syncIdCounter(savedAdministrators);
        administrators.setAll(savedAdministrators);
    }
}
