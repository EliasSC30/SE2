import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class MoneyValue implements MoneyValueClient {
    private BigDecimal amount;
    private final Currency currency;
    private static final Pattern PATTERN_WITH_CURRENCY_FIRST = Pattern.compile("([$€¥£]|USD|EUR|JPY|GBP)?\\s*([\\d.,]+)");
    private static final Pattern PATTERN_WITH_AMOUNT_FIRST = Pattern.compile("([\\d.,]+)\\s*([$€¥£]|USD|EUR|JPY|GBP)?");


    public MoneyValue(double v, Currency currency)  {
        this(new BigDecimal(v), currency);
    }
  
    public MoneyValue(long v, Currency currency)  {
        this(new BigDecimal(v), currency);
    }

    public MoneyValue(BigDecimal amount, Currency currency) {
        if (amount == null || currency == null) {
            throw new RuntimeException(ConstErrorMessages.INVALID_MONEY_VALUE_AS_STRING);
        }
        this.amount = amount.setScale(2, RoundingMode.HALF_UP);
        this.currency = currency;
    }
  
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

    private static int getLastNonDigitIndex(String amountStr) {
        for (int i = amountStr.length() - 1; i >= 0; i--) {
            if (!Character.isDigit(amountStr.charAt(i))) {
                return i;
            }
        }
        return -1;
    }

    private static int countChar(String str, char ch) {
        return (int) str.chars().filter(c -> c == ch).count();
    }

    public Currency getCurrency() {
        return this.currency;
    }

    public BigDecimal getAmount() {
        return this.amount;
    }

    @Override
    public String toString() {
        return CurrencyFormatter.formatCurrency(this);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof MoneyValue other)) return false;
        return this.amount.equals(other.amount) && this.currency.equals(other.currency);
    }
    @Override
    public int hashCode() {
        return Objects.hash(amount.doubleValue(), currency.getIsoCode());
    }

    public String toISOCode() {
        return CurrencyFormatter.formatISOCode(this);
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
            throw new RuntimeException(ConstErrorMessages.DIVIDE_BY_ZERO);
        }
        validateForOperation(this, other);
        setAmount(this.amount.divide(other.amount, 2, RoundingMode.HALF_UP));
        return this;
    }

    private static void validateForOperation(MoneyValue a, MoneyValue b) {
        if (a == null || b == null) {
            throw new RuntimeException(ConstErrorMessages.INVALID_MONEY_VALUE_AS_STRING);
        } 
        if(!a.getCurrency().equals(b.getCurrency())){
            throw new RuntimeException(ConstErrorMessages.CURRENCIES_NOT_EQUAL);
        }
    }

    private void setAmount(BigDecimal amount){
        if (amount == null) {
            throw new RuntimeException(ConstErrorMessages.AMOUNT_NULL);
        }
        this.amount = amount.setScale(2, RoundingMode.HALF_UP);
    }
}
