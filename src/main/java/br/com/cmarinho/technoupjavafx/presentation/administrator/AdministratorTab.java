package br.com.cmarinho.technoupjavafx.presentation.administrator;

import br.com.cmarinho.technoupjavafx.domain.entities.Administrator;
import br.com.cmarinho.technoupjavafx.presentation.shared.crud.CrudTab;

public class AdministratorTab extends CrudTab<Administrator> {
    public AdministratorTab() {
        super(AdministratorCrudConfig.create());
    }
}
