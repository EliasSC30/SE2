/**
 * An interface that contains constant error messages used in the application.
 */
interface ConstErrorMessages {
    // MoneyValue

    /**
     * Error message for invalid money value as a string.
     */
    String INVALID_MONEY_VALUE_AS_STRING = "Invalid Money Value";

    /**
     * Error message when currencies are not equal for an operation.
     */
    String CURRENCIES_NOT_EQUAL = "Currencies are not equal for operation";

    /**
     * Error message when the amount is null.
     */
    String AMOUNT_NULL = "Amount is null";

    /**
     * Error message for division by zero.
     */
    String DIVIDE_BY_ZERO = "Can not divide by zero";

    // Calculator

    /**
     * Error message when a money value is null.
     */
    String MONEY_VALUE_NULL = "Money Value can not be null";

    /**
     * Error message when a converter is null.
     */
    String CONVERTER_NULL = "Converter can not be null";

    // FixedExchangeRateProvider

    /**
     * Error message when the exchange rate type is null.
     */
    String EXCHANGE_RATE_TYPE_NULL = "ExchangeRateType can not be null";

    /**
     * Error message when the currency is null.
     */
    String CURRENCY_NULL = "Currency can not be null";
}
