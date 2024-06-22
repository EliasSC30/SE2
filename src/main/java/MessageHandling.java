public class MessageHandling {

    public static final String INVALID_MONEY_VALUE_AS_STRING = "Invalid Money Value";
    public static class InvalidMoneyValueException extends RuntimeException {
        public InvalidMoneyValueException(String message) {
            super(message);
        }
    }
}
