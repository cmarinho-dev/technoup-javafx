package br.com.cmarinho.technoupjavafx.presentation.customer;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Optional;

import br.com.cmarinho.technoupjavafx.domain.entities.Customer;
import br.com.cmarinho.technoupjavafx.presentation.shared.dialogs.ConfirmationDialog;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Window;
import javafx.util.StringConverter;

public class CustomerEditDialog {
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public enum Action {
        SAVE,
        DELETE
    }

    public record Result(Action action, Customer customer) {
    }

    public Optional<Result> show(Window owner, Customer customer) {
        Dialog<Result> dialog = new Dialog<>();
        dialog.initOwner(owner);
        dialog.setTitle("Editar cliente");
        dialog.setHeaderText("Atualize ou exclua o cliente");

        ButtonType saveButtonType = new ButtonType("Salvar", ButtonData.OK_DONE);
        ButtonType deleteButtonType = new ButtonType("Excluir", ButtonData.LEFT);
        dialog.getDialogPane().getButtonTypes().add(saveButtonType);
        dialog.getDialogPane().getButtonTypes().add(deleteButtonType);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);

        TextField nameInput = new TextField(customer.getName());
        TextField cpfInput = new TextField(customer.getCpf());
        DatePicker birthDateInput = new DatePicker();
        birthDateInput.setConverter(createDateConverter());
        setDateValue(birthDateInput, customer.getBirthDate());

        VBox form = new VBox(12);
        form.setPadding(new Insets(20));
        form.getChildren().addAll(
                createRow("Nome", nameInput),
                createRow("CPF", cpfInput),
                createRow("Data de nascimento", birthDateInput));
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

                    if (onlyDigits(cpfInput).length() != 11) {
                        showError(owner, "CPF deve ter 11 digitos.");
                        cpfInput.requestFocus();
                        event.consume();
                        return;
                    }

                    if (getDateText(birthDateInput).isBlank()) {
                        showError(owner, "Data de nascimento e obrigatoria.");
                        birthDateInput.requestFocus();
                        event.consume();
                        return;
                    }

                    try {
                        LocalDate.parse(getDateText(birthDateInput), DATE_FORMATTER);
                    } catch (DateTimeParseException exception) {
                        showError(owner, "Data de nascimento deve estar no formato dd/MM/aaaa.");
                        birthDateInput.requestFocus();
                        event.consume();
                    }
                });

        Button deleteButton = (Button) dialog.getDialogPane().lookupButton(deleteButtonType);
        deleteButton.addEventFilter(javafx.event.ActionEvent.ACTION, event -> {
            boolean confirmed = ConfirmationDialog.confirm(
                    owner,
                    "Excluir cliente",
                    "Tem certeza que deseja excluir este cliente?");

            if (!confirmed) {
                event.consume();
            }
        });

        dialog.setResultConverter(buttonType -> {
            if (buttonType == saveButtonType) {
                customer.updateData(
                        nameInput.getText().trim(),
                        onlyDigits(cpfInput),
                        getDateText(birthDateInput));
                return new Result(Action.SAVE, customer);
            }

            if (buttonType == deleteButtonType) {
                return new Result(Action.DELETE, customer);
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

    private HBox createRow(String label, DatePicker input) {
        input.setPrefWidth(240);

        Label labelControl = new Label(label);
        labelControl.setMinWidth(150);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox row = new HBox(12, labelControl, spacer, input);
        row.setAlignment(Pos.CENTER_LEFT);
        return row;
    }

    private String onlyDigits(TextField input) {
        return input.getText().replaceAll("\\D", "");
    }

    private void setDateValue(DatePicker input, String value) {
        if (value == null || value.isBlank()) {
            return;
        }

        try {
            input.setValue(LocalDate.parse(value, DATE_FORMATTER));
        } catch (DateTimeParseException exception) {
            input.getEditor().setText(value);
        }
    }

    private String getDateText(DatePicker input) {
        LocalDate value = input.getValue();
        return value == null ? input.getEditor().getText().trim() : DATE_FORMATTER.format(value);
    }

    private StringConverter<LocalDate> createDateConverter() {
        return new StringConverter<>() {
            @Override
            public String toString(LocalDate value) {
                return value == null ? "" : DATE_FORMATTER.format(value);
            }

            @Override
            public LocalDate fromString(String value) {
                if (value == null || value.isBlank()) {
                    return null;
                }

                return LocalDate.parse(value, DATE_FORMATTER);
            }
        };
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
