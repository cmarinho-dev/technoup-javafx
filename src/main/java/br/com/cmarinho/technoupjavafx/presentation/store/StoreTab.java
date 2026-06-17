package br.com.cmarinho.technoupjavafx.presentation.store;

import br.com.cmarinho.technoupjavafx.domain.entities.Store;
import br.com.cmarinho.technoupjavafx.presentation.shared.crud.CrudTab;

public class StoreTab extends CrudTab<Store> {
    public StoreTab() {
        super(StoreCrudConfig.create());
    }
}
