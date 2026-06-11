package br.com.cmarinho.technoupjavafx;

import br.com.cmarinho.technoupjavafx.ui.AtlantafxStylesheet;
import br.com.cmarinho.technoupjavafx.ui.Form;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

public class TechnoupApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        AtlantafxStylesheet.applyTheme(true);

        String[] inputs = { "Nome", "Endereço", "CPF" };

        Form form = new Form(inputs);
        VBox formBox = form.getVbox();

        form.setButtonAction(event -> {
            getDatas(formBox);
        });

        Scene scene = new Scene(formBox, 1024, 768);
        stage.setTitle("TechnoUp!");
        stage.setScene(scene);
        stage.show();
    }

    private void getDatas(VBox formVBox) {
        HBox hbox1 = (HBox) formVBox.getChildren().get(0);
        TextField hbox1Input = (TextField) hbox1.getChildren().get(2);
        System.out.println(hbox1Input.getText());
    }
}