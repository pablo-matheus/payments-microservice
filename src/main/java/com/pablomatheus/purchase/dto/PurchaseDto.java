package com.pablomatheus.purchase.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class PurchaseDto {

    private Long id;
    private String description;
    private LocalDateTime transactionDate;
    private PurchaseAmountDto originalAmount;
    private PurchaseAmountDto convertedAmount;

}
