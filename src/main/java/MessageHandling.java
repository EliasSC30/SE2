public class MessageHandling {
    // MoneyValue
    public static final String INVALID_MONEY_VALUE_AS_STRING = "Invalid Money Value";
    public static final String CURRENCIES_NOT_EQUAL = "Currencies are not equal for operation";
    public static class InvalidMoneyValueException extends RuntimeException {
        public InvalidMoneyValueException(String message) {
            super(message);
        }
    }


    // Converter
    public static final String EXCHANGE_RATE_TYPE_NULL = "ExchangeRate Type is null";
    public static class InvalidConverterException extends RuntimeException {
        public InvalidConverterException(String message) {
            super(message);
        }
    }
}
