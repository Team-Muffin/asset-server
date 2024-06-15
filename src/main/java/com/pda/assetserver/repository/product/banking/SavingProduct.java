package com.pda.assetserver.repository.product.banking;

import com.pda.assetserver.repository.product.Product;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("SAVING")
public class SavingProduct extends Product {
}
