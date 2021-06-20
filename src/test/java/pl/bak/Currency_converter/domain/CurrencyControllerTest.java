package pl.bak.Currency_converter.domain;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import pl.bak.Currency_converter.model.Currency;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static pl.bak.Currency_converter.model.CurrencyCode.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CurrencyController.class)
@AutoConfigureRestDocs(outputDir = "documentation/endpoints")
@AutoConfigureMockMvc
class CurrencyControllerTest {

    @MockBean
    private CurrencyService currencyService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void sellCurrency() throws Exception {
        //given
        given(currencyService.sellCurrency(USD.value, EUR.value, BigDecimal.valueOf(3.0))).willReturn(prepareCurrencySell());
        given(currencyService.purchaseCurrency("RUB", EUR.value, BigDecimal.valueOf(3.0))).willReturn(Optional.empty());
        given(currencyService.checkIfCodeIsSupported(USD.value, EUR.value)).willReturn(false);
        given(currencyService.checkIfCodeIsSupported("CZK", EUR.value)).willReturn(true);

        //when
        ResultActions perform = mockMvc.perform(post("/api/sell/{fromCode}/{toCode}", USD.value, EUR.value)
                .contentType(MediaType.APPLICATION_JSON)
                .param("value", "3")
        );

        ResultActions rub = mockMvc.perform(post("/api/purchase/{fromCode}/{toCode}", "RUB", EUR.value)
                .contentType(MediaType.APPLICATION_JSON)
                .param("value", "3")
        );

        ResultActions czk = mockMvc.perform(post("/api/purchase/{fromCode}/{toCode}", "CZK", EUR.value)
                .contentType(MediaType.APPLICATION_JSON)
                .param("value", "3")
        );

        //then
        perform
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$.currencyCode").exists())
                .andExpect(jsonPath("$.currencyCode").isNotEmpty())
                .andExpect(jsonPath("$.currencyCode").isString())
                .andExpect(jsonPath("$.currencyCode").value(EUR.value))
                .andExpect(jsonPath("$.saleValue").exists())
                .andExpect(jsonPath("$.saleValue").isNotEmpty())
                .andExpect(jsonPath("$.saleValue").isNumber())
                .andExpect(jsonPath("$.saleValue").value(2.46))
                .andExpect(jsonPath("$.purchaseValue").doesNotExist())
                .andDo(print())
                .andDo(document("sell-currency"));

        rub
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$").doesNotExist())
                .andDo(print())
                .andDo(document("sell-currency-is-not-in-api"));

        czk
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$").doesNotExist())
                .andDo(print())
                .andDo(document("sell-currency-if-code-doesn't-exist"));
    }

    @Test
    void purchaseCurrency() throws Exception {
        //given
        given(currencyService.purchaseCurrency(USD.value, EUR.value, BigDecimal.valueOf(3.0))).willReturn(prepareCurrencyPurchase());
        given(currencyService.purchaseCurrency("RUB", EUR.value, BigDecimal.valueOf(3.0))).willReturn(Optional.empty());
        given(currencyService.checkIfCodeIsSupported(USD.value, EUR.value)).willReturn(false);
        given(currencyService.checkIfCodeIsSupported("CZK", EUR.value)).willReturn(true);

        //when
        ResultActions perform = mockMvc.perform(post("/api/purchase/{fromCode}/{toCode}", USD.value, EUR.value)
                .contentType(MediaType.APPLICATION_JSON)
                .param("value", "3")
        );

        ResultActions rub = mockMvc.perform(post("/api/purchase/{fromCode}/{toCode}", "RUB", EUR.value)
                .contentType(MediaType.APPLICATION_JSON)
                .param("value", "3")
        );

        ResultActions czk = mockMvc.perform(post("/api/purchase/{fromCode}/{toCode}", "CZK", EUR.value)
                .contentType(MediaType.APPLICATION_JSON)
                .param("value", "3")
        );

        //then
        perform
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$.currencyCode").exists())
                .andExpect(jsonPath("$.currencyCode").isNotEmpty())
                .andExpect(jsonPath("$.currencyCode").isString())
                .andExpect(jsonPath("$.currencyCode").value(EUR.value))
                .andExpect(jsonPath("$.purchaseValue").exists())
                .andExpect(jsonPath("$.purchaseValue").isNotEmpty())
                .andExpect(jsonPath("$.purchaseValue").isNumber())
                .andExpect(jsonPath("$.purchaseValue").value(2.46))
                .andExpect(jsonPath("$.saleValue").doesNotExist())
                .andDo(print())
                .andDo(document("purchase-currency"));

        rub
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$").doesNotExist())
                .andDo(print())
                .andDo(document("purchase-currency-is-not-in-api"));

        czk
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$").doesNotExist())
                .andDo(print())
                .andDo(document("purchase-currency-if-code-doesn't-exist"));
    }

    private Optional<Currency> prepareCurrencySell() {
        Currency currency = new Currency();

        currency.setCurrencyCode(EUR);
        currency.setSaleValue(BigDecimal.valueOf(2.46));

        return Optional.of(currency);
    }

    private Optional<Currency> prepareCurrencyPurchase() {
        Currency currency = new Currency();

        currency.setCurrencyCode(EUR);
        currency.setPurchaseValue(BigDecimal.valueOf(2.46));

        return Optional.of(currency);
    }
}