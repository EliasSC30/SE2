public interface ConverterClient {
    MoneyValue convertTo(MoneyValue mv, Currency toCurrency);

    MoneyValue convertTo(MoneyValue mv, Currency toCurrency, ExchangeRateProvider.ExchangeRateType type);
}
