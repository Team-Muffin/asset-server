package com.pda.assetserver.repository.asset;

import com.pda.assetserver.utils.enums.AccountType;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.util.ArrayList;
import java.util.List;

@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@Entity
@Table(name = "BankAccount")
@DiscriminatorValue("ACC")
@DynamicInsert
@DynamicUpdate
public class BankAccount extends Asset {
    @Column(name = "account_type", nullable = false, updatable = false)
    private AccountType accountType;

    @Column(name = "account_number", unique = true, updatable = false)
    private String accountNumber;

    @Column(name = "cash", columnDefinition = "bigint unsigned default 0 not null")
    private Long cash;

    @OneToMany(mappedBy = "account")
    List<StockAsset> stockAssets = new ArrayList<>();

    @Column(name = "return_rate")
    private Double returnRate;

    public void witdraw(long amount) {
        if (cash-amount < 0) throw new RuntimeException("Invalid amount");
        this.cash -= amount;
    }

    public void deposit(long amount) {
        this.cash += amount;
    }
}
