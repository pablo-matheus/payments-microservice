package com.pablomatheus.purchase.client;

import com.pablomatheus.purchase.client.response.ExchangeRateResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "${client.united-states-treasury.name}", url = "${client.united-states-treasury.base-url}")
public interface UnitedStatesTreasuryClient {

    @GetMapping("/v1/accounting/od/rates_of_exchange")
    ExchangeRateResponse getExchangeRate(@RequestParam(value = "filter", required = false) String filter,
                                         @RequestParam(value = "sort", required = false) String sort);

}
