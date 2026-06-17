package br.com.cmarinho.technoupjavafx.presentation.customer;

import java.util.List;

import br.com.cmarinho.technoupjavafx.domain.entities.Customer;
import br.com.cmarinho.technoupjavafx.persistence.FileRepository;
import br.com.cmarinho.technoupjavafx.persistence.Repository;
import br.com.cmarinho.technoupjavafx.presentation.customer.CustomerEditDialog.Action;
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

public class CustomerTab extends BorderPane {
    private final Repository<Customer> repository = new FileRepository<>(Customer.class);
    private final ObservableList<Customer> customers = FXCollections.observableArrayList();
    private final CustomerAddDialog addDialog = new CustomerAddDialog();
    private final CustomerEditDialog editDialog = new CustomerEditDialog();

    public CustomerTab() {
        Label title = new Label("Clientes");
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

    private TableView<Customer> createTable() {
        TableView<Customer> table = new TableView<>(customers);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);

        TableColumn<Customer, Number> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getId()));
        idColumn.setMaxWidth(80);

        TableColumn<Customer, String> nameColumn = new TableColumn<>("Nome");
        nameColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getName()));

        TableColumn<Customer, String> cpfColumn = new TableColumn<>("CPF");
        cpfColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getCpf()));

        TableColumn<Customer, String> birthDateColumn = new TableColumn<>("Data de nascimento");
        birthDateColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getBirthDate()));

        table.getColumns().add(idColumn);
        table.getColumns().add(nameColumn);
        table.getColumns().add(cpfColumn);
        table.getColumns().add(birthDateColumn);
        table.setRowFactory(tableView -> {
            TableRow<Customer> row = new TableRow<>();
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
                .ifPresent(customer -> {
                    repository.save(customer);
                    refresh();
                });
    }

    private void openEditDialog(Customer customer) {
        editDialog.show(getScene().getWindow(), customer)
                .ifPresent(result -> {
                    if (result.action() == Action.DELETE) {
                        repository.delete(result.customer());
                    } else {
                        repository.update(result.customer());
                    }

                    refresh();
                });
    }

    private void refresh() {
        List<Customer> savedCustomers = repository.findAll();
        Customer.syncIdCounter(savedCustomers);
        customers.setAll(savedCustomers);
    }
}
