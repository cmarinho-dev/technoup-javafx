package br.com.cmarinho.technoupjavafx.presentation.store;

import java.util.Optional;

import br.com.cmarinho.technoupjavafx.domain.entities.Store;
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

public class StoreEditDialog {
    public enum Action {
        SAVE,
        DELETE
    }

    public record Result(Action action, Store store) {
    }

    public Optional<Result> show(Window owner, Store store) {
        Dialog<Result> dialog = new Dialog<>();
        dialog.initOwner(owner);
        dialog.setTitle("Editar loja");
        dialog.setHeaderText("Atualize ou exclua a loja");

        ButtonType saveButtonType = new ButtonType("Salvar", ButtonData.OK_DONE);
        ButtonType deleteButtonType = new ButtonType("Excluir", ButtonData.LEFT);
        dialog.getDialogPane().getButtonTypes().add(saveButtonType);
        dialog.getDialogPane().getButtonTypes().add(deleteButtonType);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);

        TextField nameInput = new TextField(store.getName());
        TextField addressInput = new TextField(store.getAddress());
        TextField phoneInput = new TextField(store.getPhone());

        VBox form = new VBox(12);
        form.setPadding(new Insets(20));
        form.getChildren().addAll(
                createRow("Nome", nameInput),
                createRow("Endereco", addressInput),
                createRow("Telefone", phoneInput));
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

                    if (addressInput.getText().isBlank()) {
                        showError(owner, "Endereco e obrigatorio.");
                        addressInput.requestFocus();
                        event.consume();
                        return;
                    }

                    if (phoneInput.getText().isBlank()) {
                        showError(owner, "Telefone e obrigatorio.");
                        phoneInput.requestFocus();
                        event.consume();
                    }
                });

        Button deleteButton = (Button) dialog.getDialogPane().lookupButton(deleteButtonType);
        deleteButton.addEventFilter(javafx.event.ActionEvent.ACTION, event -> {
            boolean confirmed = ConfirmationDialog.confirm(
                    owner,
                    "Excluir loja",
                    "Tem certeza que deseja excluir esta loja?");

            if (!confirmed) {
                event.consume();
            }
        });

        dialog.setResultConverter(buttonType -> {
            if (buttonType == saveButtonType) {
                store.setName(nameInput.getText().trim());
                store.setAddress(addressInput.getText().trim());
                store.setPhone(phoneInput.getText().trim());
                return new Result(Action.SAVE, store);
            }

            if (buttonType == deleteButtonType) {
                return new Result(Action.DELETE, store);
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

    private void showError(Window owner, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.initOwner(owner);
        alert.setTitle("Dados invalidos");
        alert.setHeaderText(message);
        alert.setContentText(null);
        alert.showAndWait();
    }
}
