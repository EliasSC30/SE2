public class MoneyValue {
    public enum Currency{EURO, US_DOLLAR, JAPANESE_YEN, BRITISH_POUND};
    public static Currency NEUTRAL_CURRENCY = Currency.US_DOLLAR;
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

    MoneyValue(double amount)
    {
        if (Double.isNaN(amount) || !amountIsInRange(amount))
            throw new InvalidMoneyValueException(INVALID_MONEY_VALUE_AS_STRING);

        this.amount = Converter.roundTwoPlaces(amount);
        this.currency = NEUTRAL_CURRENCY;
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
        return Converter.roundTwoPlaces(amount) + " " + Converter.CURRENCY_TO_SYMBOL.get(currency);
    }

    public String toStringPrefix()
    {
        return Converter.CURRENCY_TO_SYMBOL.get(currency) + " " + Converter.roundTwoPlaces(amount);
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
        return Converter.roundTwoPlaces(amount) + " " + Converter.CURRENCY_TO_ISO.get(currency);
    }

    public String toISOCodePrefix()
    {
        return Converter.CURRENCY_TO_ISO.get(currency) + " " + Converter.roundTwoPlaces(amount);
    }

    public int compareTo(MoneyValue other)
    {
        if(this == other)
            return 0;

        return (int)(
                (100.0 * (Converter.convertToNeutral(this).amount - Converter.convertToNeutral(other).amount)));
    }

    public boolean isValid()
    {
        return !Double.isNaN(this.amount);
    }

    public MoneyValue convertTo(Currency toCurrency)
    {
        return Converter.convertTo(this, toCurrency);
    }

    public MoneyValue add(MoneyValue other)
    {
        if(this.currency == other.getCurrency())
            return this.add(other, this.currency);

        return this.add(other, NEUTRAL_CURRENCY);
    }

    private MoneyValue add(MoneyValue other, Currency toCurrency)
    {
        if(atLeastOneIsInvalid(this, other))
            throw new InvalidMoneyValueException(INVALID_MONEY_VALUE_AS_STRING);

        MoneyValue thisNeutral = Converter.convertToNeutral(this);
        MoneyValue otherNeutral = Converter.convertToNeutral(other);

        MoneyValue neutralResult = thisNeutral.neutralAdd(otherNeutral);

        if(toCurrency == NEUTRAL_CURRENCY)
            return neutralResult;

        return Converter.convertTo(neutralResult, toCurrency);
    }

    public MoneyValue subtract(MoneyValue other)
    {
        if(this.currency == other.getCurrency())
            return subtract(other, this.currency);

        return subtract(other, NEUTRAL_CURRENCY);
    }

    private MoneyValue subtract(MoneyValue other, Currency toCurrency)
    {
        if(atLeastOneIsInvalid(this, other))
            throw new InvalidMoneyValueException(INVALID_MONEY_VALUE_AS_STRING);

        MoneyValue thisNeutral = Converter.convertToNeutral(this);
        MoneyValue otherNeutral = Converter.convertToNeutral(other);

        MoneyValue neutralResult = thisNeutral.neutralSubtract(otherNeutral);

        if(toCurrency == NEUTRAL_CURRENCY)
            return neutralResult;

        return Converter.convertTo(neutralResult, toCurrency);
    }

    public MoneyValue multiply(MoneyValue other)
    {
        return multiply(other, NEUTRAL_CURRENCY);
    }

    private MoneyValue multiply(MoneyValue other, Currency toCurrency)
    {
        if(atLeastOneIsInvalid(this, other))
            throw new InvalidMoneyValueException(INVALID_MONEY_VALUE_AS_STRING);

        MoneyValue thisNeutral = Converter.convertToNeutral(this);
        MoneyValue otherNeutral = Converter.convertToNeutral(other);

        MoneyValue neutralResult = thisNeutral.neutralMultiply(otherNeutral);

        if(toCurrency == NEUTRAL_CURRENCY)
            return neutralResult;

        return Converter.convertTo(neutralResult, toCurrency);
    }

    public MoneyValue divide(MoneyValue other)
    {
        return divide(other, NEUTRAL_CURRENCY);
    }

    private MoneyValue divide(MoneyValue other, Currency toCurrency)
    {
        if(atLeastOneIsInvalid(this, other) || other.getAmount() == 0.0)
            throw new InvalidMoneyValueException(INVALID_MONEY_VALUE_AS_STRING);

        MoneyValue thisNeutral = Converter.convertToNeutral(this);
        MoneyValue otherNeutral = Converter.convertToNeutral(other);

        MoneyValue neutralResult = thisNeutral.neutralDivide(otherNeutral);

        if(toCurrency == NEUTRAL_CURRENCY)
            return neutralResult;

        return Converter.convertTo(neutralResult, toCurrency);
    }

    private MoneyValue neutralAdd(MoneyValue other)
    {
        if(atLeastOneIsInvalid(this, other))
            throw new InvalidMoneyValueException(INVALID_MONEY_VALUE_AS_STRING);

        MoneyValue thisNeutral = Converter.convertToNeutral(this);
        MoneyValue otherNeutral = Converter.convertToNeutral(other);

        return new MoneyValue(
                    Converter.roundTwoPlaces(thisNeutral.getAmount() + otherNeutral.getAmount()),
                    NEUTRAL_CURRENCY);
    }

    private MoneyValue neutralSubtract(MoneyValue other)
    {
        if(atLeastOneIsInvalid(this, other))
            throw new InvalidMoneyValueException(INVALID_MONEY_VALUE_AS_STRING);

        MoneyValue thisNeutral = Converter.convertToNeutral(this);
        MoneyValue otherNeutral = Converter.convertToNeutral(other);

        return new MoneyValue(
                    Converter.roundTwoPlaces(thisNeutral.getAmount() - otherNeutral.getAmount()),
                    NEUTRAL_CURRENCY);
    }

    private MoneyValue neutralMultiply(MoneyValue other)
    {
        if(atLeastOneIsInvalid(this, other))
            throw new InvalidMoneyValueException(INVALID_MONEY_VALUE_AS_STRING);

        MoneyValue thisNeutral = Converter.convertToNeutral(this);
        MoneyValue otherNeutral = Converter.convertToNeutral(other);

        return new MoneyValue(
                    Converter.roundTwoPlaces(thisNeutral.getAmount() * otherNeutral.getAmount()),
                    NEUTRAL_CURRENCY);
    }

    private MoneyValue neutralDivide(MoneyValue other)
    {
        if(atLeastOneIsInvalid(this, other) || other.getAmount() == 0.0)
            throw new InvalidMoneyValueException(INVALID_MONEY_VALUE_AS_STRING);

        MoneyValue thisNeutral = Converter.convertToNeutral(this);
        MoneyValue otherNeutral = Converter.convertToNeutral(other);

        return new MoneyValue(
                        Converter.roundTwoPlaces(thisNeutral.getAmount() / otherNeutral.getAmount()),
                        NEUTRAL_CURRENCY);
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
