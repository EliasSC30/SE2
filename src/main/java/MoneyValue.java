import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class MoneyValue implements MoneyValueClient {
    private BigDecimal amount;
    private final Currency currency;
    public static final String INVALID_MONEY_VALUE_AS_STRING = "Invalid Money Value";
    public static final String INVALID_CONVERTER = "Invalid Converter";
  
    private static final Pattern PATTERN_WITH_CURRENCY_FIRST = Pattern.compile("([$€¥£]|USD|EUR|JPY|GBP)?\\s*([\\d.,]+)");
    private static final Pattern PATTERN_WITH_AMOUNT_FIRST = Pattern.compile("([\\d.,]+)\\s*([$€¥£]|USD|EUR|JPY|GBP)?");

    public static class InvalidMoneyValueException extends RuntimeException {
        public InvalidMoneyValueException(String message) {
            super(message);
        }
    }

    public MoneyValue(double v, Currency currency)  {
        this(new BigDecimal(v), currency);
    }
  
    public MoneyValue(long v, Currency currency)  {
        this(new BigDecimal(v), currency);
    }


    public MoneyValue(BigDecimal amount, Currency currency) {
        if (amount == null || currency == null) {
            throw new InvalidMoneyValueException(INVALID_MONEY_VALUE_AS_STRING);
        }
        this.amount = amount.setScale(2, RoundingMode.HALF_UP);
        this.currency = currency;
    }
  
    public MoneyValue(String str) {
        if (str == null || str.isEmpty()) {
            throw new InvalidMoneyValueException(INVALID_MONEY_VALUE_AS_STRING);
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
    @Override
    public int hashCode() {
        return Objects.hash(amount, currency);
    }

    public String toISOCode() {
        return amount + " " + currency.getIsoCode();
    }

    public String toISOCodePrefix() {
        return currency.getIsoCode() + " " + amount;
    }

    public int compareTo(MoneyValue other) {
        if(this == other) return 0;
        if(other == null) return -1;

        if(!this.amount.equals(other.getAmount()))
            return other.getAmount().subtract(this.amount).round(new MathContext(2, RoundingMode.UP)).intValue();

        return this.currency.compareTo(other.currency);
    }

    synchronized public MoneyValue add(MoneyValue other) {
        validateForOperation(this, other);
        setAmount(this.amount.add(other.amount));
        return this;
    }

    synchronized public MoneyValue subtract(MoneyValue other) {
        validateForOperation(this, other);
        setAmount(this.amount.subtract(other.amount));
        return this;
    }

    synchronized public MoneyValue multiply(MoneyValue other) {
        validateForOperation(this, other);
        setAmount(this.amount.multiply(other.amount));
        return this;
    }

    synchronized public MoneyValue divide(MoneyValue other) {
        if (other.amount.compareTo(BigDecimal.ZERO) == 0) {
            throw new InvalidMoneyValueException(INVALID_MONEY_VALUE_AS_STRING);
        }
        validateForOperation(this, other);
        setAmount(this.amount.divide(other.amount, 2, RoundingMode.HALF_UP));
        return this;
    }

    private static void validateForOperation(MoneyValue a, MoneyValue b) {
        if (a == null || b == null) {
            throw new InvalidMoneyValueException(INVALID_MONEY_VALUE_AS_STRING);
        } 
        if(!a.getCurrency().equals(b.getCurrency())){
            throw new InvalidMoneyValueException(INVALID_MONEY_VALUE_AS_STRING);
        }
    }

    private void setAmount(BigDecimal amount){
        if (amount == null) {
            throw new InvalidMoneyValueException(INVALID_MONEY_VALUE_AS_STRING);
        }
        this.amount = amount.setScale(2, RoundingMode.HALF_UP);
    }
}
