package com.pablomatheus.purchase.client.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Data
@NoArgsConstructor
public class ExchangeRateDataResponse {

    private String recordDate;

    private String country;

    private String currency;

    @JsonProperty("country_currency_desc")
    private String countryCurrencyDescription;

    private BigDecimal exchangeRate;

    private String effectiveDate;

    @JsonProperty("src_line_nbr")
    private String sourceLineNumber;

    private String recordFiscalYear;

    private String recordFiscalQuarter;

    private String recordCalendarYear;

    private String recordCalendarQuarter;

    private String recordCalendarMonth;

    private String recordCalendarDay;

}
