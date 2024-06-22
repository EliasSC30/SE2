import java.math.BigDecimal;

public class Converter implements ConverterClient {
    private ExchangeRateProvider exchangeRateProvider;

    Converter(ExchangeRateProvider exchangeRateProvider){
        this.exchangeRateProvider = exchangeRateProvider;
    }

    public MoneyValue convertTo(MoneyValue mv, Currency toCurrency)
    {
        return convertTo(mv, toCurrency, ExchangeRateProvider.ExchangeRateType.REALTIME);
    }

    public MoneyValue convertTo(MoneyValue mv, Currency toCurrency, ExchangeRateProvider.ExchangeRateType type)
    {
        if(mv == null || toCurrency == null)
            throw new MessageHandling.InvalidMoneyValueException(MessageHandling.INVALID_MONEY_VALUE_AS_STRING);

        BigDecimal toFactor = BigDecimal.valueOf(exchangeRateProvider.getExchangeRate(mv.getCurrency(), toCurrency, type));

        return new MoneyValue(mv.getAmount().multiply(toFactor), toCurrency);
    }
}
