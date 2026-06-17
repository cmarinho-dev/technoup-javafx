package br.com.cmarinho.technoupjavafx.presentation.offer;

import java.util.Optional;

import br.com.cmarinho.technoupjavafx.domain.entities.Offer;
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

public class OfferAddDialog {
    public Optional<Offer> show(Window owner) {
        Dialog<Offer> dialog = new Dialog<>();
        dialog.initOwner(owner);
        dialog.setTitle("Nova oferta");
        dialog.setHeaderText("Cadastre uma nova oferta");

        ButtonType saveButtonType = new ButtonType("Salvar", ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().add(saveButtonType);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);

        TextField titleInput = new TextField();
        TextField descriptionInput = new TextField();
        TextField discountInput = new TextField();

        VBox form = new VBox(12);
        form.setPadding(new Insets(20));
        form.getChildren().addAll(
                createRow("Titulo", titleInput),
                createRow("Descricao", descriptionInput),
                createRow("Desconto (%)", discountInput));
        dialog.getDialogPane().setContent(form);

        dialog.getDialogPane().lookupButton(saveButtonType).addEventFilter(
                javafx.event.ActionEvent.ACTION,
                event -> {
                    if (titleInput.getText().isBlank()) {
                        showError(owner, "Titulo e obrigatorio.");
                        titleInput.requestFocus();
                        event.consume();
                        return;
                    }

                    if (descriptionInput.getText().isBlank()) {
                        showError(owner, "Descricao e obrigatoria.");
                        descriptionInput.requestFocus();
                        event.consume();
                        return;
                    }

                    if (!isDecimal(discountInput.getText())) {
                        showError(owner, "Desconto deve ser um numero.");
                        discountInput.requestFocus();
                        event.consume();
                    }
                });

        dialog.setResultConverter(buttonType -> {
            if (buttonType == saveButtonType) {
                return new Offer(
                        titleInput.getText().trim(),
                        descriptionInput.getText().trim(),
                        Double.parseDouble(discountInput.getText().trim()));
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

    private void showError(Window owner, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.initOwner(owner);
        alert.setTitle("Dados invalidos");
        alert.setHeaderText(message);
        alert.setContentText(null);
        alert.showAndWait();
    }
}
