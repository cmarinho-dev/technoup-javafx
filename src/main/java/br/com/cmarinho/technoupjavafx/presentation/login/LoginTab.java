package br.com.cmarinho.technoupjavafx.presentation.login;

import br.com.cmarinho.technoupjavafx.domain.entities.Login;
import br.com.cmarinho.technoupjavafx.presentation.shared.crud.CrudTab;

public class LoginTab extends CrudTab<Login> {
    public LoginTab() {
        super(LoginCrudConfig.create());
    }
}
