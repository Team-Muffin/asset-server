package com.pda.assetserver.repository.asset;

import com.pda.assetserver.repository.product.stock.Stock;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Table
@Entity(name = "StockAsset")
public class StockAsset {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "account_id", nullable = false)
    private BankAccount account;

    @ManyToOne(cascade = {CascadeType.MERGE})
    @JoinColumn(name = "code", nullable = false)
    private Stock stock;

    @Column(name = "quantity", columnDefinition = "int unsigned not null default 1")
    private int quantity;
}
