package com.pda.assetserver.repository.product.card;

import com.pda.assetserver.repository.product.Product;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;

@Getter
@Entity
@DiscriminatorValue("CARD")
public class CardProduct extends Product {
    @Column(name = "card_img", nullable = false)
    private String cardImage;
}
