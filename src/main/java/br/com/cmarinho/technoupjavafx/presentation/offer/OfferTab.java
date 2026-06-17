package br.com.cmarinho.technoupjavafx.presentation.offer;

import java.util.List;

import br.com.cmarinho.technoupjavafx.domain.entities.Offer;
import br.com.cmarinho.technoupjavafx.persistence.FileRepository;
import br.com.cmarinho.technoupjavafx.persistence.Repository;
import br.com.cmarinho.technoupjavafx.presentation.offer.OfferEditDialog.Action;
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

public class OfferTab extends BorderPane {
    private final Repository<Offer> repository = new FileRepository<>(Offer.class);
    private final ObservableList<Offer> offers = FXCollections.observableArrayList();
    private final OfferAddDialog addDialog = new OfferAddDialog();
    private final OfferEditDialog editDialog = new OfferEditDialog();

    public OfferTab() {
        Label title = new Label("Ofertas");
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

    private TableView<Offer> createTable() {
        TableView<Offer> table = new TableView<>(offers);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);

        TableColumn<Offer, Number> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getId()));
        idColumn.setMaxWidth(80);

        TableColumn<Offer, String> titleColumn = new TableColumn<>("Titulo");
        titleColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getTitle()));

        TableColumn<Offer, String> descriptionColumn = new TableColumn<>("Descricao");
        descriptionColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getDescription()));

        TableColumn<Offer, String> discountColumn = new TableColumn<>("Desconto (%)");
        discountColumn.setCellValueFactory(data -> new SimpleStringProperty(
                Double.toString(data.getValue().getDiscountPercentage())));

        table.getColumns().add(idColumn);
        table.getColumns().add(titleColumn);
        table.getColumns().add(descriptionColumn);
        table.getColumns().add(discountColumn);
        table.setRowFactory(tableView -> {
            TableRow<Offer> row = new TableRow<>();
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
                .ifPresent(offer -> {
                    repository.save(offer);
                    refresh();
                });
    }

    private void openEditDialog(Offer offer) {
        editDialog.show(getScene().getWindow(), offer)
                .ifPresent(result -> {
                    if (result.action() == Action.DELETE) {
                        repository.delete(result.offer());
                    } else {
                        repository.update(result.offer());
                    }

                    refresh();
                });
    }

    private void refresh() {
        List<Offer> savedOffers = repository.findAll();
        Offer.syncIdCounter(savedOffers);
        offers.setAll(savedOffers);
    }
}
