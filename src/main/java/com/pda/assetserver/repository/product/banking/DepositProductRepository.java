package com.pda.assetserver.repository.product.banking;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DepositProductRepository extends JpaRepository<DepositProduct, Long> {

}
