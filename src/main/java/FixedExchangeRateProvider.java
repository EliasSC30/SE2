import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class FixedExchangeRateProvider implements ExchangeRateProvider {
    private final Map<String, Double> realtimeRates = new HashMap<>();
    private final Map<String, Double> monthlyRates = new HashMap<>();
    private final Map<String, Double> dailyRates = new HashMap<>();

    public FixedExchangeRateProvider() {
        initializeRates();
    }

    private void initializeRates() {
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

    @Override
    public Double getExchangeRate(Currency from, Currency to, ExchangeRateType exchangeRateType) {
        if (from == null)
            throw new RuntimeException(ConstErrorMessages.CURRENCY_NULL);
        else if (to == null)
            throw new RuntimeException(ConstErrorMessages.CURRENCY_NULL);
        else if (exchangeRateType == null)
            throw new RuntimeException(ConstErrorMessages.EXCHANGE_RATE_TYPE_NULL);

        if(from.equals(to)) return 1.0;

        Double rate = null;
        String key = createKey(from, to);

        rate = switch (exchangeRateType) {
            case REALTIME -> realtimeRates.get(key);
            case MONTHLY -> monthlyRates.get(key);
            case DAILY -> dailyRates.get(key);
            default -> throw new IllegalArgumentException("Exchange rate type not supported: " + exchangeRateType);
        };

        if (rate == null) {
            throw new IllegalArgumentException("Exchange rate not found for: " + from + " to " + to);
        }
        return rate;
    }

    private String createKey(Currency from, Currency to) {
        return from.getIsoCode() + "_" + to.getIsoCode();
    }



}
