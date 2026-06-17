package br.com.cmarinho.technoupjavafx.presentation.offer;

import br.com.cmarinho.technoupjavafx.domain.entities.Offer;
import br.com.cmarinho.technoupjavafx.presentation.shared.crud.CrudTab;

public class OfferTab extends CrudTab<Offer> {
    public OfferTab() {
        super(OfferCrudConfig.create());
    }
}
