import java.math.BigDecimal;

/**
 * The Converter class implements the ConverterClient interface and provides methods to convert
 * monetary values between different currencies using an ExchangeRateProvider.
 */
public class Converter implements ConverterClient {
    private final ExchangeRateProvider exchangeRateProvider;

    /**
     * Constructs a Converter with the specified ExchangeRateProvider.
     * The ExchangeRateProvider is used to obtain exchange rates for currency conversion.
     *
     * @param exchangeRateProvider the ExchangeRateProvider used to fetch exchange rates
     */
    Converter(ExchangeRateProvider exchangeRateProvider) {
        this.exchangeRateProvider = exchangeRateProvider;
    }

    /**
     * Converts the specified MoneyValue to the given target currency using the real-time exchange rate.
     * This method uses the default exchange rate type of REALTIME to perform the conversion.
     *
     * @param mv the MoneyValue to convert
     * @param toCurrency the target currency to convert to
     * @return a new MoneyValue instance representing the converted amount in the target currency
     * @throws MoneyValue.InvalidMoneyValueException if the MoneyValue or target currency is null
     */
    public MoneyValue convertTo(MoneyValue mv, Currency toCurrency) {
        return convertTo(mv, toCurrency, ExchangeRateProvider.ExchangeRateType.REALTIME);
    }

    /**
     * Converts the specified MoneyValue to the given target currency using the specified exchange rate type.
     * The exchange rate type can be REALTIME, MONTHLY DAILY.
     *
     * @param mv the MoneyValue to convert
     * @param toCurrency the target currency to convert to
     * @param type the type of exchange rate to use for conversion
     * @return a new MoneyValue instance representing the converted amount in the target currency
     * @throws MoneyValue.InvalidMoneyValueException if the MoneyValue or target currency is null
     */
    public MoneyValue convertTo(MoneyValue mv, Currency toCurrency, ExchangeRateProvider.ExchangeRateType type) {
        if (mv == null || toCurrency == null) {
            throw new MoneyValue.InvalidMoneyValueException(MoneyValue.INVALID_MONEY_VALUE_AS_STRING);
        }

        BigDecimal toFactor = BigDecimal.valueOf(exchangeRateProvider.getExchangeRate(mv.getCurrency(), toCurrency, type));
        return new MoneyValue(mv.getAmount().multiply(toFactor), toCurrency);
    }
}
