package br.com.cmarinho.technoupjavafx.presentation.login;

import java.util.List;

import br.com.cmarinho.technoupjavafx.domain.entities.Login;
import br.com.cmarinho.technoupjavafx.persistence.FileRepository;
import br.com.cmarinho.technoupjavafx.persistence.Repository;
import br.com.cmarinho.technoupjavafx.presentation.login.LoginEditDialog.Action;
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

public class LoginTab extends BorderPane {
    private final Repository<Login> repository = new FileRepository<>(Login.class);
    private final ObservableList<Login> logins = FXCollections.observableArrayList();
    private final LoginAddDialog addDialog = new LoginAddDialog();
    private final LoginEditDialog editDialog = new LoginEditDialog();

    public LoginTab() {
        Label title = new Label("Logins");
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

    private TableView<Login> createTable() {
        TableView<Login> table = new TableView<>(logins);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);

        TableColumn<Login, Number> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getId()));
        idColumn.setMaxWidth(80);

        TableColumn<Login, String> usernameColumn = new TableColumn<>("Usuario");
        usernameColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getUsername()));

        TableColumn<Login, String> passwordColumn = new TableColumn<>("Senha");
        passwordColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getPassword()));

        TableColumn<Login, String> emailColumn = new TableColumn<>("Email");
        emailColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getEmail()));

        table.getColumns().add(idColumn);
        table.getColumns().add(usernameColumn);
        table.getColumns().add(passwordColumn);
        table.getColumns().add(emailColumn);
        table.setRowFactory(tableView -> {
            TableRow<Login> row = new TableRow<>();
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
                .ifPresent(login -> {
                    repository.save(login);
                    refresh();
                });
    }

    private void openEditDialog(Login login) {
        editDialog.show(getScene().getWindow(), login)
                .ifPresent(result -> {
                    if (result.action() == Action.DELETE) {
                        repository.delete(result.login());
                    } else {
                        repository.update(result.login());
                    }

                    refresh();
                });
    }

    private void refresh() {
        List<Login> savedLogins = repository.findAll();
        Login.syncIdCounter(savedLogins);
        logins.setAll(savedLogins);
    }
}
