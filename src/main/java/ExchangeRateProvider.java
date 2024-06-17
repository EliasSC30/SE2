public interface ExchangeRateProvider {
    Double getExchangeRate(Currency from, Currency to);
}
