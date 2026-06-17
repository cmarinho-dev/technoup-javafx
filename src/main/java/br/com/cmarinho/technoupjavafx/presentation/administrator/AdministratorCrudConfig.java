package br.com.cmarinho.technoupjavafx.presentation.administrator;

import java.util.List;

import br.com.cmarinho.technoupjavafx.domain.entities.Administrator;
import br.com.cmarinho.technoupjavafx.presentation.shared.crud.CrudConfig;
import br.com.cmarinho.technoupjavafx.presentation.shared.crud.CrudField;

public class AdministratorCrudConfig {
    public static CrudConfig<Administrator> create() {
        return new CrudConfig<>(
                "Administradores",
                "administrador",
                Administrator.class,
                administrator -> Integer.toString(administrator.getId()),
                List.of(
                        CrudField.text("Nome", administrator -> administrator.getName()),
                        CrudField.text("Email", administrator -> administrator.getEmail()),
                        CrudField.text("Departamento", administrator -> administrator.getDepartment())),
                data -> new Administrator(data.get("Nome"), data.get("Email"), data.get("Departamento")),
                (administrator, data) -> {
                    administrator.setName(data.get("Nome"));
                    administrator.setEmail(data.get("Email"));
                    administrator.setDepartment(data.get("Departamento"));
                },
                administrators -> Administrator.syncIdCounter(administrators));
    }
}
