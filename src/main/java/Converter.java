import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class Converter {
    private ExchangeRateProvider exchangeRateProvider;

    Converter(ExchangeRateProvider exchangeRateProvider){
        this.exchangeRateProvider = exchangeRateProvider;
    }


    public MoneyValue convertTo(MoneyValue mv, Currency toCurrency)
    {
        if(mv == null || toCurrency == null)
            throw new MoneyValue.InvalidMoneyValueException(MoneyValue.INVALID_MONEY_VALUE_AS_STRING);

        BigDecimal toFactor = BigDecimal.valueOf(exchangeRateProvider.getExchangeRate(mv.getCurrency(), toCurrency));

        return new MoneyValue(mv.getAmount().multiply(toFactor), toCurrency, this);
    }
}
