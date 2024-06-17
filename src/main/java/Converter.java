import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    public static MoneyValue convertToNeutral(MoneyValue mv)
    {
        if(mv == null)
            throw new MoneyValue.InvalidMoneyValueException(MoneyValue.INVALID_MONEY_VALUE_AS_STRING);

        double toNeutralFactor = CONVERSION_TO_NEUTRAL.get(mv.getCurrency());

        return new MoneyValue(roundTwoPlaces(mv.getAmount() * toNeutralFactor), MoneyValue.NEUTRAL_CURRENCY);
    }

    public static MoneyValue convertTo(MoneyValue mv, Currency toCurrency)
    {
        if(mv == null || !mv.isValid())
            throw new MoneyValue.InvalidMoneyValueException(MoneyValue.INVALID_MONEY_VALUE_AS_STRING);

        if(toCurrency == null)
            throw new MoneyValue.InvalidMoneyValueException(MoneyValue.INVALID_MONEY_VALUE_AS_STRING);

        double toFactor = exchangeRateProvider.getExchangeRate(mv.getCurrency(), toCurrency);

        return new MoneyValue(roundTwoPlaces(mv.getAmount() * toFactor), toCurrency);
    }



    public static double roundTwoPlaces(double amount)
    {
        if(Double.isNaN(amount))
            return Double.NaN;

        return Math.round(amount * 100) / 100.0;
    }
}
