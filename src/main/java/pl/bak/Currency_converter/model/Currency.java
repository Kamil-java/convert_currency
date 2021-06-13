package pl.bak.Currency_converter.model;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.math.BigDecimal;
import java.util.Objects;

public class Currency {
    private CurrencyCode currencyCode;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private BigDecimal saleValue;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private BigDecimal purchaseValue;

    public CurrencyCode getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(CurrencyCode currencyCode) {
        this.currencyCode = currencyCode;
    }

    public BigDecimal getSaleValue() {
        return saleValue;
    }

    public void setSaleValue(BigDecimal saleValue) {
        this.saleValue = saleValue;
    }

    public BigDecimal getPurchaseValue() {
        return purchaseValue;
    }

    public void setPurchaseValue(BigDecimal purchaseValue) {
        this.purchaseValue = purchaseValue;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Currency currency = (Currency) o;

        if (currencyCode != currency.currencyCode) return false;
        if (!Objects.equals(saleValue, currency.saleValue)) return false;
        return Objects.equals(purchaseValue, currency.purchaseValue);
    }

    @Override
    public int hashCode() {
        int result = currencyCode != null ? currencyCode.hashCode() : 0;
        result = 31 * result + (saleValue != null ? saleValue.hashCode() : 0);
        result = 31 * result + (purchaseValue != null ? purchaseValue.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Currency{" +
                "currencyCode=" + currencyCode +
                ", saleValue=" + saleValue +
                ", purchaseValue=" + purchaseValue +
                '}';
    }
}
