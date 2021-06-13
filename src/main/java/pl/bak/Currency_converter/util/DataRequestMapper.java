package pl.bak.Currency_converter.util;

import pl.bak.Currency_converter.model.Currency;
import pl.bak.Currency_converter.model.CurrencyCode;
import pl.bak.Currency_converter.uri.body_for_request.DataInArray;
import pl.bak.Currency_converter.uri.body_for_request.DataSingleObject;

import java.math.BigDecimal;

public class DataRequestMapper {
    public Currency mapFromRequestToCurrencyDto(DataSingleObject currencyData) {
        Currency currency = new Currency();
        currency.setCurrencyCode(CurrencyCode.valueOf(currencyData.getCode()));
        DataInArray dataInArray = currencyData.getRates().get(0);

        BigDecimal purchaseValue = BigDecimal.valueOf(dataInArray.getBid());

        BigDecimal saleValue = BigDecimal.valueOf(dataInArray.getAsk());

        currency.setPurchaseValue(purchaseValue);
        currency.setSaleValue(saleValue);
        return currency;
    }
}
