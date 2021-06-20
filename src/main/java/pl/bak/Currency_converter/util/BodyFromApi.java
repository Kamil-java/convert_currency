package pl.bak.Currency_converter.util;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import pl.bak.Currency_converter.model.Currency;
import pl.bak.Currency_converter.uri.NbpPathApi;
import pl.bak.Currency_converter.uri.body_for_request.DataSingleObject;

import java.util.Optional;

@Component
public class BodyFromApi {
    private final RestTemplate restTemplate;

    public BodyFromApi(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public Optional<Currency> getSingleBodyFromApi(String fromCurrency) {
        NbpPathApi pathApi = new NbpPathApi();
        DataSingleObject currencyData = restTemplate.getForObject(pathApi.getSingleData() + fromCurrency, DataSingleObject.class);
        Optional<DataSingleObject> singleObjectOptional = Optional.ofNullable(currencyData);

        if (singleObjectOptional.isEmpty()) {
            return Optional.empty();
        }

        return Optional.ofNullable(new DataRequestMapper().mapFromRequestToCurrencyDto(currencyData));
    }
}
