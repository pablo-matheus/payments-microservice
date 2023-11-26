package com.pablomatheus.purchase.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SourceType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Table(name = "purchases")
@Entity
public class PurchaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "purchases_sequence_generator")
    @SequenceGenerator(name = "purchases_sequence_generator", sequenceName = "PURCHASES_SEQ", allocationSize = 1)
    private Long id;

    private String description;

    private BigDecimal amount;

    private String currency;

    private String country;

    private LocalDateTime transactionDate;

    @CreationTimestamp(source = SourceType.DB)
    private LocalDateTime creationDate;

}
