package br.com.cmarinho.technoupjavafx.presentation.customer;

import br.com.cmarinho.technoupjavafx.domain.entities.Customer;
import br.com.cmarinho.technoupjavafx.presentation.shared.crud.CrudTab;

public class CustomerTab extends CrudTab<Customer> {
    public CustomerTab() {
        super(CustomerCrudConfig.create());
    }
}
