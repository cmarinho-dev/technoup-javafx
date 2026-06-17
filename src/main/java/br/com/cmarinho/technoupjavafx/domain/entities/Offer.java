package br.com.cmarinho.technoupjavafx.domain.entities;

import java.io.Serializable;
import java.util.Collection;

public class Offer implements Serializable {
    private static final long serialVersionUID = 1L;
    private static int idCounter = 1;

    private int id;
    private String title;
    private String description;
    private double discountPercentage;

    public Offer(String title, String description, double discountPercentage) {
        this.id = idCounter++;
        this.title = title;
        this.description = description;
        this.discountPercentage = discountPercentage;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getDiscountPercentage() {
        return discountPercentage;
    }

    public void setDiscountPercentage(double discountPercentage) {
        this.discountPercentage = discountPercentage;
    }

    public static void syncIdCounter(Collection<Offer> offers) {
        int highestId = offers.stream()
                .mapToInt(offer -> offer.getId())
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
        Offer other = (Offer) obj;
        if (id != other.id)
            return false;
        return true;
    }
}
