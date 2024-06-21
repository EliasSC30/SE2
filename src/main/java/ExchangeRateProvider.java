/**
 * Interface for providing exchange rates between currencies.
 */
public interface ExchangeRateProvider {

    /**
     * Enum defining various types of exchange rates.
     */
    enum ExchangeRateType {
        REALTIME,
        MONTHLY,
        DAILY
    }

    /**
     * Retrieves the exchange rate from one currency to another based on the specified rate type.
     *
     * @param from The currency to convert from.
     * @param to   The currency to convert to.
     * @param type The type of exchange rate (e.g., REALTIME, MONTHLY, DAILY).
     * @return The exchange rate from currency 'from' to currency 'to' as a Double value.
     */
    Double getExchangeRate(Currency from, Currency to, ExchangeRateType type);
}
