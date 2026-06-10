package br.com.cmarinho.technoupjavafx.ui;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

public class Form {
    VBox vbox;
    Button button;
    String[] inputs;

    public Form(String[] inputs) {
        vbox = new VBox();
        vbox.setPadding(new Insets(
                6, 24, 6, 24));

        this.inputs = inputs;
        insertInputsAndLabels(vbox, inputs);

        button = new Button("Cadastrar");
        button.setPrefSize(400, 0);

        HBox buttonBox = new HBox(button);
        buttonBox.setMaxWidth(400);
        buttonBox.setPadding(new Insets(
                19.6,
                12,
                12,
                12));
                
        vbox.getChildren().add(buttonBox);
    }

    private void insertInputsAndLabels(VBox vbox, String[] inputs) {
        for (String input : inputs) {
            HBox hbox = new HBox(
                    new Label(input),
                    createSpacer(),
                    new TextField());
            hbox.setAlignment(Pos.CENTER_LEFT);
            hbox.setPadding(new Insets(12));
            hbox.setMaxWidth(400);
            vbox.getChildren().add(hbox);
        }
    }

    private Region createSpacer() {
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        return spacer;
    }

    public void setButtonAction(EventHandler<ActionEvent> action) {
        button.setOnAction(action);
    }

    public VBox getVbox() {
        return vbox;
    }
}
