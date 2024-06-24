import java.math.BigDecimal;

/**
 * The Calculator class provides methods to perform arithmetic operations on MoneyValue objects.
 * It supports addition, subtraction, multiplication, and division of MoneyValue instances.
 * The class ensures that the operations are performed in the same currency, using a ConverterClient
 * if necessary to handle currency conversions.
 */
public class Calculator {
    private MoneyValueClient mv_;
    private ConverterClient cv_;

    /**
     * Constructs a Calculator with the specified MoneyValue and Converter clients.
     * The MoneyValue client represents the current monetary value and currency,
     * while the Converter client is used to convert between different currencies if needed.
     *
     * @param mv the MoneyValue client representing the current monetary value
     * @param cv the Converter client used for currency conversion
     * @throws RuntimeException if the MoneyValue or Converter clients are null
     */
    public Calculator(MoneyValue mv, Converter cv) {
        if (mv == null)
            throw new RuntimeException(ConstErrorMessages.MONEY_VALUE_NULL);
        else if (cv == null)
            throw new RuntimeException(ConstErrorMessages.CONVERTER_NULL);

        this.mv_ = mv;
        this.cv_ = cv;
    }

    /**
     * Returns the current MoneyValue client.
     * This client contains the monetary value and its associated currency.
     *
     * @return the MoneyValue client representing the current monetary value
     */
    public MoneyValueClient getMoneyValueClient() { return this.mv_; }

    /**
     * Returns the Converter client.
     * The Converter client is responsible for converting monetary values between different currencies.
     *
     * @return the Converter client used for currency conversion
     */
    public ConverterClient getConverterClient() { return this.cv_; }
    synchronized public void setMoneyValue(MoneyValue mv) {
        if(mv == null)
            throw new RuntimeException(ConstErrorMessages.MONEY_VALUE_NULL);
        this.mv_ = mv;
    }

    /**
     * Sets the Converter client to the specified value.
     * The Converter client should not be null, as it is responsible for converting monetary values
     * between different currencies. If a null value is provided, a RuntimeException is thrown.
     *
     * @param cv the Converter client to set
     * @throws RuntimeException if the provided Converter client is null
     */
     synchronized public void setConverter(Converter cv) {
        if(cv == null)
            throw new RuntimeException(ConstErrorMessages.CONVERTER_NULL);
        this.cv_ = cv;
    }
    /**
     * Adds the specified MoneyValue to the current MoneyValue in this calculator.
     * If the currencies of the two MoneyValues are different, the specified MoneyValue
     * is converted to the currency of the current MoneyValue using the Converter client.
     * If the specified MoneyValue is invalid, an InvalidMoneyValueException is thrown.
     *
     * @param other the MoneyValue to add to the current MoneyValue
     * @return this Calculator instance, updated with the new total
     * @throws RuntimeException if the specified MoneyValue is invalid
     */

    synchronized public Calculator add(MoneyValue other) {
        if(isMoneyValueValid(other))
            throw new RuntimeException(ConstErrorMessages.INVALID_MONEY_VALUE_AS_STRING);

        MoneyValue otherInSameCurrency = mv_.getCurrency() == other.getCurrency() ?
                                            other : cv_.convertTo(other, mv_.getCurrency());
        mv_.add(otherInSameCurrency);
        return this;
    }

    /**
     * Multiplies the current MoneyValue in this calculator by the specified MoneyValue.
     * If the currencies of the two MoneyValues are different, the specified MoneyValue
     * is converted to the currency of the current MoneyValue using the Converter client.
     * If the specified MoneyValue is invalid, an InvalidMoneyValueException is thrown.
     *
     * @param other the MoneyValue to multiply with the current MoneyValue
     * @return this Calculator instance, updated with the new product
     * @throws RuntimeException if the specified MoneyValue is invalid
     */
    synchronized public Calculator multiply(MoneyValue other) {
        if(isMoneyValueValid(other))
            throw new RuntimeException(ConstErrorMessages.INVALID_MONEY_VALUE_AS_STRING);

        MoneyValue otherInSameCurrency = mv_.getCurrency() == other.getCurrency() ?
                other : cv_.convertTo(other, mv_.getCurrency());
        mv_.multiply(otherInSameCurrency);
        return this;
    }

    /**
     * Subtracts the specified MoneyValue from the current MoneyValue in this calculator.
     * If the currencies of the two MoneyValues are different, the specified MoneyValue
     * is converted to the currency of the current MoneyValue using the Converter client.
     * If the specified MoneyValue is invalid, an InvalidMoneyValueException is thrown.
     *
     * @param other the MoneyValue to subtract from the current MoneyValue
     * @return this Calculator instance, updated with the new difference
     * @throws RuntimeException if the specified MoneyValue is invalid
     */
    synchronized public Calculator subtract(MoneyValue other) {
        if(isMoneyValueValid(other))
            throw new RuntimeException(ConstErrorMessages.INVALID_MONEY_VALUE_AS_STRING);

        MoneyValue otherInSameCurrency = mv_.getCurrency() == other.getCurrency() ?
                other : cv_.convertTo(other, mv_.getCurrency());
        mv_.subtract(otherInSameCurrency);
        return this;
    }

    /**
     * Divides the current MoneyValue in this calculator by the specified MoneyValue.
     * If the currencies of the two MoneyValues are different, the specified MoneyValue
     * is converted to the currency of the current MoneyValue using the Converter client.
     * If the specified MoneyValue is invalid or zero, an InvalidMoneyValueException is thrown.
     *
     * @param other the MoneyValue to divide by
     * @return this Calculator instance, updated with the new quotient
     * @throws RuntimeException if the specified MoneyValue is invalid or zero
     */
    synchronized public Calculator divide(MoneyValue other) {
        if(isMoneyValueValid(other) || other.getAmount().equals(BigDecimal.valueOf(0.0)))
            throw new RuntimeException(ConstErrorMessages.INVALID_MONEY_VALUE_AS_STRING);

        MoneyValue otherInSameCurrency = mv_.getCurrency() == other.getCurrency() ?
                other : cv_.convertTo(other, mv_.getCurrency());
        mv_.divide(otherInSameCurrency);
        return this;
    }

    /**
     * Validates the specified MoneyValue.
     * A valid MoneyValue is not null. This method can be extended to include additional validation
     * criteria in the future, such as checking for positive amounts or specific currency requirements.
     *
     * @param mv the MoneyValue to validate
     * @return true if the MoneyValue is valid, false otherwise
     */
    private boolean isMoneyValueValid(MoneyValue mv) {
        return mv == null;
    }

}
