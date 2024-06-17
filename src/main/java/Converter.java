import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Converter {
    public static final Map<Character, MoneyValue.Currency> SYMBOL_TO_CURRENCY = new HashMap<>(Map.of(
            '$', MoneyValue.Currency.US_DOLLAR,
            '€', MoneyValue.Currency.EURO,
            '¥', MoneyValue.Currency.JAPANESE_YEN,
            '£', MoneyValue.Currency.BRITISH_POUND
    ));
    public static final Map<MoneyValue.Currency, Character> CURRENCY_TO_SYMBOL = new HashMap<>(Map.of(
            MoneyValue.Currency.US_DOLLAR, '$',
            MoneyValue.Currency.EURO, '€',
            MoneyValue.Currency.JAPANESE_YEN, '¥',
            MoneyValue.Currency.BRITISH_POUND, '£'
    ));
    public static final Map<String, MoneyValue.Currency> ISO_TO_CURRENCY = new HashMap<>(Map.of(
            "USD", MoneyValue.Currency.US_DOLLAR,
            "EUR", MoneyValue.Currency.EURO,
            "JPY", MoneyValue.Currency.JAPANESE_YEN,
            "GBP", MoneyValue.Currency.BRITISH_POUND
    ));
    public static final Map<MoneyValue.Currency, String> CURRENCY_TO_ISO = new HashMap<>(Map.of(
             MoneyValue.Currency.US_DOLLAR, "USD",
             MoneyValue.Currency.EURO, "EUR",
             MoneyValue.Currency.JAPANESE_YEN, "JPY",
             MoneyValue.Currency.BRITISH_POUND, "GBP"
    ));
    public static final Map<MoneyValue.Currency, Double> CONVERSION_TO_NEUTRAL = new HashMap<>(Map.of(
            MoneyValue.Currency.US_DOLLAR, 1.0,
            MoneyValue.Currency.EURO, 1.09,
            MoneyValue.Currency.JAPANESE_YEN, 0.0064,
            MoneyValue.Currency.BRITISH_POUND, 1.28
    ));

    public static class InvalidConversionException extends RuntimeException {
        public InvalidConversionException(String message) {
            super(message);
        }
    }

    static double getConversionFactor(MoneyValue.Currency from, MoneyValue.Currency to)
    {
        if (from == null ) {
            throw new InvalidConversionException("Invalid conversion factor: 'from' currency is invalid");
        }
        if (to == null) {
            throw new InvalidConversionException("Invalid conversion factor: 'to' currency is invalid");
        }

        double fromToDollar = CONVERSION_TO_NEUTRAL.get(from);
        double toToDollar = CONVERSION_TO_NEUTRAL.get(to);

        return fromToDollar / toToDollar;
    }

    public static MoneyValue convertToNeutral(MoneyValue mv)
    {
        if(mv == null)
            throw new MoneyValue.InvalidMoneyValueException(MoneyValue.INVALID_MONEY_VALUE_AS_STRING);

        double toNeutralFactor = CONVERSION_TO_NEUTRAL.get(mv.getCurrency());

        return new MoneyValue(roundTwoPlaces(mv.getAmount() * toNeutralFactor), MoneyValue.NEUTRAL_CURRENCY);
    }

    public static MoneyValue convertTo(MoneyValue mv, MoneyValue.Currency toCurrency)
    {
        if(mv == null || !mv.isValid())
            throw new MoneyValue.InvalidMoneyValueException(MoneyValue.INVALID_MONEY_VALUE_AS_STRING);

        if(toCurrency == null)
            throw new MoneyValue.InvalidMoneyValueException(MoneyValue.INVALID_MONEY_VALUE_AS_STRING);

        double toFactor = Converter.getConversionFactor(mv.getCurrency(), toCurrency);

        return new MoneyValue(roundTwoPlaces(mv.getAmount() * toFactor), toCurrency);
    }


    public static MoneyValue stringToMoneyValue(String str) {
        if(str == null || str.equals(""))
            throw new MoneyValue.InvalidMoneyValueException(MoneyValue.INVALID_MONEY_VALUE_AS_STRING);

        Pattern pattern = Pattern.compile("([\\$€¥£]|USD|EUR|JPY|GBP)?\\s*([\\d.,]+)");
        Matcher matcher = pattern.matcher(str);

        String currencyStr;
        String amountStr;

        if (!matcher.find() || matcher.group(1) == null || matcher.group(2) == null) {
            pattern = Pattern.compile("([\\d.,]+)\\s*([\\$€¥£]|USD|EUR|JPY|GBP)?");
            matcher = pattern.matcher(str);

            if (!matcher.find()) {
                throw new MoneyValue.InvalidMoneyValueException(MoneyValue.INVALID_MONEY_VALUE_AS_STRING);
            }

            currencyStr = matcher.group(2);
            amountStr = matcher.group(1);
        }else{
            currencyStr = matcher.group(1);
            amountStr = matcher.group(2);
        }


        MoneyValue.Currency currency;
        if (currencyStr == null || currencyStr.isEmpty()) {
            throw new MoneyValue.InvalidMoneyValueException(MoneyValue.INVALID_MONEY_VALUE_AS_STRING);
        } else if (currencyStr.length() == 3) {
            currency = isoToCurrency(currencyStr);
        } else {
            char currencyChar = currencyStr.charAt(0);
            currency = symbolToCurrency(currencyChar);
        }


        double unroundedAmount;
        try {
             unroundedAmount = Double.parseDouble(amountStr);
        } catch (NumberFormatException e)
        {
            throw new MoneyValue.InvalidMoneyValueException(MoneyValue.INVALID_MONEY_VALUE_AS_STRING);
        }

        return new MoneyValue(roundTwoPlaces(unroundedAmount), currency);
    }



    public static double roundTwoPlaces(double amount)
    {
        if(Double.isNaN(amount))
            return Double.NaN;

        return Math.round(amount * 100) / 100.0;
    }

    private static MoneyValue.Currency symbolToCurrency(Character symbol)
    {
        if(!SYMBOL_TO_CURRENCY.containsKey(symbol))
            throw new MoneyValue.InvalidMoneyValueException("");

        return SYMBOL_TO_CURRENCY.get(symbol);
    }

    private static MoneyValue.Currency isoToCurrency(String iso)
    {
        if(!ISO_TO_CURRENCY.containsKey(iso))
            throw new MoneyValue.InvalidMoneyValueException("");

        return ISO_TO_CURRENCY.get(iso);
    }
}
