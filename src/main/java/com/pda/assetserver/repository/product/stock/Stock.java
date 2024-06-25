package com.pda.assetserver.repository.product.stock;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;

@Entity
@Table(name = "Stock")
@Getter
public class Stock {
    @Id
    @Column(name = "code")
    private String code;

    @Column(name = "name")
    private String name;

    @Column(name = "dart_code")
    private String dartCode;

    @Column(name = "price")
    private double price;

    @Column(name = "stock_type")
    private String stockType;

    @Column(name = "eng_name")
    private String englishName;
}
