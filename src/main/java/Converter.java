import java.math.BigDecimal;

public class Converter implements ConverterClient {
    private ExchangeRateProvider exchangeRateProvider;

    Converter(ExchangeRateProvider exchangeRateProvider){
        this.exchangeRateProvider = exchangeRateProvider;
    }

    public MoneyValue convertTo(MoneyValue mv, Currency toCurrency) {
        return convertTo(mv, toCurrency, ExchangeRateProvider.ExchangeRateType.REALTIME);
    }

    public MoneyValue convertTo(MoneyValue mv, Currency toCurrency, ExchangeRateProvider.ExchangeRateType type) {
        if(mv == null)
            throw new RuntimeException(ConstErrorMessages.INVALID_MONEY_VALUE_AS_STRING);
        else if (toCurrency == null)
            throw new RuntimeException(ConstErrorMessages.CONVERTER_NULL);
        else if (type == null)
            throw new RuntimeException(ConstErrorMessages.EXCHANGE_RATE_TYPE_NULL);


        BigDecimal toFactor = BigDecimal.valueOf(exchangeRateProvider.getExchangeRate(mv.getCurrency(), toCurrency, type));

        return new MoneyValue(mv.getAmount().multiply(toFactor), toCurrency);
    }
}
