package br.com.cmarinho.technoupjavafx.presentation.store;

import java.util.List;

import br.com.cmarinho.technoupjavafx.domain.entities.Store;
import br.com.cmarinho.technoupjavafx.presentation.shared.crud.CrudConfig;
import br.com.cmarinho.technoupjavafx.presentation.shared.crud.CrudField;

public class StoreCrudConfig {
    public static CrudConfig<Store> create() {
        return new CrudConfig<>(
                "Lojas",
                "loja",
                Store.class,
                store -> Integer.toString(store.getId()),
                List.of(
                        CrudField.text("Nome", store -> store.getName()),
                        CrudField.text("Endereco", store -> store.getAddress()),
                        CrudField.text("Telefone", store -> store.getPhone())),
                data -> new Store(data.get("Nome"), data.get("Endereco"), data.get("Telefone")),
                (store, data) -> {
                    store.setName(data.get("Nome"));
                    store.setAddress(data.get("Endereco"));
                    store.setPhone(data.get("Telefone"));
                },
                stores -> Store.syncIdCounter(stores));
    }
}
