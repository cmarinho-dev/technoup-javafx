package br.com.cmarinho.technoupjavafx.domain.entities;

import java.io.Serializable;
import java.util.Collection;

public class Customer implements Serializable {
    private static final long serialVersionUID = 1L;
    private static int idCounter = 1;

    private int id;
    private String name;
    private String cpf;
    private String birthDate;

    public Customer(String name, String cpf, String birthDate) {
        this.id = idCounter++;
        this.name = name;
        this.cpf = cpf;
        this.birthDate = birthDate;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public void updateData(String name, String cpf, String birthDate) {
        this.name = name;
        this.cpf = cpf;
        this.birthDate = birthDate;
    }

    public static void syncIdCounter(Collection<Customer> customers) {
        int highestId = customers.stream()
                .mapToInt(customer -> customer.getId())
                .max()
                .orElse(0);

        idCounter = Math.max(idCounter, highestId + 1);
    }

    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + id;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Customer other = (Customer) obj;
        if (id != other.id)
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "Customer [id=" + id + ", name=" + name + ", cpf=" + cpf + ", birthDate=" + birthDate + "]";
    }

    

    
}
