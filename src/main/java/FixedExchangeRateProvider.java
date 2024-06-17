import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class FixedExchangeRateProvider implements ExchangeRateProvider {
    private final Map<String, Double> rates = new HashMap<>();

    public FixedExchangeRateProvider() {
        initializeRates();
    }

    private void initializeRates() {
        rates.put(createKey(Currency.US_DOLLAR, Currency.EURO), 0.93);
        rates.put(createKey(Currency.US_DOLLAR, Currency.JAPANESE_YEN), 157.74);
        rates.put(createKey(Currency.US_DOLLAR, Currency.BRITISH_POUND), 0.79);

        rates.put(createKey(Currency.EURO, Currency.US_DOLLAR), 1.07);
        rates.put(createKey(Currency.EURO, Currency.JAPANESE_YEN), 169.26);
        rates.put(createKey(Currency.EURO, Currency.BRITISH_POUND), 0.84);

        rates.put(createKey(Currency.JAPANESE_YEN, Currency.US_DOLLAR), 0.0063);
        rates.put(createKey(Currency.JAPANESE_YEN, Currency.EURO), 0.0059);
        rates.put(createKey(Currency.JAPANESE_YEN, Currency.BRITISH_POUND), 0.0050);

        rates.put(createKey(Currency.BRITISH_POUND, Currency.US_DOLLAR), 1.27);
        rates.put(createKey(Currency.BRITISH_POUND, Currency.EURO), 1.18);
        rates.put(createKey(Currency.BRITISH_POUND, Currency.JAPANESE_YEN), 200.43);
    }

    @Override
    public Double getExchangeRate(Currency from, Currency to) {
        Double rate = rates.get(createKey(from, to));
        if (rate == null) {
            throw new IllegalArgumentException("Exchange rate not found for: " + from + " to " + to);
        }
        return rate;
    }

    private String createKey(Currency from, Currency to) {
        return from.getIsoCode() + "_" + to.getIsoCode();
    }



}
