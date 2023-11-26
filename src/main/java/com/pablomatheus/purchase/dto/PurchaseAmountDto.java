package com.pablomatheus.purchase.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PurchaseAmountDto {

    private String value;
    private String currency;
    private String country;
    private String exchangeRate;

}
