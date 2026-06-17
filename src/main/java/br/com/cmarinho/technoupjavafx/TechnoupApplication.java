package br.com.cmarinho.technoupjavafx;

import br.com.cmarinho.technoupjavafx.presentation.home.HomeView;
import br.com.cmarinho.technoupjavafx.presentation.shared.css.AtlantafxStylesheet;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class TechnoupApplication extends Application {
    @Override
    public void start(Stage stage) {
        AtlantafxStylesheet.applyTheme(true);

        Scene scene = new Scene(new HomeView(), 1024, 768);
        stage.setTitle("TechnoUp!");
        stage.setScene(scene);
        stage.show();
    }
}
