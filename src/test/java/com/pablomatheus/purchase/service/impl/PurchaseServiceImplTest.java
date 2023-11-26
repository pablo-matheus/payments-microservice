package com.pablomatheus.purchase.service.impl;

import com.pablomatheus.purchase.client.UnitedStatesTreasuryClient;
import com.pablomatheus.purchase.client.response.ExchangeRateDataResponse;
import com.pablomatheus.purchase.client.response.ExchangeRateResponse;
import com.pablomatheus.purchase.dto.PurchaseAmountDto;
import com.pablomatheus.purchase.dto.PurchaseDto;
import com.pablomatheus.purchase.entity.PurchaseEntity;
import com.pablomatheus.purchase.mapper.PurchaseMapper;
import com.pablomatheus.purchase.repository.PurchaseRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.verify;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@ExtendWith(MockitoExtension.class)
class PurchaseServiceImplTest {

    @Mock
    private PurchaseRepository purchaseRepository;

    @Mock
    private PurchaseMapper purchaseMapper;

    @Mock
    private UnitedStatesTreasuryClient unitedStatesTreasuryClient;

    @InjectMocks
    private PurchaseServiceImpl purchaseService;

    @Test
    void givenValidPurchaseWhenAddPurchaseThenReturnAddedPurchase() {
        PurchaseDto purchaseDto = new PurchaseDto();
        PurchaseEntity purchaseEntity = new PurchaseEntity();

        given(purchaseMapper.toEntity(purchaseDto)).willReturn(purchaseEntity);
        given(purchaseRepository.save(purchaseEntity)).willReturn(purchaseEntity);
        given(purchaseMapper.toDto(purchaseEntity)).willReturn(purchaseDto);

        PurchaseDto result = purchaseService.addPurchase(purchaseDto);

        Assertions.assertEquals(purchaseDto, result);

        verify(purchaseMapper, times(1)).toEntity(purchaseDto);
        verify(purchaseRepository, times(1)).save(purchaseEntity);
        verify(purchaseMapper, times(1)).toDto(purchaseEntity);

        verifyNoMoreInteractions(purchaseMapper);
        verifyNoMoreInteractions(purchaseRepository);
    }

    @Test
    void givenValidParametersWhenGetCurrencyConvertedPurchaseThenReturnPurchase() {
        Long id = 1L;
        String currency = "Real";
        String country = "Brazil";

        PurchaseEntity purchaseEntity = new PurchaseEntity();
        purchaseEntity.setCurrency("Dollar");
        purchaseEntity.setCountry("United States");

        PurchaseDto purchaseDto = new PurchaseDto();
        purchaseDto.setTransactionDate(LocalDateTime.now());

        PurchaseAmountDto purchaseAmountDto = new PurchaseAmountDto();
        purchaseAmountDto.setValue("11.25");

        purchaseDto.setOriginalAmount(purchaseAmountDto);

        ExchangeRateResponse exchangeRateResponse = new ExchangeRateResponse();

        ExchangeRateDataResponse exchangeRateDataResponse = new ExchangeRateDataResponse();
        exchangeRateDataResponse.setCurrency(currency);
        exchangeRateDataResponse.setCountry(country);

        String exchangeRate = "5.033";
        exchangeRateDataResponse.setExchangeRate(new BigDecimal(exchangeRate));

        exchangeRateResponse.setData(List.of(exchangeRateDataResponse));

        given(purchaseRepository.findById(id)).willReturn(Optional.of(purchaseEntity));
        given(purchaseMapper.toDto(purchaseEntity)).willReturn(purchaseDto);
        given(unitedStatesTreasuryClient.getExchangeRate(anyString(), eq("-record_date"))).willReturn(exchangeRateResponse);

        PurchaseDto result = purchaseService.getCurrencyConvertedPurchase(id, currency, country);

        Assertions.assertEquals("56.62", result.getConvertedAmount().getValue());
        Assertions.assertEquals(currency, result.getConvertedAmount().getCurrency());
        Assertions.assertEquals(country, result.getConvertedAmount().getCountry());
        Assertions.assertEquals(exchangeRate, result.getConvertedAmount().getExchangeRate());

        verify(purchaseRepository, times(1)).findById(id);
        verify(purchaseMapper, times(1)).toDto(purchaseEntity);
        verify(unitedStatesTreasuryClient, times(1)).getExchangeRate(anyString(), eq("-record_date"));

        verifyNoMoreInteractions(purchaseRepository);
        verifyNoMoreInteractions(purchaseMapper);
        verifyNoMoreInteractions(unitedStatesTreasuryClient);
    }

    @Test
    void givenNoExchangeRateFoundWhenGetCurrencyConvertedPurchaseThenThrowException() {
        Long id = 1L;
        String currency = "Real";
        String country = "Brazil";

        PurchaseEntity purchaseEntity = new PurchaseEntity();
        purchaseEntity.setCurrency("Dollar");
        purchaseEntity.setCountry("United States");

        PurchaseDto purchaseDto = new PurchaseDto();
        purchaseDto.setTransactionDate(LocalDateTime.now());

        ExchangeRateResponse exchangeRateResponse = new ExchangeRateResponse();

        given(purchaseRepository.findById(id)).willReturn(Optional.of(purchaseEntity));
        given(purchaseMapper.toDto(purchaseEntity)).willReturn(purchaseDto);
        given(unitedStatesTreasuryClient.getExchangeRate(anyString(), eq("-record_date"))).willReturn(exchangeRateResponse);

        ResponseStatusException exception = Assertions.assertThrows(
                ResponseStatusException.class,
                () -> purchaseService.getCurrencyConvertedPurchase(id, currency, country));

        Assertions.assertEquals("No exchange rates were found to perform the conversion", exception.getReason());
        Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, exception.getStatusCode());

        verify(purchaseRepository, times(1)).findById(id);
        verify(purchaseMapper, times(1)).toDto(purchaseEntity);
        verify(unitedStatesTreasuryClient, times(1)).getExchangeRate(anyString(), eq("-record_date"));

        verifyNoMoreInteractions(purchaseRepository);
        verifyNoMoreInteractions(purchaseMapper);
        verifyNoMoreInteractions(unitedStatesTreasuryClient);
    }

    @Test
    void givenInvalidPurchaseCountryWhenGetCurrencyConvertedPurchaseThenThrowException() {
        Long id = 1L;
        String currency = "Real";
        String country = "Brazil";

        PurchaseEntity purchaseEntity = new PurchaseEntity();
        purchaseEntity.setCurrency("Dollar");
        purchaseEntity.setCountry("Canada");

        given(purchaseRepository.findById(id)).willReturn(Optional.of(purchaseEntity));

        ResponseStatusException exception = Assertions.assertThrows(
                ResponseStatusException.class,
                () -> purchaseService.getCurrencyConvertedPurchase(id, currency, country));

        Assertions.assertEquals("The conversion is not available, the original purchase was not made in US Dollars", exception.getReason());
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());

        verify(purchaseRepository, times(1)).findById(id);

        verifyNoMoreInteractions(purchaseRepository);

        verifyNoInteractions(purchaseMapper);
        verifyNoInteractions(unitedStatesTreasuryClient);
    }

    @Test
    void givenInvalidPurchaseCurrencyWhenGetCurrencyConvertedPurchaseThenThrowException() {
        Long id = 1L;
        String currency = "Real";
        String country = "Brazil";

        PurchaseEntity purchaseEntity = new PurchaseEntity();
        purchaseEntity.setCurrency("Pesos");
        purchaseEntity.setCountry("United States");

        given(purchaseRepository.findById(id)).willReturn(Optional.of(purchaseEntity));

        ResponseStatusException exception = Assertions.assertThrows(
                ResponseStatusException.class,
                () -> purchaseService.getCurrencyConvertedPurchase(id, currency, country));

        Assertions.assertEquals("The conversion is not available, the original purchase was not made in US Dollars", exception.getReason());
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());

        verify(purchaseRepository, times(1)).findById(id);

        verifyNoMoreInteractions(purchaseRepository);

        verifyNoInteractions(purchaseMapper);
        verifyNoInteractions(unitedStatesTreasuryClient);
    }

    @Test
    void givenNotFoundPurchaseTransactionWhenGetCurrencyConvertedPurchaseThenThrowException() {
        Long id = 1L;
        String currency = "Real";
        String country = "Brazil";

        given(purchaseRepository.findById(id)).willReturn(Optional.empty());

        ResponseStatusException exception = Assertions.assertThrows(
                ResponseStatusException.class,
                () -> purchaseService.getCurrencyConvertedPurchase(id, currency, country));

        Assertions.assertEquals("The purchase was not found", exception.getReason());
        Assertions.assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());

        verify(purchaseRepository, times(1)).findById(id);

        verifyNoMoreInteractions(purchaseRepository);
        verifyNoInteractions(purchaseMapper);

        verifyNoInteractions(unitedStatesTreasuryClient);
    }

}
