public class MoneyValue {
    public enum Currency{Euro, Dollar, JapaneseYen, Pound, InvalidCurrency};
    public static Currency NEUTRAL_CURRENCY = Currency.Dollar;
    private final double amount;
    private final Currency currency;
    public static final MoneyValue INVALID_MONEY_VALUE = new MoneyValue(0, Currency.InvalidCurrency);
    public static final String INVALID_MONEY_VALUE_AS_STRING = "Invalid Money Value";

    MoneyValue(double amount, Currency currency)
    {
        this.amount = Converter.roundTwoPlaces(amount);
        this.currency = currency;
    }

    MoneyValue(double amount)
    {
        this.amount = Converter.roundTwoPlaces(amount);
        this.currency = NEUTRAL_CURRENCY;
    }

    public void print()
    {
        System.out.println(this);
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
        if(this.currency == Currency.InvalidCurrency)
            return INVALID_MONEY_VALUE_AS_STRING;

        return Converter.roundTwoPlaces(amount) + " " + Converter.CURRENCY_TO_SYMBOL.get(currency);
    }

    public String toStringPrefix()
    {
        if(this.currency == Currency.InvalidCurrency)
            return INVALID_MONEY_VALUE_AS_STRING;

        return Converter.CURRENCY_TO_SYMBOL.get(currency)+ " " + Converter.roundTwoPlaces(amount);
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == this)
            return true;
        if(!(obj instanceof MoneyValue other))
            return false;

        if(other.currency == Currency.InvalidCurrency && this.currency == Currency.InvalidCurrency)
            return true;

        return this.compareTo(other) == 0;
    }

    public String toISOCode()
    {
        if(this.currency == Currency.InvalidCurrency)
            return INVALID_MONEY_VALUE_AS_STRING;

        return Converter.roundTwoPlaces(amount) + " " + Converter.CURRENCY_TO_ISO.get(currency);
    }

    public String toISOCodePrefix()
    {
        if(this.currency == Currency.InvalidCurrency)
            return INVALID_MONEY_VALUE_AS_STRING;

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
        return this.currency != Currency.InvalidCurrency;
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

    public MoneyValue add(MoneyValue other, Currency toCurrency)
    {
        if(other == null || !other.isValid())
            return INVALID_MONEY_VALUE;

        MoneyValue thisNeutral = Converter.convertToNeutral(this);
        MoneyValue otherNeutral = Converter.convertToNeutral(other);

        MoneyValue neutralResult = thisNeutral.neutralAdd(otherNeutral);

        if(toCurrency == NEUTRAL_CURRENCY)
            return neutralResult;

        return Converter.convertTo(neutralResult, toCurrency);
    }

    public MoneyValue subtract(MoneyValue other)
    {
        return subtract(other, NEUTRAL_CURRENCY);
    }

    public MoneyValue subtract(MoneyValue other, Currency toCurrency)
    {
        if(other == null || !other.isValid())
            return INVALID_MONEY_VALUE;

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

    public MoneyValue multiply(MoneyValue other, Currency toCurrency)
    {
        if(other == null || !other.isValid())
            return INVALID_MONEY_VALUE;

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

    public MoneyValue divide(MoneyValue other, Currency toCurrency)
    {
        if(other == null || !other.isValid() || other.getAmount() == 0.0)
            return INVALID_MONEY_VALUE;

        MoneyValue thisNeutral = Converter.convertToNeutral(this);
        MoneyValue otherNeutral = Converter.convertToNeutral(other);

        MoneyValue neutralResult = thisNeutral.neutralDivide(otherNeutral);

        if(toCurrency == NEUTRAL_CURRENCY)
            return neutralResult;

        return Converter.convertTo(neutralResult, toCurrency);
    }

    private MoneyValue neutralAdd(MoneyValue other)
    {
        if(other == null || !other.isValid())
            return INVALID_MONEY_VALUE;

        MoneyValue thisNeutral = Converter.convertToNeutral(this);
        MoneyValue otherNeutral = Converter.convertToNeutral(other);

        return new MoneyValue(
                    Converter.roundTwoPlaces(thisNeutral.getAmount() + otherNeutral.getAmount()),
                    NEUTRAL_CURRENCY);
    }

    private MoneyValue neutralSubtract(MoneyValue other)
    {
        if(other == null || !other.isValid())
            return INVALID_MONEY_VALUE;

        MoneyValue thisNeutral = Converter.convertToNeutral(this);
        MoneyValue otherNeutral = Converter.convertToNeutral(other);

        return new MoneyValue(
                    Converter.roundTwoPlaces(thisNeutral.getAmount() - otherNeutral.getAmount()),
                    NEUTRAL_CURRENCY);
    }

    private MoneyValue neutralMultiply(MoneyValue other)
    {
        if(other == null || !other.isValid())
            return INVALID_MONEY_VALUE;

        MoneyValue thisNeutral = Converter.convertToNeutral(this);
        MoneyValue otherNeutral = Converter.convertToNeutral(other);

        return new MoneyValue(
                    Converter.roundTwoPlaces(thisNeutral.getAmount() * otherNeutral.getAmount()),
                    NEUTRAL_CURRENCY);
    }

    private MoneyValue neutralDivide(MoneyValue other)
    {
        if(other == null || !other.isValid() || other.getAmount() == 0.0)
            return INVALID_MONEY_VALUE;

        MoneyValue thisNeutral = Converter.convertToNeutral(this);
        MoneyValue otherNeutral = Converter.convertToNeutral(other);

        return new MoneyValue(
                        Converter.roundTwoPlaces(thisNeutral.getAmount() / otherNeutral.getAmount()),
                        NEUTRAL_CURRENCY);
    }

}
