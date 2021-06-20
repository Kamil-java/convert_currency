package pl.bak.Currency_converter.domain;

import org.springframework.stereotype.Service;
import pl.bak.Currency_converter.model.Currency;
import pl.bak.Currency_converter.model.CurrencyCode;
import pl.bak.Currency_converter.util.BodyFromApi;
import pl.bak.Currency_converter.util.ValueCalculate;

import java.math.BigDecimal;
import java.util.EnumSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static java.math.RoundingMode.HALF_UP;
import static pl.bak.Currency_converter.model.CurrencyCode.*;

@Service
public class CurrencyService {
    private final ValueCalculate valueCalculate;
    private final BodyFromApi bodyFromApi;

    public CurrencyService(ValueCalculate valueCalculate, BodyFromApi bodyFromApi) {
        this.valueCalculate = valueCalculate;
        this.bodyFromApi = bodyFromApi;
    }

    public Optional<Currency> sellCurrency(String fromCurrency, String toCurrency, BigDecimal value) {
        Currency currency = new Currency();

        if (fromCurrency.equals(PLN.value)) {

            Optional<Currency> singleBodyFromApi =  bodyFromApi.getSingleBodyFromApi(toCurrency);

            if (singleBodyFromApi.isEmpty()) {
                return Optional.empty();
            }

            currency.setCurrencyCode(PLN);
            BigDecimal calculateValue = value.divide(singleBodyFromApi.get().getSaleValue(), HALF_UP);
            currency.setSaleValue(calculateValue);
            currency.setCurrencyCode(CurrencyCode.valueOf(toCurrency));

            return Optional.of(currency);
        }

        Optional<Currency> singleBodyFromApi = bodyFromApi.getSingleBodyFromApi(fromCurrency);

        if (singleBodyFromApi.isEmpty()) {
            return Optional.empty();
        }

        currency = singleBodyFromApi.get();

        currency.setPurchaseValue(null);
        currency.setSaleValue(valueCalculate.calculateValue(toCurrency, value, currency));
        currency.setCurrencyCode(CurrencyCode.valueOf(toCurrency));

        return Optional.of(currency);
    }

    public Optional<Currency> purchaseCurrency(String fromCurrency, String toCurrency, BigDecimal value) {
        Currency currency = new Currency();

        if (fromCurrency.equals(PLN.value)) {
            Optional<Currency> singleBodyFromApi = bodyFromApi.getSingleBodyFromApi(toCurrency);

            if (singleBodyFromApi.isEmpty()) {
                return Optional.empty();
            }

            currency.setCurrencyCode(PLN);
            BigDecimal calculateValue = value.divide(singleBodyFromApi.get().getPurchaseValue(), HALF_UP);
            currency.setPurchaseValue(calculateValue);
            currency.setCurrencyCode(CurrencyCode.valueOf(toCurrency));

            return Optional.of(currency);
        }

        Optional<Currency> singleBodyFromApi = bodyFromApi.getSingleBodyFromApi(fromCurrency);

        if (singleBodyFromApi.isEmpty()) {
            return Optional.empty();
        }

        currency = singleBodyFromApi.get();

        currency.setSaleValue(null);
        currency.setPurchaseValue(valueCalculate.calculateValue(toCurrency, value, currency));
        currency.setCurrencyCode(CurrencyCode.valueOf(toCurrency));

        return Optional.of(currency);
    }

    public boolean checkIfCodeIsSupported(String fromCode, String toCode) {
        Set<String> currencyCodes = EnumSet.allOf(CurrencyCode.class)
                .stream()
                .map(Enum::name)
                .collect(Collectors.toSet());
        return !(currencyCodes.contains(fromCode) && currencyCodes.contains(toCode));
    }

}
