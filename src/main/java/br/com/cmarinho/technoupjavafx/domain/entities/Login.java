package br.com.cmarinho.technoupjavafx.domain.entities;

import java.io.Serializable;
import java.util.Collection;

public class Login implements Serializable {
    private static final long serialVersionUID = 1L;
    private static int idCounter = 1;

    private int id;
    private String username;
    private String password;
    private String email;

    public Login(String username, String password, String email) {
        this.id = idCounter++;
        this.username = username;
        this.password = password;
        this.email = email;
    }

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public static void syncIdCounter(Collection<Login> logins) {
        int highestId = logins.stream()
                .mapToInt(login -> login.getId())
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
        Login other = (Login) obj;
        if (id != other.id)
            return false;
        return true;
    }
}
