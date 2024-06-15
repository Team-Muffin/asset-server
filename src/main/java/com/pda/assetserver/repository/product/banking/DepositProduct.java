package com.pda.assetserver.repository.product.banking;

import com.pda.assetserver.repository.product.Product;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("DEPOSIT")
public class DepositProduct extends Product {
}
