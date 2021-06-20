# Convert_currency
* [General info](#general-info)
* [Technological Stack](#technological-stack)
* [Endpoints](#endpoints)

## General info

The application will convert one currency to another according to the NBP api pattern. The supported currencies are: PLN, USD, GBP and EUR.

The application uses <b>actuator</b> to facilitate checking during startup.
Endpoint documentation is available [here](https://docs.spring.io/spring-boot/docs/current/reference/html/production-ready-features.html).

(The actuator only has default endpoints enabled. The other endpoints are disabled)

## Technological Stack
### Back-End
* Java (11)
* Spring-Boot
* Spring-Web
* Actuator
#### Tests
* JUnit
* Mockito
* Spring RestDocs
### Build Tools
* Maven

## Endpoints

### Mainly endpoints

* Sell currency

[Full documentation](https://github.com/Kamil-java/convert_currency/tree/master/documentation/endpoints)

```
Sell -> POST - http://localhost:8080/api/sell/{fromCurrency}/{toCurrency}?value=[ value ]
Required params value as number. Please read documentation below
```
[Example request](https://github.com/Kamil-java/convert_currency/tree/master/documentation/endpoints/sell-currency)

```
Purchase -> POST - http://localhost:8080/api/purchase/{fromCurrency}/{toCurrency}?value=[ value ]
Required params value as number. Please read documentation below
```
[Example request](https://github.com/Kamil-java/convert_currency/tree/master/documentation/endpoints/purchase-currency)
