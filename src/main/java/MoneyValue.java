import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MoneyValue {
    private final double amount;
    private final Currency currency;
    public static final String INVALID_MONEY_VALUE_AS_STRING = "Invalid Money Value";

    public static class InvalidMoneyValueException extends RuntimeException {
        public InvalidMoneyValueException(String message) {
            super(message);
        }
    }

    MoneyValue(double amount, Currency currency)
    {
        if (Double.isNaN(amount) || !amountIsInRange(amount)) {
            throw new InvalidMoneyValueException(INVALID_MONEY_VALUE_AS_STRING);
        }
        if (currency == null) {
            throw new InvalidMoneyValueException(INVALID_MONEY_VALUE_AS_STRING);
        }
        this.amount = Converter.roundTwoPlaces(amount);
        this.currency = currency;
    }


     MoneyValue(String str) {
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

         this.amount = Converter.roundTwoPlaces(unroundedAmount);
         this.currency = currency;
    }

    public Currency getCurrency(){
        return this.currency;
    }
    public double getAmount(){
        return this.amount;
    }

    @Override
    public String toString()
    {
        Double roundedAmount = Converter.roundTwoPlaces(amount);
        String formattedAmount = String.format("%.2f", roundedAmount);

        if(currency == Currency.EURO){
            return formattedAmount.replace(".", ",") + " " + currency.getSymbol();
        } 

        return currency.getSymbol() + " " + formattedAmount;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == this)
            return true;
        if(!(obj instanceof MoneyValue other))
            return false;

        if(!this.isValid() && !other.isValid())
            return true;

        return this.compareTo(other) == 0;
    }

    public String toISOCode()
    {
        return Converter.roundTwoPlaces(amount) + " " + currency.getIsoCode();
    }

    public String toISOCodePrefix()
    {
        return currency.getIsoCode() + " " + Converter.roundTwoPlaces(amount);
    }

    public int compareTo(MoneyValue other)
    {
        if(this == other)
            return 0;

        MoneyValue converted = Converter.convertTo(other, this.currency);

        return (int)((100.0 * ((this).amount - converted.getAmount())) );
    }

    public boolean isValid()
    {
        return !Double.isNaN(this.amount);
    }

    public MoneyValue convertTo(Currency toCurrency)
    {
        return Converter.convertTo(this, toCurrency);
    }

    public MoneyValue add(MoneyValue other, Currency toCurrency)
    {
        if(atLeastOneIsInvalid(this, other))
            throw new InvalidMoneyValueException(INVALID_MONEY_VALUE_AS_STRING);

        MoneyValue thisNeutral = Converter.convertTo(this, toCurrency);
        MoneyValue otherNeutral = Converter.convertTo(other, toCurrency);

        return new MoneyValue(thisNeutral.amount + otherNeutral.amount, toCurrency);
    }

    public MoneyValue subtract(MoneyValue other, Currency toCurrency)
    {
        if(atLeastOneIsInvalid(this, other))
            throw new InvalidMoneyValueException(INVALID_MONEY_VALUE_AS_STRING);

        MoneyValue thisNeutral = Converter.convertTo(this, toCurrency);
        MoneyValue otherNeutral = Converter.convertTo(other, toCurrency);

        return new MoneyValue(thisNeutral.getAmount() - otherNeutral.getAmount(), toCurrency);
    }

    public MoneyValue multiply(MoneyValue other, Currency toCurrency)
    {
        if(atLeastOneIsInvalid(this, other))
            throw new InvalidMoneyValueException(INVALID_MONEY_VALUE_AS_STRING);

        MoneyValue thisNeutral = Converter.convertTo(this, toCurrency);
        MoneyValue otherNeutral = Converter.convertTo(other, toCurrency);

        return new MoneyValue(thisNeutral.getAmount() * otherNeutral.getAmount(), toCurrency);
    }

    public MoneyValue divide(MoneyValue other, Currency toCurrency)
    {
        if(atLeastOneIsInvalid(this, other) || other.getAmount() == 0.0)
            throw new InvalidMoneyValueException(INVALID_MONEY_VALUE_AS_STRING);

        MoneyValue thisNeutral = Converter.convertTo(this, toCurrency);
        MoneyValue otherNeutral = Converter.convertTo(other, toCurrency);

        return new MoneyValue(Converter.roundTwoPlaces(thisNeutral.getAmount() / otherNeutral.getAmount()), toCurrency);
    }

    private static boolean atLeastOneIsInvalid(MoneyValue a, MoneyValue b)
    {
        return a == null || b == null || !a.isValid() || !b.isValid();
    }

    // Double precision has a relative error less than 10^-(15). Thus
    // values up to 10^(13) will be rounded correctly, up to two places.
    private static boolean amountIsInRange(double amount)
    {
        return (!Double.isNaN(amount) && Math.abs(amount) < 1e13);
    }

}
