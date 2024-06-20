import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class Converter {
    private static final ExchangeRateProvider exchangeRateProvider = new FixedExchangeRateProvider();


    public static MoneyValue convertTo(MoneyValue mv, Currency toCurrency)
    {
        if(mv == null)
            throw new MoneyValue.InvalidMoneyValueException(MoneyValue.INVALID_MONEY_VALUE_AS_STRING);

        if(toCurrency == null)
            throw new MoneyValue.InvalidMoneyValueException(MoneyValue.INVALID_MONEY_VALUE_AS_STRING);

        BigDecimal toFactor = BigDecimal.valueOf(exchangeRateProvider.getExchangeRate(mv.getCurrency(), toCurrency));

        return new MoneyValue(mv.getAmount().multiply(toFactor), toCurrency);
    }
}
