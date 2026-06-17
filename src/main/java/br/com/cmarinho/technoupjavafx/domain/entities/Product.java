package br.com.cmarinho.technoupjavafx.domain.entities;

import java.io.Serializable;
import java.util.Collection;

public class Product implements Serializable {
    private static final long serialVersionUID = 1L;
    private static int idCounter = 1;

    private int id;
    private String name;
    private double price;
    private int stockQuantity;
    
    public Product(String name, double price, int stockQuantity) {
        this.id = idCounter++;
        this.name = name;
        this.price = price;
        this.stockQuantity = stockQuantity;
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

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getStockQuantity() {
        return stockQuantity;
    }

    public void setStockQuantity(int stockQuantity) {
        this.stockQuantity = stockQuantity;
    }

    public static void syncIdCounter(Collection<Product> products) {
        int highestId = products.stream()
                .mapToInt(product -> product.getId())
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
        Product other = (Product) obj;
        if (id != other.id)
            return false;
        return true;
    }
}
