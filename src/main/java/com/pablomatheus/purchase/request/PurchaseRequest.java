package com.pablomatheus.purchase.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class PurchaseRequest {

    @NotBlank
    private String description;

    @NotNull
    private LocalDateTime transactionDate;

    @NotNull
    private BigDecimal amount;

    private String currency = "Dollar";

    private String country = "United States";

}
