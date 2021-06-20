package pl.bak.Currency_converter.domain;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import pl.bak.Currency_converter.model.Currency;

import java.math.BigDecimal;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class CurrencyController {
    private final CurrencyService currencyService;

    public CurrencyController(CurrencyService currencyService) {
        this.currencyService = currencyService;
    }

    @PostMapping("/sell/{fromCode}/{toCode}")
    public Currency sellCurrency(@PathVariable("fromCode") String fromCode, @PathVariable("toCode") String toCode,
                                    @RequestParam("value") double value) {
        fromCode = fromCode.toUpperCase();
        toCode = toCode.toUpperCase();

        if (fromCode.equals(toCode)){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        if (currencyService.checkIfCodeIsSupported(fromCode, toCode)) {
           throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        BigDecimal decimal = BigDecimal.valueOf(value);
        if (decimal.compareTo(BigDecimal.ZERO) < 0){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        Optional<Currency> currency = currencyService.sellCurrency(fromCode, toCode, decimal);
        return currency
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR));
    }

    @PostMapping("/purchase/{fromCode}/{toCode}")
    public Currency purchaseCurrency(@PathVariable("fromCode") String fromCode, @PathVariable("toCode") String toCode,
                                    @RequestParam("value") double value) {
        fromCode = fromCode.toUpperCase();
        toCode = toCode.toUpperCase();

        if (fromCode.equals(toCode)){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        if (currencyService.checkIfCodeIsSupported(fromCode, toCode)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        BigDecimal decimal = BigDecimal.valueOf(value);
        if (decimal.compareTo(BigDecimal.ZERO) < 0){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        return currencyService.purchaseCurrency(fromCode, toCode, decimal)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR));
    }
}
