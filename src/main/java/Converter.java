import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class Converter {
    private static final ExchangeRateProvider exchangeRateProvider = new FixedExchangeRateProvider();

    public static final Map<Currency, Double> CONVERSION_TO_NEUTRAL = new HashMap<>(Map.of(
            Currency.US_DOLLAR, 1.0,
            Currency.EURO, 1.09,
            Currency.JAPANESE_YEN, 0.0064,
            Currency.BRITISH_POUND, 1.28
    ));

    public static class InvalidConversionException extends RuntimeException {
        public InvalidConversionException(String message) {
            super(message);
        }
    }

    public static MoneyValue convertTo(MoneyValue mv, Currency toCurrency)
    {
        if(mv == null)
            throw new MoneyValue.InvalidMoneyValueException(MoneyValue.INVALID_MONEY_VALUE_AS_STRING);

        if(toCurrency == null)
            throw new MoneyValue.InvalidMoneyValueException(MoneyValue.INVALID_MONEY_VALUE_AS_STRING);

        BigDecimal toFactor = BigDecimal.valueOf(exchangeRateProvider.getExchangeRate(mv.getCurrency(), toCurrency));

        return new MoneyValue(mv.getAmount().multiply(toFactor), toCurrency);
    }
}
