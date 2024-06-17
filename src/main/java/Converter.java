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


        Currency currency;
        if (currencyStr == null || currencyStr.isEmpty()) {
            throw new MoneyValue.InvalidMoneyValueException(MoneyValue.INVALID_MONEY_VALUE_AS_STRING);
        } else if (currencyStr.length() == 3) {
            currency = Currency.fromIsoCode(currencyStr);
        } else {
            char currencyChar = currencyStr.charAt(0);
            currency = Currency.fromSymbol(currencyChar);
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
}
