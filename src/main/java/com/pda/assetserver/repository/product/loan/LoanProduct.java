package com.pda.assetserver.repository.product.loan;

import com.pda.assetserver.repository.product.Product;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("LOAN")
public class LoanProduct extends Product {
}
