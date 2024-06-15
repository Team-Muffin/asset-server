package com.pda.assetserver.repository.asset;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@Entity
@Table(name = "CardAsset")
@DiscriminatorValue("CARD")
public class CardAsset extends Asset {
    @Column(name = "cardNumber", unique = true, updatable = false)
    private String cardNumber;

}
