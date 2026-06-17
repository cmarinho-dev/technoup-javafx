package br.com.cmarinho.technoupjavafx.presentation.product;

import br.com.cmarinho.technoupjavafx.domain.entities.Product;
import br.com.cmarinho.technoupjavafx.presentation.shared.crud.CrudTab;

public class ProductTab extends CrudTab<Product> {
    public ProductTab() {
        super(ProductCrudConfig.create());
    }
}
