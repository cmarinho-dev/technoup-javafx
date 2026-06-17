package br.com.cmarinho.technoupjavafx.presentation.customer;

import java.util.List;

import br.com.cmarinho.technoupjavafx.domain.entities.Customer;
import br.com.cmarinho.technoupjavafx.presentation.shared.crud.CrudConfig;
import br.com.cmarinho.technoupjavafx.presentation.shared.crud.CrudField;

public class CustomerCrudConfig {
    public static CrudConfig<Customer> create() {
        return new CrudConfig<>(
                "Clientes",
                "cliente",
                Customer.class,
                customer -> Integer.toString(customer.getId()),
                List.of(
                        CrudField.text("Nome", customer -> customer.getName()),
                        CrudField.cpf("CPF", customer -> customer.getCpf()),
                        CrudField.birthDate("Data de nascimento", customer -> customer.getBirthDate())),
                data -> new Customer(data.get("Nome"), data.get("CPF"), data.get("Data de nascimento")),
                (customer, data) -> customer.updateData(
                        data.get("Nome"),
                        data.get("CPF"),
                        data.get("Data de nascimento")),
                customers -> Customer.syncIdCounter(customers));
    }
}
