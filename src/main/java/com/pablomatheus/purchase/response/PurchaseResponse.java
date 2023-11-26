package com.pablomatheus.purchase.response;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class PurchaseResponse {

    private Long id;
    private String description;
    private LocalDateTime transactionDate;
    private PurchaseAmountResponse originalAmount;
    private PurchaseAmountResponse convertedAmount;

}
