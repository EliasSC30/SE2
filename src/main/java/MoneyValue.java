import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MoneyValue {
    private BigDecimal amount;
    private final Currency currency;
    private Converter converter;
    public static final String INVALID_MONEY_VALUE_AS_STRING = "Invalid Money Value";
    public static final String INVALID_CONVERTER = "Invalid Converter";
  
    private static final Pattern PATTERN_WITH_CURRENCY_FIRST = Pattern.compile("([$€¥£]|USD|EUR|JPY|GBP)?\\s*([\\d.,]+)");
    private static final Pattern PATTERN_WITH_AMOUNT_FIRST = Pattern.compile("([\\d.,]+)\\s*([$€¥£]|USD|EUR|JPY|GBP)?");

    public Converter getConverter() {
        return converter;
    }

    synchronized public void setConverter(Converter converter) {
        if (converter == null){
            throw new InvalidMoneyValueException(INVALID_CONVERTER);
        }
        this.converter = converter;
    }

    public static class InvalidMoneyValueException extends RuntimeException {
        public InvalidMoneyValueException(String message) {
            super(message);
        }
    }
  
    
    public MoneyValue(double v, Currency currency, Converter converter) {
        this(new BigDecimal(v), currency, converter);
    }
  
    public MoneyValue(int v, Currency currency, Converter converter) {
        this(new BigDecimal(v), currency, converter);
    }



    public MoneyValue(BigDecimal amount, Currency currency, Converter converter) {
        if (amount == null || currency == null) {
            throw new InvalidMoneyValueException(INVALID_MONEY_VALUE_AS_STRING);
        }else if (converter == null){
            throw new InvalidMoneyValueException(INVALID_CONVERTER);
        }
        this.amount = amount.setScale(2, RoundingMode.HALF_UP);
        this.currency = currency;
        this.converter = converter;
    }
  
    public MoneyValue(String str, Converter converter) {
        if (str == null || str.isEmpty()) {
            throw new InvalidMoneyValueException(INVALID_MONEY_VALUE_AS_STRING);
        }else if (converter == null){
            throw new InvalidMoneyValueException(INVALID_CONVERTER);
        }

        // First ISO then amount
        Matcher matcher = PATTERN_WITH_CURRENCY_FIRST.matcher(str);
        String currencyStr;
        String amountStr;

        // Extracts currency and amount from the input string.
        if (!matcher.find() || matcher.group(1) == null || matcher.group(2) == null) {
            // First amount then ISO
            matcher = PATTERN_WITH_AMOUNT_FIRST.matcher(str);
            if (!matcher.find()) {
                throw new InvalidMoneyValueException(INVALID_MONEY_VALUE_AS_STRING);
            }
            currencyStr = matcher.group(2);
            amountStr = matcher.group(1);
        } else {
            currencyStr = matcher.group(1);
            amountStr = matcher.group(2);
        }

        // Convert String to currency
        if (currencyStr == null || currencyStr.isEmpty()) {
            throw new InvalidMoneyValueException(INVALID_MONEY_VALUE_AS_STRING);
        }
        Currency currency = currencyStr.length() == 3 ? Currency.fromIsoCode(currencyStr) : Currency.fromSymbol(currencyStr.charAt(0));

        // Convert String to amount
        BigDecimal unroundedAmount;
        try {
            unroundedAmount = new BigDecimal(amountStr.replace(",", ""));
        } catch (NumberFormatException e) {
            throw new InvalidMoneyValueException(INVALID_MONEY_VALUE_AS_STRING);
        }

        this.amount = unroundedAmount.setScale(2, RoundingMode.HALF_UP);
        this.currency = currency;
        this.converter = converter;
    }

    public Currency getCurrency() {
        return this.currency;
    }

    public BigDecimal getAmount() {
        return this.amount;
    }

    @Override
    public String toString() {
        String formattedAmount = this.amount.setScale(2, RoundingMode.HALF_UP).toString();
        if (currency == Currency.EURO) {
            return formattedAmount.replace(".", ",") + " " + currency.getSymbol();
        }
        return currency.getSymbol() + " " + formattedAmount;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof MoneyValue other)) return false;
        return this.amount.equals(other.amount) && this.currency.equals(other.currency);
    }

    public String toISOCode() {
        return amount + " " + currency.getIsoCode();
    }

    public String toISOCodePrefix() {
        return currency.getIsoCode() + " " + amount;
    }

    public int compareTo(MoneyValue other) {
        if (this == other) return 0;
        MoneyValue converted = converter.convertTo(other, this.currency);
        return this.amount.compareTo(converted.amount);
    }

    private void setAmount(BigDecimal amount){
        if (amount == null) {
            throw new InvalidMoneyValueException(INVALID_MONEY_VALUE_AS_STRING);
        }
        this.amount = amount.setScale(2, RoundingMode.HALF_UP);
    }

    synchronized public MoneyValue add(MoneyValue other) {
        validateForOperation(this, other);
        MoneyValue otherRightCurrency = converter.convertTo(other, this.currency);
        setAmount(this.amount.add(otherRightCurrency.amount));
        return this;
    }

    synchronized public MoneyValue subtract(MoneyValue other) {
        validateForOperation(this, other);
        MoneyValue otherRightCurrency = converter.convertTo(other, this.currency);
        setAmount(this.amount.subtract(otherRightCurrency.amount));
        return this;
    }

    synchronized public MoneyValue multiply(MoneyValue other) {
        validateForOperation(this, other);
        MoneyValue otherRightCurrency = converter.convertTo(other, this.currency);
        setAmount(this.amount.multiply(otherRightCurrency.amount));
        return this;
    }

    synchronized public MoneyValue divide(MoneyValue other) {
        if (other.amount.compareTo(BigDecimal.ZERO) == 0) {
            throw new InvalidMoneyValueException(INVALID_MONEY_VALUE_AS_STRING);
        }
        validateForOperation(this, other);
        MoneyValue otherRightCurrency = converter.convertTo(other, this.currency);
        setAmount(this.amount.divide(otherRightCurrency.amount, 2, RoundingMode.HALF_UP));
        return this;
    }

    private static void validateForOperation(MoneyValue a, MoneyValue b) {
        if (a == null || b == null) {
            throw new InvalidMoneyValueException(INVALID_MONEY_VALUE_AS_STRING);
        }
    }
}
