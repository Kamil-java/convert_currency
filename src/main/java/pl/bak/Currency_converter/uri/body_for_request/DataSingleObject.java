package pl.bak.Currency_converter.uri.body_for_request;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class DataSingleObject implements Serializable {
    private String table;
    private String currency;
    private String code;
    private List<DataInArray> rates = new ArrayList<>();

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public List<DataInArray> getRates() {
        return rates;
    }

    public void setRates(List<DataInArray> rates) {
        this.rates = rates;
    }

    @Override
    public String toString() {
        return "DataSingleObject{" +
                "table='" + table + '\'' +
                ", currency='" + currency + '\'' +
                ", code='" + code + '\'' +
                ", rates=" + rates +
                '}';
    }
}

