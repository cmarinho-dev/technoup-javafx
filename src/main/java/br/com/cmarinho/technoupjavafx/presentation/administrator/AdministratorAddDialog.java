package br.com.cmarinho.technoupjavafx.presentation.administrator;

import java.util.Optional;

import br.com.cmarinho.technoupjavafx.domain.entities.Administrator;
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

public class AdministratorAddDialog {
    public Optional<Administrator> show(Window owner) {
        Dialog<Administrator> dialog = new Dialog<>();
        dialog.initOwner(owner);
        dialog.setTitle("Novo administrador");
        dialog.setHeaderText("Cadastre um novo administrador");

        ButtonType saveButtonType = new ButtonType("Salvar", ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().add(saveButtonType);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);

        TextField nameInput = new TextField();
        TextField emailInput = new TextField();
        TextField departmentInput = new TextField();

        VBox form = new VBox(12);
        form.setPadding(new Insets(20));
        form.getChildren().addAll(
                createRow("Nome", nameInput),
                createRow("Email", emailInput),
                createRow("Departamento", departmentInput));
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

                    if (emailInput.getText().isBlank()) {
                        showError(owner, "Email e obrigatorio.");
                        emailInput.requestFocus();
                        event.consume();
                        return;
                    }

                    if (departmentInput.getText().isBlank()) {
                        showError(owner, "Departamento e obrigatorio.");
                        departmentInput.requestFocus();
                        event.consume();
                    }
                });

        dialog.setResultConverter(buttonType -> {
            if (buttonType == saveButtonType) {
                return new Administrator(
                        nameInput.getText().trim(),
                        emailInput.getText().trim(),
                        departmentInput.getText().trim());
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
