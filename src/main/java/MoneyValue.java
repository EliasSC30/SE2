import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Represents a monetary value with a specific currency.
 */
public final class MoneyValue implements MoneyValueClient {

    private BigDecimal amount;
    private final Currency currency;
    public static final String INVALID_MONEY_VALUE_AS_STRING = "Invalid Money Value";

    private static final Pattern PATTERN_WITH_CURRENCY_FIRST = Pattern.compile("([$€¥£]|USD|EUR|JPY|GBP)?\\s*([\\d.,]+)");
    private static final Pattern PATTERN_WITH_AMOUNT_FIRST = Pattern.compile("([\\d.,]+)\\s*([$€¥£]|USD|EUR|JPY|GBP)?");

    /**
     * Exception thrown when the provided money value string is invalid.
     */
    public static class InvalidMoneyValueException extends RuntimeException {
        public InvalidMoneyValueException(String message) {
            super(message);
        }
    }

    /**
     * Constructs a MoneyValue object with a double amount and specified currency.
     *
     * @param v        The amount as a double.
     * @param currency The currency of the money value.
     */
    public MoneyValue(double v, Currency currency) {
        this(new BigDecimal(v), currency);
    }

    /**
     * Constructs a MoneyValue object with a long amount and specified currency.
     *
     * @param v        The amount as a long.
     * @param currency The currency of the money value.
     */
    public MoneyValue(long v, Currency currency) {
        this(new BigDecimal(v), currency);
    }

    /**
     * Constructs a MoneyValue object with a BigDecimal amount and specified currency.
     *
     * @param amount   The amount as a BigDecimal.
     * @param currency The currency of the money value.
     */
    public MoneyValue(BigDecimal amount, Currency currency) {
        if (amount == null || currency == null) {
            throw new InvalidMoneyValueException(INVALID_MONEY_VALUE_AS_STRING);
        }
        this.amount = amount.setScale(2, RoundingMode.HALF_UP);
        this.currency = currency;
    }

    /**
     * Constructs a MoneyValue object from a string representation.
     * Parses the string to extract currency and amount.
     *
     * @param str The string representation of the money value.
     * @throws InvalidMoneyValueException If the string format is invalid or parsing fails.
     */
    public MoneyValue(String str) {
        if (str == null || str.isEmpty()) {
            throw new InvalidMoneyValueException(INVALID_MONEY_VALUE_AS_STRING);
        }

        Matcher matcher = PATTERN_WITH_CURRENCY_FIRST.matcher(str);
        String currencyStr;
        String amountStr;

        if (!matcher.find() || matcher.group(1) == null || matcher.group(2) == null) {
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

        if (currencyStr == null || currencyStr.isEmpty()) {
            throw new InvalidMoneyValueException(INVALID_MONEY_VALUE_AS_STRING);
        }
        Currency currency = currencyStr.length() == 3 ? Currency.fromIsoCode(currencyStr) : Currency.fromSymbol(currencyStr.charAt(0));

        BigDecimal unroundedAmount;
        try {
            unroundedAmount = new BigDecimal(amountStr.replace(",", ""));
        } catch (NumberFormatException e) {
            throw new InvalidMoneyValueException(INVALID_MONEY_VALUE_AS_STRING);
        }

        this.amount = unroundedAmount.setScale(2, RoundingMode.HALF_UP);
        this.currency = currency;
    }

    /**
     * Retrieves the currency of this money value.
     *
     * @return The currency of the money value.
     */
    public Currency getCurrency() {
        return this.currency;
    }

    /**
     * Retrieves the amount of this money value.
     *
     * @return The amount of the money value as a BigDecimal.
     */
    public BigDecimal getAmount() {
        return this.amount;
    }

    /**
     * Converts this MoneyValue object to a string representation.
     *
     * @return A string representation of the MoneyValue object.
     */
    @Override
    public String toString() {
        String formattedAmount = this.amount.setScale(2, RoundingMode.HALF_UP).toString();
        if (currency == Currency.EURO) {
            return formattedAmount.replace(".", ",") + " " + currency.getSymbol();
        }
        return currency.getSymbol() + " " + formattedAmount;
    }

    /**
     * Checks if this MoneyValue object is equal to another object.
     *
     * @param obj The object to compare with.
     * @return true if the objects are equal, false otherwise.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof MoneyValue other)) return false;
        return this.amount.equals(other.amount) && this.currency.equals(other.currency);
    }

    /**
     * Computes the hash code of this MoneyValue object.
     *
     * @return The hash code of the MoneyValue object.
     */
    @Override
    public int hashCode() {
        return Objects.hash(amount, currency);
    }

    /**
     * Converts this MoneyValue object to a string representation with ISO code prefix.
     *
     * @return A string representation of the MoneyValue object with ISO code prefix.
     */
    public String toISOCodePrefix() {
        return currency.getIsoCode() + " " + amount;
    }

    /**
     * Compares this MoneyValue object with another MoneyValue object.
     *
     * @param other The MoneyValue object to compare with.
     * @return A negative integer, zero, or a positive integer as this MoneyValue is less than, equal to, or greater than the specified MoneyValue.
     */
    public int compareTo(MoneyValue other) {
        if (this == other) return 0;
        if (other == null) return -1;

        if (!this.amount.equals(other.getAmount()))
            return other.getAmount().subtract(this.amount).round(new MathContext(2, RoundingMode.UP)).intValue();

        return this.currency.compareTo(other.currency);
    }

    /**
     * Adds another MoneyValue to this MoneyValue object.
     *
     * @param other The MoneyValue to add.
     * @return The resulting MoneyValue after addition.
     * @throws InvalidMoneyValueException If the currencies are different or if either MoneyValue is null.
     */
    synchronized public MoneyValue add(MoneyValue other) {
        validateForOperation(this, other);
        setAmount(this.amount.add(other.amount));
        return this;
    }

    /**
     * Subtracts another MoneyValue from this MoneyValue object.
     *
     * @param other The MoneyValue to subtract.
     * @return The resulting MoneyValue after subtraction.
     * @throws InvalidMoneyValueException If the currencies are different or if either MoneyValue is null.
     */
    synchronized public MoneyValue subtract(MoneyValue other) {
        validateForOperation(this, other);
        setAmount(this.amount.subtract(other.amount));
        return this;
    }

    /**
     * Multiplies this MoneyValue object by another MoneyValue.
     *
     * @param other The MoneyValue to multiply by.
     * @return The resulting MoneyValue after multiplication.
     * @throws InvalidMoneyValueException If the currencies are different or if either MoneyValue is null.
     */
    synchronized public MoneyValue multiply(MoneyValue other) {
        validateForOperation(this, other);
        setAmount(this.amount.multiply(other.amount));
        return this;
    }

    /**
     * Divides this MoneyValue object by another MoneyValue.
     *
     * @param other The MoneyValue to divide by.
     * @return The resulting MoneyValue after division.
     * @throws InvalidMoneyValueException If the currencies are different, if either MoneyValue is null, or if the divisor MoneyValue is zero.
     */
    synchronized public MoneyValue divide(MoneyValue other) {
        if (other.amount.compareTo(BigDecimal.ZERO) == 0) {
            throw new InvalidMoneyValueException(INVALID_MONEY_VALUE_AS_STRING);
        }
        validateForOperation(this, other);
        setAmount(this.amount.divide(other.amount, 2, RoundingMode.HALF_UP));
        return this;
    }

    /**
     * Validates that two MoneyValue objects have the same currency.
     *
     * @param a The first MoneyValue.
     * @param b The second MoneyValue.
     * @throws InvalidMoneyValueException If either MoneyValue is null or if their currencies are different.
     */
    private static void validateForOperation(MoneyValue a, MoneyValue b) {
        if (a == null || b == null) {
            throw new InvalidMoneyValueException(INVALID_MONEY_VALUE_AS_STRING);
        }
        if (!a.getCurrency().equals(b.getCurrency())) {
            throw new InvalidMoneyValueException(INVALID_MONEY_VALUE_AS_STRING);
        }
    }

    /**
     * Sets the amount of this MoneyValue object.
     *
     * @param amount The new amount to set.
     * @throws InvalidMoneyValueException If the amount is null.
     */
    private void setAmount(BigDecimal amount) {
        if (amount == null) {
            throw new InvalidMoneyValueException(INVALID_MONEY_VALUE_AS_STRING);
        }
        this.amount = amount.setScale(2, RoundingMode.HALF_UP);
    }
}
