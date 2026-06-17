package br.com.cmarinho.technoupjavafx.presentation.home;

import br.com.cmarinho.technoupjavafx.presentation.administrator.AdministratorTab;
import br.com.cmarinho.technoupjavafx.presentation.customer.CustomerTab;
import br.com.cmarinho.technoupjavafx.presentation.login.LoginTab;
import br.com.cmarinho.technoupjavafx.presentation.offer.OfferTab;
import br.com.cmarinho.technoupjavafx.presentation.product.ProductTab;
import br.com.cmarinho.technoupjavafx.presentation.store.StoreTab;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

public class HomeView extends TabPane {
    public HomeView() {
        setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        getTabs().addAll(
                new Tab("Clientes", new CustomerTab()),
                new Tab("Produtos", new ProductTab()),
                new Tab("Lojas", new StoreTab()),
                new Tab("Ofertas", new OfferTab()),
                new Tab("Logins", new LoginTab()),
                new Tab("Administradores", new AdministratorTab()));
    }
}
