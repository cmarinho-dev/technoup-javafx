package br.com.cmarinho.technoupjavafx.presentation.login;

import java.util.Optional;

import br.com.cmarinho.technoupjavafx.domain.entities.Login;
import br.com.cmarinho.technoupjavafx.presentation.shared.dialogs.ConfirmationDialog;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Window;

public class LoginEditDialog {
    public enum Action {
        SAVE,
        DELETE
    }

    public record Result(Action action, Login login) {
    }

    public Optional<Result> show(Window owner, Login login) {
        Dialog<Result> dialog = new Dialog<>();
        dialog.initOwner(owner);
        dialog.setTitle("Editar login");
        dialog.setHeaderText("Atualize ou exclua o login");

        ButtonType saveButtonType = new ButtonType("Salvar", ButtonData.OK_DONE);
        ButtonType deleteButtonType = new ButtonType("Excluir", ButtonData.LEFT);
        dialog.getDialogPane().getButtonTypes().add(saveButtonType);
        dialog.getDialogPane().getButtonTypes().add(deleteButtonType);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);

        TextField usernameInput = new TextField(login.getUsername());
        PasswordField passwordInput = new PasswordField();
        passwordInput.setText(login.getPassword());
        TextField emailInput = new TextField(login.getEmail());

        VBox form = new VBox(12);
        form.setPadding(new Insets(20));
        form.getChildren().addAll(
                createRow("Usuario", usernameInput),
                createRow("Senha", passwordInput),
                createRow("Email", emailInput));
        dialog.getDialogPane().setContent(form);

        dialog.getDialogPane().lookupButton(saveButtonType).addEventFilter(
                javafx.event.ActionEvent.ACTION,
                event -> {
                    if (usernameInput.getText().isBlank()) {
                        showError(owner, "Usuario e obrigatorio.");
                        usernameInput.requestFocus();
                        event.consume();
                        return;
                    }

                    if (passwordInput.getText().isBlank()) {
                        showError(owner, "Senha e obrigatoria.");
                        passwordInput.requestFocus();
                        event.consume();
                        return;
                    }

                    if (emailInput.getText().isBlank()) {
                        showError(owner, "Email e obrigatorio.");
                        emailInput.requestFocus();
                        event.consume();
                    }
                });

        Button deleteButton = (Button) dialog.getDialogPane().lookupButton(deleteButtonType);
        deleteButton.addEventFilter(javafx.event.ActionEvent.ACTION, event -> {
            boolean confirmed = ConfirmationDialog.confirm(
                    owner,
                    "Excluir login",
                    "Tem certeza que deseja excluir este login?");

            if (!confirmed) {
                event.consume();
            }
        });

        dialog.setResultConverter(buttonType -> {
            if (buttonType == saveButtonType) {
                login.setUsername(usernameInput.getText().trim());
                login.setPassword(passwordInput.getText().trim());
                login.setEmail(emailInput.getText().trim());
                return new Result(Action.SAVE, login);
            }

            if (buttonType == deleteButtonType) {
                return new Result(Action.DELETE, login);
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
