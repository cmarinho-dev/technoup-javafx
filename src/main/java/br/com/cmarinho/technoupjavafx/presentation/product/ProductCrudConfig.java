package br.com.cmarinho.technoupjavafx.presentation.product;

import java.util.List;

import br.com.cmarinho.technoupjavafx.domain.entities.Product;
import br.com.cmarinho.technoupjavafx.presentation.shared.crud.CrudConfig;
import br.com.cmarinho.technoupjavafx.presentation.shared.crud.CrudField;

public class ProductCrudConfig {
    public static CrudConfig<Product> create() {
        return new CrudConfig<>(
                "Produtos",
                "produto",
                Product.class,
                product -> Integer.toString(product.getId()),
                List.of(
                        CrudField.text("Nome", product -> product.getName()),
                        CrudField.decimal("Preco", product -> Double.toString(product.getPrice())),
                        CrudField.integer("Estoque", product -> Integer.toString(product.getStockQuantity()))),
                data -> new Product(data.get("Nome"), data.getDouble("Preco"), data.getInt("Estoque")),
                (product, data) -> {
                    product.setName(data.get("Nome"));
                    product.setPrice(data.getDouble("Preco"));
                    product.setStockQuantity(data.getInt("Estoque"));
                },
                products -> Product.syncIdCounter(products));
    }
}
