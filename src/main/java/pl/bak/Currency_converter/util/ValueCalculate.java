package pl.bak.Currency_converter.util;

import org.springframework.stereotype.Component;
import pl.bak.Currency_converter.model.Currency;
import pl.bak.Currency_converter.model.CurrencyCode;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;

import static java.math.RoundingMode.HALF_UP;
import static pl.bak.Currency_converter.model.CurrencyCode.*;

@Component
public class ValueCalculate {
    private final BodyFromApi bodyFromApi;

    public ValueCalculate(BodyFromApi bodyFromApi) {
        this.bodyFromApi = bodyFromApi;
    }

    public BigDecimal calculateValue(String currencyCode, BigDecimal value, Currency indicator) {
        if (value.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }

        BigDecimal calculateValue;

        switch (CurrencyCode.valueOf(currencyCode)) {
            case PLN:
                if (indicator.getSaleValue() == null) {
                    calculateValue = indicator.getPurchaseValue().multiply(value);
                } else {
                    calculateValue = indicator.getSaleValue().multiply(value);
                }

                calculateValue = commission(calculateValue);
                break;
            case EUR:
                calculateValue = calculateCurrency(EUR.value, value, indicator);
                break;
            case GBP:
                calculateValue = calculateCurrency(GBP.value, value, indicator);
                break;
            case USD:
                calculateValue = calculateCurrency(USD.value, value, indicator);
                break;
            default:
                throw new IllegalArgumentException("Code is incorrect");
        }

        return calculateValue;
    }

    private BigDecimal calculateCurrency(String currencyCode, BigDecimal value, Currency indicator) {
        Optional<Currency> apiValue = bodyFromApi.getSingleBodyFromApi(currencyCode);
        Currency currency = new Currency();
        BigDecimal calculateValue;

        if (apiValue.isPresent()) {
            currency = apiValue.get();
        }

        if (indicator.getSaleValue() == null) {
            value = indicator.getPurchaseValue().multiply(value);
            calculateValue = value.divide(currency.getPurchaseValue(), RoundingMode.HALF_UP);
        } else {
            value = indicator.getSaleValue().multiply(value);
            calculateValue = value.divide(currency.getSaleValue(), RoundingMode.HALF_UP);
        }

        return commission(calculateValue);
    }

    private BigDecimal commission(BigDecimal value) {
        double afterCommission = value.doubleValue() * 0.02;
        BigDecimal decimal = value.subtract(BigDecimal.valueOf(afterCommission));
        return decimal.setScale(2, HALF_UP);
    }
}
