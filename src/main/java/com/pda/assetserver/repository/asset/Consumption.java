package com.pda.assetserver.repository.asset;

import com.pda.assetserver.utils.enums.ConsumptionType;
import jakarta.persistence.Column;
import jakarta.persistence.ConstraintMode;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "Consumption")
public class Consumption {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "account_id", nullable = false, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private BankAccount account;

    @Column(name = "date_time", nullable = false, updatable = false)
    private LocalDateTime dateTime;

    @Column(name = "purchase_place", nullable = false)
    private String purchasePlace;

    @Column(name = "consumption_type", nullable = false)
    private ConsumptionType consumptionType;

    @Column(name = "amount", nullable = false)
    private Long amount;
}
