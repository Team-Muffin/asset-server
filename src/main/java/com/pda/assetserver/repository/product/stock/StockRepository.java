package com.pda.assetserver.repository.product.stock;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StockRepository <T extends Stock> extends JpaRepository<T, String> {
    List<Stock> findAllByStockType(String stockType);

}
