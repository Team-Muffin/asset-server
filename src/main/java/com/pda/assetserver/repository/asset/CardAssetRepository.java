package com.pda.assetserver.repository.asset;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CardAssetRepository extends JpaRepository<CardAsset, Long> {
    boolean existsByCardNumber(String cardNumber);
}
