package br.com.cmarinho.technoupjavafx.presentation.shared.crud;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import br.com.cmarinho.technoupjavafx.presentation.shared.dialogs.ConfirmationDialog;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Window;

public class CrudFormDialog<T extends Serializable> {
    public enum Action {
        SAVE,
        DELETE
    }

    public record Result<T>(Action action, T entity) {
    }

    private final CrudConfig<T> config;

    public CrudFormDialog(CrudConfig<T> config) {
        this.config = config;
    }

    public Optional<Result<T>> show(Window owner, T entity) {
        boolean editing = entity != null;

        Dialog<Result<T>> dialog = new Dialog<>();
        dialog.initOwner(owner);
        dialog.setTitle(editing ? "Editar " + config.singularTitle() : "Novo " + config.singularTitle());
        dialog.setHeaderText(editing ? "Atualize ou exclua o registro" : "Cadastre um novo registro");

        ButtonType saveButtonType = new ButtonType("Salvar", ButtonData.OK_DONE);
        ButtonType deleteButtonType = new ButtonType("Excluir", ButtonData.LEFT);
        dialog.getDialogPane().getButtonTypes().add(saveButtonType);

        if (editing) {
            dialog.getDialogPane().getButtonTypes().add(deleteButtonType);
        }

        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);

        List<CrudField.FormInput> inputs = createInputs(entity);
        dialog.getDialogPane().setContent(createForm(inputs));

        Node saveButton = dialog.getDialogPane().lookupButton(saveButtonType);
        saveButton.addEventFilter(ActionEvent.ACTION, event -> validateBeforeSave(owner, inputs, event));

        if (editing) {
            Button deleteButton = (Button) dialog.getDialogPane().lookupButton(deleteButtonType);
            deleteButton.addEventFilter(ActionEvent.ACTION, event -> {
                boolean confirmed = ConfirmationDialog.confirm(
                        owner,
                        "Excluir " + config.singularTitle(),
                        "Tem certeza que deseja excluir este registro?");

                if (!confirmed) {
                    event.consume();
                }
            });
        }

        dialog.setResultConverter(buttonType -> {
            if (buttonType == saveButtonType) {
                CrudFormData formData = readValues(inputs);

                if (editing) {
                    config.updateEntity().accept(entity, formData);
                    return new Result<>(Action.SAVE, entity);
                }

                return new Result<>(Action.SAVE, config.createEntity().apply(formData));
            }

            if (buttonType == deleteButtonType) {
                return new Result<>(Action.DELETE, entity);
            }

            return null;
        });

        return dialog.showAndWait();
    }

    private List<CrudField.FormInput> createInputs(T entity) {
        return config.fields().stream()
                .map(field -> field.createInput(entity))
                .toList();
    }

    private VBox createForm(List<CrudField.FormInput> inputs) {
        VBox form = new VBox(12);
        form.setPadding(new Insets(20));

        for (CrudField.FormInput input : inputs) {
            Label label = new Label(input.label());
            label.setMinWidth(140);

            HBox row = new HBox(12);
            row.setAlignment(Pos.CENTER_LEFT);
            row.getChildren().addAll(label, createSpacer(), input.control());
            form.getChildren().add(row);
        }

        return form;
    }

    private Region createSpacer() {
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        return spacer;
    }

    private void validateBeforeSave(Window owner, List<CrudField.FormInput> inputs, ActionEvent event) {
        for (CrudField.FormInput input : inputs) {
            String error = input.validate();

            if (error != null) {
                showError(owner, error);
                input.requestFocus();
                event.consume();
                return;
            }
        }
    }

    private CrudFormData readValues(List<CrudField.FormInput> inputs) {
        Map<String, String> values = new LinkedHashMap<>();

        for (CrudField.FormInput input : inputs) {
            values.put(input.label(), input.value());
        }

        return new CrudFormData(values);
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
