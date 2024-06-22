public class MessageHandling {

    public static final String INVALID_MONEY_VALUE_AS_STRING = "Invalid Money Value";
    public static final String CURRENCIES_NOT_EQUAL = "Currencies are not equal for operation";
    public static class InvalidMoneyValueException extends RuntimeException {
        public InvalidMoneyValueException(String message) {
            super(message);
        }
    }
}
