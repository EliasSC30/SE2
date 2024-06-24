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
    private static final Pattern PATTERN_WITH_CURRENCY_FIRST = Pattern.compile("([$€¥£]|USD|EUR|JPY|GBP)?\\s*([\\d.,]+)");
    private static final Pattern PATTERN_WITH_AMOUNT_FIRST = Pattern.compile("([\\d.,]+)\\s*([$€¥£]|USD|EUR|JPY|GBP)?");

    /**
     * Constructs a MoneyValue object with a double amount and specified currency.
     *
     * @param v        The amount as a double.
     * @param currency The currency of the money value.
     */
    public MoneyValue(double v, Currency currency)  {
        this(new BigDecimal(v), currency);
    }

    /**
     * Constructs a MoneyValue object with a long amount and specified currency.
     *
     * @param v        The amount as a long.
     * @param currency The currency of the money value.
     */
    public MoneyValue(long v, Currency currency)  {
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
            throw new RuntimeException(ConstErrorMessages.INVALID_MONEY_VALUE_AS_STRING);
        }
        this.amount = amount.setScale(2, RoundingMode.HALF_UP);
        this.currency = currency;
    }

    /**
     * Constructs a MoneyValue object from a string representation.
     * Parses the string to extract currency and amount.
     *
     * @param str The string representation of the money value.
     * @throws RuntimeException If the string format is invalid or parsing fails.
     */
    public MoneyValue(String str) {
        if (str == null || str.isEmpty())
            throw new RuntimeException(ConstErrorMessages.INVALID_MONEY_VALUE_AS_STRING);

        // First ISO then amount
        Matcher matcher = PATTERN_WITH_CURRENCY_FIRST.matcher(str);
        String currencyStr;
        String amountStr;

        // Extracts currency and amount from the input string.
        if (!matcher.find() || matcher.group(1) == null || matcher.group(2) == null) {
            // First amount then ISO
            matcher = PATTERN_WITH_AMOUNT_FIRST.matcher(str);
            if (!matcher.find()) {
                throw new RuntimeException(ConstErrorMessages.INVALID_MONEY_VALUE_AS_STRING);
            }
            currencyStr = matcher.group(2);
            amountStr = matcher.group(1);
        } else {
            currencyStr = matcher.group(1);
            amountStr = matcher.group(2);
        }

        // Convert String to currency
        if (currencyStr == null || currencyStr.isEmpty()) {
            throw new RuntimeException(ConstErrorMessages.INVALID_MONEY_VALUE_AS_STRING);
        }
        Currency currency = currencyStr.length() == 3 ? Currency.fromIsoCode(currencyStr) : Currency.fromSymbol(currencyStr.charAt(0));

        BigDecimal unroundedAmount;
        try {
            String cleanedAmount = cleanAmount(amountStr);
            String rightFormatAmount = cleanedAmount.replace(",", ".");
            unroundedAmount = new BigDecimal(rightFormatAmount);
        } catch (NumberFormatException e) {
            throw new RuntimeException(ConstErrorMessages.INVALID_MONEY_VALUE_AS_STRING);
        }

        this.amount = unroundedAmount.setScale(2, RoundingMode.HALF_UP);
        this.currency = currency;
    }

    /**
     * Cleans the given amount string by removing invalid characters and ensuring correct decimal format.
     *
     * @param amountStr The amount string to clean.
     * @return The cleaned amount string.
     */
    private static String cleanAmount(String amountStr) {
        String cleanedAmount = amountStr.replaceAll("[^0-9,.]", "");
        int dotCount = countChar(amountStr, '.');
        int commaCount = countChar(amountStr, ',');

        if (dotCount == 0 && commaCount == 0) {
            return cleanedAmount;
        }

        if (dotCount > 1) {
            cleanedAmount = cleanedAmount.replace(".", "");
            return cleanedAmount;
        } else if (commaCount > 1) {
            cleanedAmount = cleanedAmount.replace(",", "");
            return cleanedAmount;
        }

        int lastNonDigitIndex = getLastNonDigitIndex(amountStr);
        if (commaCount == 1 && dotCount == 1 && lastNonDigitIndex != -1) {
            char lastNonDigit = amountStr.charAt(lastNonDigitIndex);
            if (lastNonDigit == ',') {
                cleanedAmount = cleanedAmount.replace(".", "");
            } else if (lastNonDigit == '.') {
                cleanedAmount = cleanedAmount.replace(",", "");
            }
            return cleanedAmount;
        }

        if (dotCount == 1) {
            int dotIndex = amountStr.indexOf('.');
            if (amountStr.length() - dotIndex > 3) {
                cleanedAmount = cleanedAmount.replace(".", "");
            }
        } else if (commaCount == 1) {
            int commaIndex = amountStr.indexOf(',');
            if (amountStr.length() - commaIndex > 3) {
                cleanedAmount = cleanedAmount.replace(",", "");
            }
        }

        return cleanedAmount;
    }

    /**
     * Gets the index of the last non-digit character in the string.
     *
     * @param amountStr The amount string.
     * @return The index of the last non-digit character, or -1 if none found.
     */
    private static int getLastNonDigitIndex(String amountStr) {
        for (int i = amountStr.length() - 1; i >= 0; i--) {
            if (!Character.isDigit(amountStr.charAt(i))) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Counts the occurrences of a character in a string.
     *
     * @param str The string to search.
     * @param ch  The character to count.
     * @return The number of occurrences of the character.
     */
    private static int countChar(String str, char ch) {
        return (int) str.chars().filter(c -> c == ch).count();
    }

    /**
     * Returns the currency of this MoneyValue.
     *
     * @return The currency.
     */
    public Currency getCurrency() {
        return this.currency;
    }

    /**
     * Returns the amount of this MoneyValue.
     *
     * @return The amount.
     */
    public BigDecimal getAmount() {
        return this.amount;
    }

    /**
     * Returns a string representation of the money value formatted according to the currency.
     *
     * @return A string representation of the money value.
     */
    @Override
    public String toString() {
        return CurrencyFormatter.formatCurrency(this);
    }

    /**
     * Returns a string representation of the amount.
     *
     * @return The amount as a string.
     */
    public String toStringAmount() {
        return amount.toString();
    }

    /**
     * Returns the currency symbol of this MoneyValue.
     *
     * @return The currency symbol.
     */
    public String toStringCurrencySymbol() {
        return currency.getSymbol();
    }

    /**
     * Returns the ISO code of the currency of this MoneyValue.
     *
     * @return The ISO code.
     */
    public String toStringCurrencyISOCode() {
        return currency.getIsoCode();
    }

    /**
     * Checks if this MoneyValue is equal to another object.
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
     * Returns the hash code of this MoneyValue.
     *
     * @return The hash code.
     */
    @Override
    public int hashCode() {
        return Objects.hash(amount.doubleValue(), currency.getIsoCode());
    }

    /**
     * Returns the ISO code representation of this MoneyValue.
     *
     * @return The ISO code as a string.
     */
    public String toISOCode() {
        return CurrencyFormatter.formatISOCode(this);
    }

    /**
     * Compares this MoneyValue with another MoneyValue.
     *
     * @param other The MoneyValue to compare with.
     * @return A negative integer, zero, or a positive integer as this MoneyValue is less than, equal to, or greater than the specified MoneyValue.
     */
    public int compareTo(MoneyValue other) {
        if(this == other) return 0;
        if(other == null) return -1;

        if(!this.amount.equals(other.getAmount()))
            return other.getAmount().subtract(this.amount).round(new MathContext(2, RoundingMode.UP)).intValue();

        return this.currency.compareTo(other.currency);
    }

    /**
     * Adds another MoneyValue to this MoneyValue.
     *
     * @param other The MoneyValue to add.
     * @return The updated MoneyValue.
     */
    synchronized public MoneyValue add(MoneyValue other) {
        validateForOperation(this, other);
        setAmount(this.amount.add(other.amount));
        return this;
    }

    /**
     * Subtracts another MoneyValue from this MoneyValue.
     *
     * @param other The MoneyValue to subtract.
     * @return The updated MoneyValue.
     */
    synchronized public MoneyValue subtract(MoneyValue other) {
        validateForOperation(this, other);
        setAmount(this.amount.subtract(other.amount));
        return this;
    }

    /**
     * Multiplies this MoneyValue by another MoneyValue.
     *
     * @param other The MoneyValue to multiply by.
     * @return The updated MoneyValue.
     */
    synchronized public MoneyValue multiply(MoneyValue other) {
        validateForOperation(this, other);
        setAmount(this.amount.multiply(other.amount));
        return this;
    }

    /**
     * Divides this MoneyValue by another MoneyValue.
     *
     * @param other The MoneyValue to divide by.
     * @return The updated MoneyValue.
     * @throws RuntimeException If the other MoneyValue amount is zero.
     */
    synchronized public MoneyValue divide(MoneyValue other) {
        if (other.amount.compareTo(BigDecimal.ZERO) == 0) {
            throw new RuntimeException(ConstErrorMessages.DIVIDE_BY_ZERO);
        }
        validateForOperation(this, other);
        setAmount(this.amount.divide(other.amount, 2, RoundingMode.HALF_UP));
        return this;
    }

    /**
     * Validates if two MoneyValue objects can be used in arithmetic operations.
     *
     * @param a The first MoneyValue.
     * @param b The second MoneyValue.
     * @throws RuntimeException If any of the MoneyValue objects are null or their currencies do not match.
     */
    private static void validateForOperation(MoneyValue a, MoneyValue b) {
        if (a == null || b == null) {
            throw new RuntimeException(ConstErrorMessages.INVALID_MONEY_VALUE_AS_STRING);
        }
        if(!a.getCurrency().equals(b.getCurrency())){
            throw new RuntimeException(ConstErrorMessages.CURRENCIES_NOT_EQUAL);
        }
    }

    /**
     * Sets the amount of this MoneyValue.
     *
     * @param amount The amount to set.
     * @throws RuntimeException If the amount is null.
     */
    private void setAmount(BigDecimal amount){
        if (amount == null) {
            throw new RuntimeException(ConstErrorMessages.AMOUNT_NULL);
        }
        this.amount = amount.setScale(2, RoundingMode.HALF_UP);
    }
}
