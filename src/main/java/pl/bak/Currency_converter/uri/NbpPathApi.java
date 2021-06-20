package pl.bak.Currency_converter.uri;

public class NbpPathApi {
    private final String pathCurrencyPrefix = "http://api.nbp.pl/api/exchangerates/rates/";
    private final String pathTablePrefix = "http://api.nbp.pl/api/exchangerates/tables/";
    public String tableClass = "C/";

    public String getPathCurrencyPrefix() {
        return pathCurrencyPrefix;
    }

    public String getPathTablePrefix() {
        return pathTablePrefix;
    }

    public String getTableClass() {
        return tableClass;
    }

    public void setTableClass(String tableClass) {
        this.tableClass = tableClass;
    }

    public String getSingleData() {
        return pathCurrencyPrefix + tableClass;
    }

    public String getAllData() {
        return pathTablePrefix + tableClass;
    }
}
