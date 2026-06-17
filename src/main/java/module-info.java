module br.com.cmarinho.technoupjavafx {
    requires javafx.controls;
    requires atlantafx.base;

    exports br.com.cmarinho.technoupjavafx;
    exports br.com.cmarinho.technoupjavafx.domain.entities;
    exports br.com.cmarinho.technoupjavafx.persistence;
    exports br.com.cmarinho.technoupjavafx.presentation.administrator;
    exports br.com.cmarinho.technoupjavafx.presentation.customer;
    exports br.com.cmarinho.technoupjavafx.presentation.home;
    exports br.com.cmarinho.technoupjavafx.presentation.login;
    exports br.com.cmarinho.technoupjavafx.presentation.offer;
    exports br.com.cmarinho.technoupjavafx.presentation.product;
    exports br.com.cmarinho.technoupjavafx.presentation.shared.crud;
    exports br.com.cmarinho.technoupjavafx.presentation.store;
}
