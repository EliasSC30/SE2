public interface ExchangeRateProvider {

    public enum ExchangeRateType {
        REALTIME,
        MONTHLY,
        DAILY
    }

    Double getExchangeRate(Currency from, Currency to, ExchangeRateType type);
}
