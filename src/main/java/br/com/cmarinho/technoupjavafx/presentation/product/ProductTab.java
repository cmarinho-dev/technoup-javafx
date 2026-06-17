package br.com.cmarinho.technoupjavafx.presentation.product;

import java.util.List;

import br.com.cmarinho.technoupjavafx.domain.entities.Product;
import br.com.cmarinho.technoupjavafx.persistence.FileRepository;
import br.com.cmarinho.technoupjavafx.persistence.Repository;
import br.com.cmarinho.technoupjavafx.presentation.product.ProductEditDialog.Action;
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

public class ProductTab extends BorderPane {
    private final Repository<Product> repository = new FileRepository<>(Product.class);
    private final ObservableList<Product> products = FXCollections.observableArrayList();
    private final ProductAddDialog addDialog = new ProductAddDialog();
    private final ProductEditDialog editDialog = new ProductEditDialog();

    public ProductTab() {
        Label title = new Label("Produtos");
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

    private TableView<Product> createTable() {
        TableView<Product> table = new TableView<>(products);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);

        TableColumn<Product, Number> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getId()));
        idColumn.setMaxWidth(80);

        TableColumn<Product, String> nameColumn = new TableColumn<>("Nome");
        nameColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getName()));

        TableColumn<Product, String> priceColumn = new TableColumn<>("Preco");
        priceColumn.setCellValueFactory(data -> new SimpleStringProperty(Double.toString(data.getValue().getPrice())));

        TableColumn<Product, String> stockColumn = new TableColumn<>("Estoque");
        stockColumn.setCellValueFactory(data -> new SimpleStringProperty(Integer.toString(data.getValue().getStockQuantity())));

        table.getColumns().add(idColumn);
        table.getColumns().add(nameColumn);
        table.getColumns().add(priceColumn);
        table.getColumns().add(stockColumn);
        table.setRowFactory(tableView -> {
            TableRow<Product> row = new TableRow<>();
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
                .ifPresent(product -> {
                    repository.save(product);
                    refresh();
                });
    }

    private void openEditDialog(Product product) {
        editDialog.show(getScene().getWindow(), product)
                .ifPresent(result -> {
                    if (result.action() == Action.DELETE) {
                        repository.delete(result.product());
                    } else {
                        repository.update(result.product());
                    }

                    refresh();
                });
    }

    private void refresh() {
        List<Product> savedProducts = repository.findAll();
        Product.syncIdCounter(savedProducts);
        products.setAll(savedProducts);
    }
}
