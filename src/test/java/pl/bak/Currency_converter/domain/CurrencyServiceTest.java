package pl.bak.Currency_converter.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.bak.Currency_converter.model.Currency;
import pl.bak.Currency_converter.model.CurrencyCode;
import pl.bak.Currency_converter.util.BodyFromApi;
import pl.bak.Currency_converter.util.ValueCalculate;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static pl.bak.Currency_converter.model.CurrencyCode.*;

@ExtendWith(MockitoExtension.class)
class CurrencyServiceTest {

    @Mock
    private ValueCalculate valueCalculate;

    @Mock
    private BodyFromApi bodyFromApi;

    private CurrencyService currencyService;

    @BeforeEach
    void setUp() {
        currencyService = new CurrencyService(valueCalculate, bodyFromApi);
    }

    @Test
    void sellCurrency() {
        //given
        given(bodyFromApi.getSingleBodyFromApi(USD.value)).willReturn(Optional.of(prepareCurrencyUSD()));
        given(bodyFromApi.getSingleBodyFromApi(GBP.value)).willReturn(Optional.of(prepareCurrencyGBP()));

        Currency currencyUSD = prepareCurrencyUSD();
        currencyUSD.setPurchaseValue(null);

        Currency currencyGBP = prepareCurrencyGBP();
        currencyGBP.setPurchaseValue(null);

        BigDecimal value = BigDecimal.valueOf(3);

        given(valueCalculate.calculateValue(EUR.value, value, currencyUSD)).willReturn(BigDecimal.valueOf(2.46));
        given(valueCalculate.calculateValue(EUR.value, value, currencyGBP)).willReturn(BigDecimal.valueOf(3.44));

        //when
        Optional<Currency> currencyUSDEUR = currencyService.sellCurrency(USD.value, EUR.value, value);
        Optional<Currency> currencyGBPEUR = currencyService.sellCurrency(GBP.value, EUR.value, value);

        //then
        sellAssertion(currencyUSDEUR, 2.46);
        sellAssertion(currencyGBPEUR, 3.44);
    }

    private void sellAssertion(Optional<Currency> currency, double value) {
        assertThat(currency.isPresent()).isTrue();
        assertThat(currency).get()
                .hasSameClassAs(new Currency())
                .hasFieldOrPropertyWithValue("currencyCode", CurrencyCode.EUR)
                .hasFieldOrPropertyWithValue("saleValue", BigDecimal.valueOf(value));
        assertThat(currency.get().getPurchaseValue()).isNull();
    }

    @Test
    void purchaseCurrency() {
        //given
        given(bodyFromApi.getSingleBodyFromApi(USD.value)).willReturn(Optional.of(prepareCurrencyUSD()));
        given(bodyFromApi.getSingleBodyFromApi(GBP.value)).willReturn(Optional.of(prepareCurrencyGBP()));

        Currency currencyUSD = prepareCurrencyUSD();
        currencyUSD.setSaleValue(null);

        Currency currencyGBP = prepareCurrencyGBP();
        currencyGBP.setSaleValue(null);

        given(valueCalculate.calculateValue(EUR.value, BigDecimal.valueOf(3), currencyUSD)).willReturn(BigDecimal.valueOf(2.46));
        given(valueCalculate.calculateValue(EUR.value, BigDecimal.valueOf(3), currencyGBP)).willReturn(BigDecimal.valueOf(3.44));

        //when
        Optional<Currency> currencyUSDEUR = currencyService.purchaseCurrency(USD.value, EUR.value, BigDecimal.valueOf(3));
        Optional<Currency> currencyGBPEUR = currencyService.purchaseCurrency(GBP.value, EUR.value, BigDecimal.valueOf(3));

        //then
        purchaseAssertion(currencyUSDEUR, 2.46);
        purchaseAssertion(currencyGBPEUR, 3.44);
    }

    private void purchaseAssertion(Optional<Currency> currency, double value) {
        assertThat(currency.isPresent()).isTrue();
        assertThat(currency).get()
                .hasSameClassAs(new Currency())
                .hasFieldOrPropertyWithValue("currencyCode", EUR)
                .hasFieldOrPropertyWithValue("purchaseValue", BigDecimal.valueOf(value));
        assertThat(currency.get().getSaleValue()).isNull();
    }

    @Test
    void checkIfCodeIsSupported() {
        //when
        boolean yes = currencyService.checkIfCodeIsSupported(USD.value, EUR.value);
        boolean no = currencyService.checkIfCodeIsSupported(USD.value, "RUB");

        //then
        assertThat(yes).isFalse();
        assertThat(no).isTrue();
    }

    private Currency prepareCurrencyUSD() {
        Currency currency = new Currency();

        currency.setCurrencyCode(USD);
        currency.setPurchaseValue(BigDecimal.valueOf(3.7734));
        currency.setSaleValue(BigDecimal.valueOf(3.8496));

        return currency;
    }

    private Currency prepareCurrencyGBP() {
        Currency currency = new Currency();

        currency.setCurrencyCode(GBP);
        currency.setPurchaseValue(BigDecimal.valueOf(5.2608));
        currency.setSaleValue(BigDecimal.valueOf(5.3670));

        return currency;
    }

    private Currency prepareCurrencyEUR() {
        Currency currency = new Currency();

        currency.setCurrencyCode(EUR);
        currency.setPurchaseValue(BigDecimal.valueOf(4.5016));
        currency.setSaleValue(BigDecimal.valueOf(4.5926));

        return currency;
    }
}