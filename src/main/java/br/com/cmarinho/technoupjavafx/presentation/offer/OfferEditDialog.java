package br.com.cmarinho.technoupjavafx.presentation.offer;

import java.util.Optional;

import br.com.cmarinho.technoupjavafx.domain.entities.Offer;
import br.com.cmarinho.technoupjavafx.presentation.shared.dialogs.ConfirmationDialog;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
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

public class OfferEditDialog {
    public enum Action {
        SAVE,
        DELETE
    }

    public record Result(Action action, Offer offer) {
    }

    public Optional<Result> show(Window owner, Offer offer) {
        Dialog<Result> dialog = new Dialog<>();
        dialog.initOwner(owner);
        dialog.setTitle("Editar oferta");
        dialog.setHeaderText("Atualize ou exclua a oferta");

        ButtonType saveButtonType = new ButtonType("Salvar", ButtonData.OK_DONE);
        ButtonType deleteButtonType = new ButtonType("Excluir", ButtonData.LEFT);
        dialog.getDialogPane().getButtonTypes().add(saveButtonType);
        dialog.getDialogPane().getButtonTypes().add(deleteButtonType);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);

        TextField titleInput = new TextField(offer.getTitle());
        TextField descriptionInput = new TextField(offer.getDescription());
        TextField discountInput = new TextField(Double.toString(offer.getDiscountPercentage()));

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

        Button deleteButton = (Button) dialog.getDialogPane().lookupButton(deleteButtonType);
        deleteButton.addEventFilter(javafx.event.ActionEvent.ACTION, event -> {
            boolean confirmed = ConfirmationDialog.confirm(
                    owner,
                    "Excluir oferta",
                    "Tem certeza que deseja excluir esta oferta?");

            if (!confirmed) {
                event.consume();
            }
        });

        dialog.setResultConverter(buttonType -> {
            if (buttonType == saveButtonType) {
                offer.setTitle(titleInput.getText().trim());
                offer.setDescription(descriptionInput.getText().trim());
                offer.setDiscountPercentage(Double.parseDouble(discountInput.getText().trim()));
                return new Result(Action.SAVE, offer);
            }

            if (buttonType == deleteButtonType) {
                return new Result(Action.DELETE, offer);
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
