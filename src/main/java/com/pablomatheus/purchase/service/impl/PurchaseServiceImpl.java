package com.pablomatheus.purchase.service.impl;

import com.pablomatheus.purchase.client.UnitedStatesTreasuryClient;
import com.pablomatheus.purchase.client.response.ExchangeRateResponse;
import com.pablomatheus.purchase.client.response.ExchangeRateDataResponse;
import com.pablomatheus.purchase.dto.PurchaseAmountDto;
import com.pablomatheus.purchase.dto.PurchaseDto;
import com.pablomatheus.purchase.entity.PurchaseEntity;
import com.pablomatheus.purchase.mapper.PurchaseMapper;
import com.pablomatheus.purchase.repository.PurchaseRepository;
import com.pablomatheus.purchase.service.PurchaseService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class PurchaseServiceImpl implements PurchaseService {

    private final PurchaseRepository purchaseRepository;
    private final PurchaseMapper purchaseMapper;
    private final UnitedStatesTreasuryClient unitedStatesTreasuryClient;

    @Transactional
    @Override
    public PurchaseDto addPurchase(PurchaseDto purchaseDto) {
        PurchaseEntity purchaseEntity = purchaseRepository.save(purchaseMapper.toEntity(purchaseDto));
        return purchaseMapper.toDto(purchaseEntity);
    }

    @Override
    public PurchaseDto getCurrencyConvertedPurchase(String id, String currency, String country) {
        Optional<PurchaseEntity> purchaseEntityOptional = purchaseRepository.findById(id);

        if (purchaseEntityOptional.isEmpty()) {
            log.warn("Purchase not found with the ID [{}]", id);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Purchase not found");
        }

        PurchaseEntity purchaseEntity = purchaseEntityOptional.get();

        if (!Objects.equals(purchaseEntity.getCurrency(), "Dollar") &&
            !Objects.equals(purchaseEntity.getCountry(), "United States")) {

            log.warn("It's not possible to convert the purchase with ID [{}], the currency conversion is not available for [{}] and  country [{}]",
                    id, currency, country);

            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The conversion is not available, the original purchase was not made in US Dollars");
        }

        PurchaseDto purchaseDto = purchaseMapper.toDto(purchaseEntity);

        PurchaseAmountDto convertedAmountDto =
                getConvertedAmount(purchaseDto, currency, country);

        purchaseDto.setConvertedAmount(convertedAmountDto);

        return purchaseDto;
    }

    private PurchaseAmountDto getConvertedAmount(PurchaseDto purchaseDto,
                                                 String currency,
                                                 String country) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String startDate = purchaseDto.getTransactionDate().toLocalDate().minusMonths(6).format(formatter);
        String endDate = purchaseDto.getTransactionDate().toLocalDate().format(formatter);

        String filter = String.format("record_date:gte:%s,record_date:lte:%s,currency:eq:%s,country:eq:%s",
                startDate, endDate, currency, country);

        ExchangeRateResponse exchangeRateResponse = unitedStatesTreasuryClient.getExchangeRate(filter, "-record_date");

        ExchangeRateDataResponse exchangeRateDataResponse = Optional.ofNullable(exchangeRateResponse)
                .map(ExchangeRateResponse::getData)
                .filter(data -> !data.isEmpty())
                .map(data -> data.get(0))
                .orElseThrow(() -> {
                    log.warn("It was not possible to convert the purchase with ID [{}], no exchange rates were found with start date [{}], end date [{}], currency [{}] and country [{}]",
                            purchaseDto.getId(), startDate, endDate, currency, country);

                    return new ResponseStatusException(
                            HttpStatus.INTERNAL_SERVER_ERROR, "No exchange rates were found to perform the conversion");
                });

        String convertedAmountValue = new BigDecimal(purchaseDto.getOriginalAmount().getValue())
                .multiply(exchangeRateDataResponse.getExchangeRate())
                .setScale(2, RoundingMode.HALF_EVEN)
                .toPlainString();

        PurchaseAmountDto convertedAmountDto = new PurchaseAmountDto();
        convertedAmountDto.setCurrency(exchangeRateDataResponse.getCurrency());
        convertedAmountDto.setCountry(exchangeRateDataResponse.getCountry());
        convertedAmountDto.setValue(convertedAmountValue);
        convertedAmountDto.setExchangeRate(exchangeRateDataResponse.getExchangeRate().toPlainString());

        return convertedAmountDto;
    }

}
