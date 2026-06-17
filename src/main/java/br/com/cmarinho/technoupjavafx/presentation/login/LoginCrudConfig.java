package br.com.cmarinho.technoupjavafx.presentation.login;

import java.util.List;

import br.com.cmarinho.technoupjavafx.domain.entities.Login;
import br.com.cmarinho.technoupjavafx.presentation.shared.crud.CrudConfig;
import br.com.cmarinho.technoupjavafx.presentation.shared.crud.CrudField;

public class LoginCrudConfig {
    public static CrudConfig<Login> create() {
        return new CrudConfig<>(
                "Logins",
                "login",
                Login.class,
                login -> Integer.toString(login.getId()),
                List.of(
                        CrudField.text("Usuario", login -> login.getUsername()),
                        CrudField.password("Senha", login -> login.getPassword()),
                        CrudField.text("Email", login -> login.getEmail())),
                data -> new Login(data.get("Usuario"), data.get("Senha"), data.get("Email")),
                (login, data) -> {
                    login.setUsername(data.get("Usuario"));
                    login.setPassword(data.get("Senha"));
                    login.setEmail(data.get("Email"));
                },
                logins -> Login.syncIdCounter(logins));
    }
}
