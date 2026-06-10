module br.com.cmarinho.technoupjavafx {
    requires javafx.controls;
    requires javafx.fxml;
    requires atlantafx.base;
    requires javafx.base;

    opens br.com.cmarinho.technoupjavafx to javafx.fxml;

    exports br.com.cmarinho.technoupjavafx;
}