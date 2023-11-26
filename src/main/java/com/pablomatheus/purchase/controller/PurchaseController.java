package com.pablomatheus.purchase.controller;

import com.pablomatheus.purchase.dto.PurchaseDto;
import com.pablomatheus.purchase.mapper.PurchaseMapper;
import com.pablomatheus.purchase.request.PurchaseRequest;
import com.pablomatheus.purchase.response.PurchaseResponse;
import com.pablomatheus.purchase.service.PurchaseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/purchases")
public class PurchaseController {

    private final PurchaseService purchaseService;
    private final PurchaseMapper purchaseMapper;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PurchaseResponse addPurchase(@Valid @RequestBody PurchaseRequest purchaseRequest) {
        PurchaseDto purchaseDto = purchaseService.addPurchase(purchaseMapper.toDto(purchaseRequest));
        return purchaseMapper.toResponse(purchaseDto);
    }

    @GetMapping("/{id}/exchange-rates/conversion")
    public PurchaseResponse getCurrencyConvertedPurchase(@PathVariable Long id,
                                                         @RequestParam String currency,
                                                         @RequestParam String country) {

        PurchaseDto purchaseDto = purchaseService.getCurrencyConvertedPurchase(id, currency, country);
        return purchaseMapper.toResponse(purchaseDto);
    }

}
