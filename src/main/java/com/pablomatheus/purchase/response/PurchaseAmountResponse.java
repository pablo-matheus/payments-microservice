package com.pablomatheus.purchase.response;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PurchaseAmountResponse {

    private String value;
    private String currency;
    private String country;
    private String exchangeRate;

}
