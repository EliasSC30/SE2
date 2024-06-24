/**
 * The ConverterClient interface defines methods for converting monetary values between different currencies.
 * Implementations of this interface provide functionality to convert MoneyValue objects from one currency to another
 * using various exchange rate types.
 */
public interface ConverterClient {

    /**
     * Converts the given MoneyValue object to the specified currency.
     *
     * @param mv          The MoneyValue object representing the monetary value to convert.
     * @param toCurrency  The target currency to which mv should be converted.
     * @return A new MoneyValue object representing the converted monetary value in the specified currency.
     */
    MoneyValue convertTo(MoneyValue mv, Currency toCurrency);

    /**
     * Converts the given MoneyValue object to the specified currency using a specific exchange rate type.
     *
     * @param mv          The MoneyValue object representing the monetary value to convert.
     * @param toCurrency  The target currency to which mv should be converted.
     * @param type        The type of exchange rate to use for the conversion (e.g., realtime, monthly, daily).
     * @return A new MoneyValue object representing the converted monetary value in the specified currency.
     */
    MoneyValue convertTo(MoneyValue mv, Currency toCurrency, ExchangeRateProvider.ExchangeRateType type);
}
