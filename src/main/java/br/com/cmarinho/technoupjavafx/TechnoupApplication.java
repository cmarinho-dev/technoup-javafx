package br.com.cmarinho.technoupjavafx;

import br.com.cmarinho.technoupjavafx.ui.AtlantafxStylesheet;
import br.com.cmarinho.technoupjavafx.ui.Form;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

public class TechnoupApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        AtlantafxStylesheet.applyTheme(true);

        String[] inputs = { "Nome", "Endereço", "CPF" };

        Form form = new Form(inputs);
        form.setButtonAction(event -> {
        });
        VBox formBox = form.getVbox();

        Scene scene = new Scene(formBox, 1024, 768);
        stage.setTitle("TechnoUp!");
        stage.setScene(scene);
        stage.show();
    }


}