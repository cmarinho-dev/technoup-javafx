package br.com.cmarinho.technoupjavafx.domain.entities;

import java.io.Serializable;
import java.util.Collection;

public class Administrator implements Serializable {
    private static final long serialVersionUID = 1L;
    private static int idCounter = 1;

    private int id;
    private String name;
    private String email;
    private String department;

    public Administrator(String name, String email, String department) {
        this.id = idCounter++;
        this.name = name;
        this.email = email;
        this.department = department;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public static void syncIdCounter(Collection<Administrator> administrators) {
        int highestId = administrators.stream()
                .mapToInt(administrator -> administrator.getId())
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
        Administrator other = (Administrator) obj;
        if (id != other.id)
            return false;
        return true;
    }

    
}
