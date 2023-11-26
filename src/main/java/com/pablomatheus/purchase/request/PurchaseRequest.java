package com.pablomatheus.purchase.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class PurchaseRequest {

    @Size(max = 50)
    @NotBlank
    private String description;

    @NotNull
    private LocalDateTime transactionDate;

    @NotNull
    private BigDecimal amount;

    @Size(max = 50)
    private String currency = "Dollar";

    @Size(max = 50)
    private String country = "United States";

}
