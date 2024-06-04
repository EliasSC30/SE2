import java.util.HashMap;
import java.util.Map;

public class Converter {
    public static final double INVALID_CONVERSION_FACTOR = -1.0;
    public static final Map<Character, MoneyValue.Currency> SYMBOL_TO_CURRENCY = new HashMap<>(Map.of(
            '$', MoneyValue.Currency.Dollar,
            '€', MoneyValue.Currency.Euro,
            '¥', MoneyValue.Currency.JapaneseYen,
            '£', MoneyValue.Currency.Pound
    ));
    public static final Map<MoneyValue.Currency, Character> CURRENCY_TO_SYMBOL = new HashMap<>(Map.of(
            MoneyValue.Currency.Dollar, '$',
            MoneyValue.Currency.Euro, '€',
            MoneyValue.Currency.JapaneseYen, '¥',
            MoneyValue.Currency.Pound, '£'
    ));
    public static final Map<String, MoneyValue.Currency> ISO_TO_CURRENCY = new HashMap<>(Map.of(
            "USD", MoneyValue.Currency.Dollar,
            "EUR", MoneyValue.Currency.Euro,
            "JPY", MoneyValue.Currency.JapaneseYen,
            "GBP", MoneyValue.Currency.Pound
    ));
    public static final Map<MoneyValue.Currency, String> CURRENCY_TO_ISO = new HashMap<>(Map.of(
             MoneyValue.Currency.Dollar, "USD",
             MoneyValue.Currency.Euro, "EUR",
             MoneyValue.Currency.JapaneseYen, "JPY",
             MoneyValue.Currency.Pound, "GBP"
    ));
    public static final Map<MoneyValue.Currency, Double> CONVERSION_TO_NEUTRAL = new HashMap<>(Map.of(
            MoneyValue.Currency.Dollar, 1.0,
            MoneyValue.Currency.Euro, 1.09,
            MoneyValue.Currency.JapaneseYen, 0.0064,
            MoneyValue.Currency.Pound, 1.28
    ));

    static double getConversionFactor(MoneyValue.Currency from, MoneyValue.Currency to)
    {
        if(from == null || from == MoneyValue.Currency.InvalidCurrency)
            return INVALID_CONVERSION_FACTOR;
        if(to == null || to == MoneyValue.Currency.InvalidCurrency)
            return INVALID_CONVERSION_FACTOR;

        double fromToDollar = CONVERSION_TO_NEUTRAL.get(from);
        double toToDollar = CONVERSION_TO_NEUTRAL.get(to);

        return fromToDollar / toToDollar;
    }

    public static MoneyValue convertToNeutral(MoneyValue mv)
    {
        if(mv == null || mv.getCurrency() == MoneyValue.Currency.InvalidCurrency)
            return MoneyValue.INVALID_MONEY_VALUE;

        double toNeutralFactor = CONVERSION_TO_NEUTRAL.get(mv.getCurrency());

        return new MoneyValue(roundTwoPlaces(mv.getAmount() * toNeutralFactor), MoneyValue.NEUTRAL_CURRENCY);
    }

    public static MoneyValue convertTo(MoneyValue mv, MoneyValue.Currency toCurrency)
    {
        if(mv == null || !mv.isValid() || toCurrency == MoneyValue.Currency.InvalidCurrency)
            return MoneyValue.INVALID_MONEY_VALUE;

        double toFactor = Converter.getConversionFactor(mv.getCurrency(), toCurrency);

        return new MoneyValue(roundTwoPlaces(mv.getAmount() * toFactor), toCurrency);
    }

    public static MoneyValue stringToMoneyValue(String str) {
        if(str == null || str.equals(""))
            return MoneyValueFactory.INVALID_MONEY_VALUE;

        int index = 0;
        int startOfDigitsIndex = nextNonWhiteSpaceIndex(str, index);
        int endOfDigitsIndex = nextWhiteSpaceIndex(str, index);

        double unroundedAmount = Double.parseDouble(str.substring(startOfDigitsIndex, endOfDigitsIndex));
        double amount = roundTwoPlaces(unroundedAmount);

        index = nextNonWhiteSpaceIndex(str, endOfDigitsIndex);

        char currencySymbol = str.charAt(index++);
        MoneyValue.Currency currency = symbolToCurrency(currencySymbol);
        if(currency == MoneyValue.Currency.InvalidCurrency)
            return MoneyValueFactory.INVALID_MONEY_VALUE;

        index = nextNonWhiteSpaceIndex(str, index);

        if(index + 1 <= str.length())
            return MoneyValueFactory.INVALID_MONEY_VALUE;

        return new MoneyValue(amount, currency);
    }

    public static double roundTwoPlaces(double amount)
    {
        return Math.round(amount * 100) / 100.0;
    }

    private static MoneyValue.Currency symbolToCurrency(Character symbol)
    {
        if(!SYMBOL_TO_CURRENCY.containsKey(symbol))
            return MoneyValue.Currency.InvalidCurrency;

        return SYMBOL_TO_CURRENCY.get(symbol);
    }

    private static int nextNonWhiteSpaceIndex(String str, int index)
    {
        while(index < str.length() && str.charAt(index) == ' ')
            ++index;
        return index;
    }

    private static int nextWhiteSpaceIndex(String str, int index)
    {
        while(index < str.length() && str.charAt(index) != ' ')
            ++index;
        return index;
    }
}
