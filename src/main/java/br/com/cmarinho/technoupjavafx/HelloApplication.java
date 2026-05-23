package br.com.cmarinho.technoupjavafx;

import atlantafx.base.theme.PrimerDark;
import atlantafx.base.theme.PrimerLight;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        applyStylesheet(false);

        Text nomeLabel = new Text("Insira seu nome: ");
        TextField nomeInput = new TextField();
        HBox nomeBox = new HBox(
                nomeLabel,
                nomeInput
        );
        nomeBox.setAlignment(Pos.CENTER);
        nomeBox.setPadding(new Insets(10, 0, 10, 0));

        Text idadeLabel = new Text("Insira sua idade: ");
        TextField idadeInput = new TextField();
        HBox idadeBox = new HBox(
                idadeLabel,
                idadeInput
        );
        idadeBox.setAlignment(Pos.CENTER);
        idadeBox.setPadding(new Insets(10, 0, 10, 0));

        Button botaoSubmit = new Button("Enviar");
        HBox botaoBox = new HBox(
                botaoSubmit
        );
        botaoBox.setAlignment(Pos.CENTER_RIGHT);
        botaoBox.setPadding(new Insets(10, 0, 10, 0));

        botaoSubmit.setOnMouseClicked(e -> {
            var data = nomeInput.getText() + "\n";
            data += idadeInput.getText() + "\n";
            System.out.println(data);
            applyStylesheet(true);
        });

        VBox box = new VBox(
                nomeBox,
                idadeBox,
                botaoBox
        );
        box.setPadding(new Insets(6, 24, 6, 24));

        Scene scene = new Scene(box, 320, 240);
        stage.setTitle("TechnoUp!");
        stage.setScene(scene);
        stage.show();
    }

    private void applyStylesheet(boolean requireDarkMode) {
        if (requireDarkMode) {
            Application.setUserAgentStylesheet(new PrimerDark().getUserAgentStylesheet());
        } else {
            Application.setUserAgentStylesheet(new PrimerLight().getUserAgentStylesheet());
        }
    }
}