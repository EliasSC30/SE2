public class Calculator {
    private MoneyValue mv_;
    private Converter cv_;

    public Calculator(MoneyValue mv, Converter cv) {
        this.mv_ = mv;
        this.cv_ = cv;
    }

    public MoneyValue getMoneyValue() {
       return this.mv_;
    }

    public Calculator add(MoneyValue other) {
        if(!validateMoneyValue(other))
            throw new MoneyValue.InvalidMoneyValueException(MoneyValue.INVALID_MONEY_VALUE_AS_STRING);

        MoneyValue otherInSameCurrency = mv_.getCurrency() == other.getCurrency() ?
                                            other : cv_.convertTo(other, mv_.getCurrency());
        mv_ = mv_.add(otherInSameCurrency);
        return this;
    }

    public Calculator multiply(MoneyValue other) {
        if(!validateMoneyValue(other))
            throw new MoneyValue.InvalidMoneyValueException(MoneyValue.INVALID_MONEY_VALUE_AS_STRING);

        MoneyValue otherInSameCurrency = mv_.getCurrency() == other.getCurrency() ?
                other : cv_.convertTo(other, mv_.getCurrency());
        mv_ = mv_.multiply(otherInSameCurrency);
        return this;
    }

    public Calculator subtract(MoneyValue other) {
        if(!validateMoneyValue(other))
            throw new MoneyValue.InvalidMoneyValueException(MoneyValue.INVALID_MONEY_VALUE_AS_STRING);

        MoneyValue otherInSameCurrency = mv_.getCurrency() == other.getCurrency() ?
                other : cv_.convertTo(other, mv_.getCurrency());
        mv_ = mv_.subtract(otherInSameCurrency);
        return this;
    }

    public Calculator divide(MoneyValue other) {
        if(!validateMoneyValue(other))
            throw new MoneyValue.InvalidMoneyValueException(MoneyValue.INVALID_MONEY_VALUE_AS_STRING);

        MoneyValue otherInSameCurrency = mv_.getCurrency() == other.getCurrency() ?
                other : cv_.convertTo(other, mv_.getCurrency());
        mv_ = mv_.divide(otherInSameCurrency);
        return this;
    }

    private boolean validateMoneyValue(MoneyValue mv) {
        if(mv == null)
            return false;
        return true;
    }

}
