package com.pablomatheus.purchase.service;

import com.pablomatheus.purchase.dto.PurchaseDto;

public interface PurchaseService {

    PurchaseDto addPurchase(PurchaseDto purchaseDto);

    PurchaseDto getCurrencyConvertedPurchase(Long id, String currency, String country);

}
