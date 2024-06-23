import java.math.BigDecimal;

public class Calculator {
    private MoneyValueClient mv_;
    private ConverterClient cv_;

    public Calculator(MoneyValue mv, Converter cv) {
        if (mv == null)
            throw new RuntimeException(ConstErrorMessages.MONEY_VALUE_NULL);
        else if (cv == null)
            throw new RuntimeException(ConstErrorMessages.CONVERTER_NULL);

        this.mv_ = mv;
        this.cv_ = cv;
    }

    public MoneyValueClient getMoneyValueClient() { return this.mv_; }
    public ConverterClient getConverterClient() { return this.cv_; }
    public void setMoneyValue(MoneyValue mv) {
        if(mv == null)
            throw new RuntimeException(ConstErrorMessages.MONEY_VALUE_NULL);
        this.mv_ = mv;
    }

     public void setConverter(Converter cv) {
        if(cv == null)
            throw new RuntimeException(ConstErrorMessages.CONVERTER_NULL);
        this.cv_ = cv;
    }

    synchronized public Calculator add(MoneyValue other) {
        if(!validateMoneyValue(other))
            throw new RuntimeException(ConstErrorMessages.INVALID_MONEY_VALUE_AS_STRING);

        MoneyValue otherInSameCurrency = mv_.getCurrency() == other.getCurrency() ?
                                            other : cv_.convertTo(other, mv_.getCurrency());
        mv_.add(otherInSameCurrency);
        return this;
    }

    synchronized public Calculator multiply(MoneyValue other) {
        if(!validateMoneyValue(other))
            throw new RuntimeException(ConstErrorMessages.INVALID_MONEY_VALUE_AS_STRING);

        MoneyValue otherInSameCurrency = mv_.getCurrency() == other.getCurrency() ?
                other : cv_.convertTo(other, mv_.getCurrency());
        mv_.multiply(otherInSameCurrency);
        return this;
    }

    synchronized public Calculator subtract(MoneyValue other) {
        if(!validateMoneyValue(other))
            throw new RuntimeException(ConstErrorMessages.INVALID_MONEY_VALUE_AS_STRING);

        MoneyValue otherInSameCurrency = mv_.getCurrency() == other.getCurrency() ?
                other : cv_.convertTo(other, mv_.getCurrency());
        mv_.subtract(otherInSameCurrency);
        return this;
    }

    synchronized public Calculator divide(MoneyValue other) {
        if(!validateMoneyValue(other) || other.getAmount().equals(BigDecimal.valueOf(0.0)))
            throw new RuntimeException(ConstErrorMessages.INVALID_MONEY_VALUE_AS_STRING);

        MoneyValue otherInSameCurrency = mv_.getCurrency() == other.getCurrency() ?
                other : cv_.convertTo(other, mv_.getCurrency());
        mv_.divide(otherInSameCurrency);
        return this;
    }

    private boolean validateMoneyValue(MoneyValue mv) {
        if(mv == null)
            return false;
        return true;
    }

}
