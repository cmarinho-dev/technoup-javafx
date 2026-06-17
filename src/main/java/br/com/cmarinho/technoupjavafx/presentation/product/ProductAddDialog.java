package br.com.cmarinho.technoupjavafx.presentation.product;

import java.util.Optional;

import br.com.cmarinho.technoupjavafx.domain.entities.Product;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Window;

public class ProductAddDialog {
    public Optional<Product> show(Window owner) {
        Dialog<Product> dialog = new Dialog<>();
        dialog.initOwner(owner);
        dialog.setTitle("Novo produto");
        dialog.setHeaderText("Cadastre um novo produto");

        ButtonType saveButtonType = new ButtonType("Salvar", ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().add(saveButtonType);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);

        TextField nameInput = new TextField();
        TextField priceInput = new TextField();
        TextField stockInput = new TextField();

        VBox form = new VBox(12);
        form.setPadding(new Insets(20));
        form.getChildren().addAll(
                createRow("Nome", nameInput),
                createRow("Preco", priceInput),
                createRow("Estoque", stockInput));
        dialog.getDialogPane().setContent(form);

        dialog.getDialogPane().lookupButton(saveButtonType).addEventFilter(
                javafx.event.ActionEvent.ACTION,
                event -> {
                    if (nameInput.getText().isBlank()) {
                        showError(owner, "Nome e obrigatorio.");
                        nameInput.requestFocus();
                        event.consume();
                        return;
                    }

                    if (!isDecimal(priceInput.getText())) {
                        showError(owner, "Preco deve ser um numero.");
                        priceInput.requestFocus();
                        event.consume();
                        return;
                    }

                    if (!isInteger(stockInput.getText())) {
                        showError(owner, "Estoque deve ser um numero inteiro.");
                        stockInput.requestFocus();
                        event.consume();
                    }
                });

        dialog.setResultConverter(buttonType -> {
            if (buttonType == saveButtonType) {
                return new Product(
                        nameInput.getText().trim(),
                        Double.parseDouble(priceInput.getText().trim()),
                        Integer.parseInt(stockInput.getText().trim()));
            }

            return null;
        });

        return dialog.showAndWait();
    }

    private HBox createRow(String label, TextField input) {
        input.setPrefColumnCount(28);

        Label labelControl = new Label(label);
        labelControl.setMinWidth(150);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox row = new HBox(12, labelControl, spacer, input);
        row.setAlignment(Pos.CENTER_LEFT);
        return row;
    }

    private boolean isDecimal(String value) {
        try {
            Double.parseDouble(value.trim());
            return true;
        } catch (NumberFormatException exception) {
            return false;
        }
    }

    private boolean isInteger(String value) {
        try {
            Integer.parseInt(value.trim());
            return true;
        } catch (NumberFormatException exception) {
            return false;
        }
    }

    private void showError(Window owner, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.initOwner(owner);
        alert.setTitle("Dados invalidos");
        alert.setHeaderText(message);
        alert.setContentText(null);
        alert.showAndWait();
    }
}
