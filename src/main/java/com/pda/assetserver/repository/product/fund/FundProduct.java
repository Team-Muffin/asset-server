package com.pda.assetserver.repository.product.fund;

import com.pda.assetserver.repository.product.Product;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("FUND")
public class FundProduct extends Product {
    @Column(name = "price")
    private Double price;
}
