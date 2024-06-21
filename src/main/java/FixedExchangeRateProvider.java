import java.util.HashMap;
import java.util.Map;

/**
 * Implementation of ExchangeRateProvider that provides fixed exchange rates between currencies.
 * Rates are stored separately for realtime, monthly, and daily exchange rate types.
 */
public class FixedExchangeRateProvider implements ExchangeRateProvider {

    private final Map<String, Double> realtimeRates = new HashMap<>();
    private final Map<String, Double> monthlyRates = new HashMap<>();
    private final Map<String, Double> dailyRates = new HashMap<>();

    /**
     * Constructs a FixedExchangeRateProvider and initializes fixed exchange rates.
     */
    public FixedExchangeRateProvider() {
        initializeRates();
    }

    /**
     * Initializes fixed exchange rates for different exchange rate types (realtime, monthly, daily).
     * Rates are stored in separate maps for each rate type.
     */
    private void initializeRates() {
        // Realtime exchange rates
        realtimeRates.put(createKey(Currency.US_DOLLAR, Currency.EURO), 0.93);
        realtimeRates.put(createKey(Currency.US_DOLLAR, Currency.JAPANESE_YEN), 157.74);
        realtimeRates.put(createKey(Currency.US_DOLLAR, Currency.BRITISH_POUND), 0.79);

        realtimeRates.put(createKey(Currency.EURO, Currency.US_DOLLAR), 1.07);
        realtimeRates.put(createKey(Currency.EURO, Currency.JAPANESE_YEN), 169.26);
        realtimeRates.put(createKey(Currency.EURO, Currency.BRITISH_POUND), 0.84);

        realtimeRates.put(createKey(Currency.JAPANESE_YEN, Currency.US_DOLLAR), 0.0063);
        realtimeRates.put(createKey(Currency.JAPANESE_YEN, Currency.EURO), 0.0059);
        realtimeRates.put(createKey(Currency.JAPANESE_YEN, Currency.BRITISH_POUND), 0.0050);

        realtimeRates.put(createKey(Currency.BRITISH_POUND, Currency.US_DOLLAR), 1.27);
        realtimeRates.put(createKey(Currency.BRITISH_POUND, Currency.EURO), 1.18);
        realtimeRates.put(createKey(Currency.BRITISH_POUND, Currency.JAPANESE_YEN), 200.43);

        // Monthly exchange rates
        monthlyRates.put(createKey(Currency.US_DOLLAR, Currency.EURO), 0.92);
        monthlyRates.put(createKey(Currency.US_DOLLAR, Currency.JAPANESE_YEN), 158.74);
        monthlyRates.put(createKey(Currency.US_DOLLAR, Currency.BRITISH_POUND), 0.78);

        monthlyRates.put(createKey(Currency.EURO, Currency.US_DOLLAR), 1.08);
        monthlyRates.put(createKey(Currency.EURO, Currency.JAPANESE_YEN), 170.26);
        monthlyRates.put(createKey(Currency.EURO, Currency.BRITISH_POUND), 0.83);

        monthlyRates.put(createKey(Currency.JAPANESE_YEN, Currency.US_DOLLAR), 0.0062);
        monthlyRates.put(createKey(Currency.JAPANESE_YEN, Currency.EURO), 0.0058);
        monthlyRates.put(createKey(Currency.JAPANESE_YEN, Currency.BRITISH_POUND), 0.0049);

        monthlyRates.put(createKey(Currency.BRITISH_POUND, Currency.US_DOLLAR), 1.28);
        monthlyRates.put(createKey(Currency.BRITISH_POUND, Currency.EURO), 1.17);
        monthlyRates.put(createKey(Currency.BRITISH_POUND, Currency.JAPANESE_YEN), 201.43);

        // Daily exchange rates
        dailyRates.put(createKey(Currency.US_DOLLAR, Currency.EURO), 0.91);
        dailyRates.put(createKey(Currency.US_DOLLAR, Currency.JAPANESE_YEN), 159.74);
        dailyRates.put(createKey(Currency.US_DOLLAR, Currency.BRITISH_POUND), 0.77);

        dailyRates.put(createKey(Currency.EURO, Currency.US_DOLLAR), 1.09);
        dailyRates.put(createKey(Currency.EURO, Currency.JAPANESE_YEN), 171.26);
        dailyRates.put(createKey(Currency.EURO, Currency.BRITISH_POUND), 0.82);

        dailyRates.put(createKey(Currency.JAPANESE_YEN, Currency.US_DOLLAR), 0.0061);
        dailyRates.put(createKey(Currency.JAPANESE_YEN, Currency.EURO), 0.0057);
        dailyRates.put(createKey(Currency.JAPANESE_YEN, Currency.BRITISH_POUND), 0.0048);

        dailyRates.put(createKey(Currency.BRITISH_POUND, Currency.US_DOLLAR), 1.29);
        dailyRates.put(createKey(Currency.BRITISH_POUND, Currency.EURO), 1.16);
        dailyRates.put(createKey(Currency.BRITISH_POUND, Currency.JAPANESE_YEN), 202.43);
    }

    /**
     * Retrieves the fixed exchange rate from one currency to another based on the specified rate type.
     *
     * @param from            The currency to convert from.
     * @param to              The currency to convert to.
     * @param exchangeRateType The type of exchange rate (e.g., REALTIME, MONTHLY, DAILY).
     * @return The fixed exchange rate from currency 'from' to currency 'to' as a Double value.
     * @throws IllegalArgumentException If the exchange rate type is not supported or if no exchange rate is found.
     */
    @Override
    public Double getExchangeRate(Currency from, Currency to, ExchangeRateType exchangeRateType) {
        if (from.equals(to)) return 1.0;

        Double rate;
        String key = createKey(from, to);

        rate = switch (exchangeRateType) {
            case REALTIME -> realtimeRates.get(key);
            case MONTHLY -> monthlyRates.get(key);
            case DAILY -> dailyRates.get(key);
        };

        if (rate == null) {
            throw new IllegalArgumentException("Exchange rate not found for: " + from + " to " + to);
        }
        return rate;
    }

    /**
     * Creates a unique key for currency pair based on their ISO codes.
     *
     * @param from The currency to convert from.
     * @param to   The currency to convert to.
     * @return A unique key string representing the currency pair (e.g., USD_EUR).
     */
    private String createKey(Currency from, Currency to) {
        return from.getIsoCode() + "_" + to.getIsoCode();
    }
}
