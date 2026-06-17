package br.com.cmarinho.technoupjavafx.presentation.offer;

import java.util.List;

import br.com.cmarinho.technoupjavafx.domain.entities.Offer;
import br.com.cmarinho.technoupjavafx.presentation.shared.crud.CrudConfig;
import br.com.cmarinho.technoupjavafx.presentation.shared.crud.CrudField;

public class OfferCrudConfig {
    public static CrudConfig<Offer> create() {
        return new CrudConfig<>(
                "Ofertas",
                "oferta",
                Offer.class,
                offer -> Integer.toString(offer.getId()),
                List.of(
                        CrudField.text("Titulo", offer -> offer.getTitle()),
                        CrudField.text("Descricao", offer -> offer.getDescription()),
                        CrudField.decimal("Desconto (%)", offer -> Double.toString(offer.getDiscountPercentage()))),
                data -> new Offer(
                        data.get("Titulo"),
                        data.get("Descricao"),
                        data.getDouble("Desconto (%)")),
                (offer, data) -> {
                    offer.setTitle(data.get("Titulo"));
                    offer.setDescription(data.get("Descricao"));
                    offer.setDiscountPercentage(data.getDouble("Desconto (%)"));
                },
                offers -> Offer.syncIdCounter(offers));
    }
}
