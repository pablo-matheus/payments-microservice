package com.pablomatheus.purchase.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pablomatheus.purchase.dto.PurchaseDto;
import com.pablomatheus.purchase.mapper.PurchaseMapper;
import com.pablomatheus.purchase.request.PurchaseRequest;
import com.pablomatheus.purchase.response.PurchaseResponse;
import com.pablomatheus.purchase.service.PurchaseService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PurchaseController.class)
class PurchaseControllerTest {

    private static final String V1_PURCHASES = "/v1/purchases";
    private static final String V1_PURCHASES_ID_EXCHANGERATES_CONVERSION = V1_PURCHASES + "/{id}/exchange-rates/conversion";

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PurchaseMapper purchaseMapper;

    @MockBean
    private PurchaseService purchaseService;

    @Test
    void givenValidRequestWhenAddPurchaseThenStatus201CreatedAndReturnSavedInformation() throws Exception {
        PurchaseRequest purchaseRequest = new PurchaseRequest();
        purchaseRequest.setAmount(new BigDecimal("11.25"));
        purchaseRequest.setDescription("Test");
        purchaseRequest.setTransactionDate(LocalDateTime.now());

        PurchaseDto purchaseDto = new PurchaseDto();

        PurchaseResponse purchaseResponse = new PurchaseResponse();
        purchaseResponse.setId(1L);

        given(purchaseMapper.toDto(Mockito.any(PurchaseRequest.class))).willReturn(purchaseDto);
        given(purchaseService.addPurchase(purchaseDto)).willReturn(purchaseDto);
        given(purchaseMapper.toResponse(purchaseDto)).willReturn(purchaseResponse);

        RequestBuilder request = MockMvcRequestBuilders.post(V1_PURCHASES)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(purchaseRequest));

        mvc.perform(request)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L));

        verify(purchaseMapper, times(1)).toDto(Mockito.any(PurchaseRequest.class));
        verify(purchaseService, times(1)).addPurchase(purchaseDto);
        verify(purchaseMapper, times(1)).toResponse(purchaseDto);

        verifyNoMoreInteractions(purchaseMapper);
        verifyNoMoreInteractions(purchaseService);
    }

    @Test
    void givenNullAmountWhenAddPurchaseThenStatus400BadRequest() throws Exception {
        PurchaseRequest purchaseRequest = new PurchaseRequest();
        purchaseRequest.setDescription("Test");
        purchaseRequest.setTransactionDate(LocalDateTime.now());

        RequestBuilder request = MockMvcRequestBuilders.post(V1_PURCHASES)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(purchaseRequest));

        mvc.perform(request)
                .andExpect(status().isBadRequest());

        verifyNoInteractions(purchaseMapper);
        verifyNoInteractions(purchaseService);
    }

    @Test
    void givenNullDescriptionWhenAddPurchaseThenStatus400BadRequest() throws Exception {
        PurchaseRequest purchaseRequest = new PurchaseRequest();
        purchaseRequest.setAmount(new BigDecimal("11.25"));
        purchaseRequest.setTransactionDate(LocalDateTime.now());

        RequestBuilder request = MockMvcRequestBuilders.post(V1_PURCHASES)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(purchaseRequest));

        mvc.perform(request)
                .andExpect(status().isBadRequest());

        verifyNoInteractions(purchaseMapper);
        verifyNoInteractions(purchaseService);
    }

    @Test
    void givenOutOfLimitDescriptionWhenAddPurchaseThenStatus400BadRequest() throws Exception {
        PurchaseRequest purchaseRequest = new PurchaseRequest();
        purchaseRequest.setAmount(new BigDecimal("11.25"));
        purchaseRequest.setDescription("Testttttttttttttttttttttttttttttttttttttttttttttttt");
        purchaseRequest.setTransactionDate(LocalDateTime.now());

        RequestBuilder request = MockMvcRequestBuilders.post(V1_PURCHASES)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(purchaseRequest));

        mvc.perform(request)
                .andExpect(status().isBadRequest());

        verifyNoInteractions(purchaseMapper);
        verifyNoInteractions(purchaseService);
    }

    @Test
    void givenNullTransactionDateWhenAddPurchaseThenStatus400BadRequest() throws Exception {
        PurchaseRequest purchaseRequest = new PurchaseRequest();
        purchaseRequest.setAmount(new BigDecimal("11.25"));
        purchaseRequest.setDescription("Test");

        RequestBuilder request = MockMvcRequestBuilders.post(V1_PURCHASES)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(purchaseRequest));

        mvc.perform(request)
                .andExpect(status().isBadRequest());

        verifyNoInteractions(purchaseMapper);
        verifyNoInteractions(purchaseService);
    }

    @Test
    void givenOutOfLimitCurrencyWhenAddPurchaseThenStatus400BadRequest() throws Exception {
        PurchaseRequest purchaseRequest = new PurchaseRequest();
        purchaseRequest.setAmount(new BigDecimal("11.25"));
        purchaseRequest.setDescription("Test");
        purchaseRequest.setTransactionDate(LocalDateTime.now());
        purchaseRequest.setCurrency("Testttttttttttttttttttttttttttttttttttttttttttttttt");

        RequestBuilder request = MockMvcRequestBuilders.post(V1_PURCHASES)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(purchaseRequest));

        mvc.perform(request)
                .andExpect(status().isBadRequest());

        verifyNoInteractions(purchaseMapper);
        verifyNoInteractions(purchaseService);
    }

    @Test
    void givenOutOfLimitCountryWhenAddPurchaseThenStatus400BadRequest() throws Exception {
        PurchaseRequest purchaseRequest = new PurchaseRequest();
        purchaseRequest.setAmount(new BigDecimal("11.25"));
        purchaseRequest.setDescription("Test");
        purchaseRequest.setTransactionDate(LocalDateTime.now());
        purchaseRequest.setCountry("Testttttttttttttttttttttttttttttttttttttttttttttttt");

        RequestBuilder request = MockMvcRequestBuilders.post(V1_PURCHASES)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(purchaseRequest));

        mvc.perform(request)
                .andExpect(status().isBadRequest());

        verifyNoInteractions(purchaseMapper);
        verifyNoInteractions(purchaseService);
    }

    @Test
    void givenValidRequestWhenGetCurrencyConvertedPurchaseThenStatus200Ok() throws Exception {
        Long id = 1L;
        String currency = "Real";
        String country = "Brazil";

        PurchaseDto purchaseDto = new PurchaseDto();
        PurchaseResponse purchaseResponse = new PurchaseResponse();

        given(purchaseService.getCurrencyConvertedPurchase(id, currency, country)).willReturn(purchaseDto);
        given(purchaseMapper.toResponse(purchaseDto)).willReturn(purchaseResponse);

        RequestBuilder request = MockMvcRequestBuilders.get(V1_PURCHASES_ID_EXCHANGERATES_CONVERSION, id)
                .param("country", country)
                .param("currency", currency)
                .contentType(MediaType.APPLICATION_JSON);

        mvc.perform(request)
                .andExpect(status().isOk());

        verify(purchaseService, times(1)).getCurrencyConvertedPurchase(id, currency, country);
        verify(purchaseMapper, times(1)).toResponse(purchaseDto);

        verifyNoMoreInteractions(purchaseService);
        verifyNoMoreInteractions(purchaseMapper);
    }

    @Test
    void givenNoCurrencyWhenGetCurrencyConvertedPurchaseThenStatus400BadRequest() throws Exception {
        Long id = 1L;
        String country = "Brazil";

        RequestBuilder request = MockMvcRequestBuilders.get(V1_PURCHASES_ID_EXCHANGERATES_CONVERSION, id)
                .param("country", country)
                .contentType(MediaType.APPLICATION_JSON);

        mvc.perform(request)
                .andExpect(status().isBadRequest());

        verifyNoInteractions(purchaseService);
        verifyNoInteractions(purchaseMapper);
    }

    @Test
    void givenNoCountryWhenGetCurrencyConvertedPurchaseThenStatus400BadRequest() throws Exception {
        Long id = 1L;
        String currency = "Real";

        RequestBuilder request = MockMvcRequestBuilders.get(V1_PURCHASES_ID_EXCHANGERATES_CONVERSION, id)
                .param("currency", currency)
                .contentType(MediaType.APPLICATION_JSON);

        mvc.perform(request)
                .andExpect(status().isBadRequest());

        verifyNoInteractions(purchaseService);
        verifyNoInteractions(purchaseMapper);
    }

}
