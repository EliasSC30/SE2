public class MoneyValueFactory {
    private static final String INVALID_AMOUNT_MSG = "Invalid amount";
    private static final String INVALID_CURRENCY_MSG = "Invalid currency";
    public static final MoneyValue INVALID_MONEY_VALUE = new MoneyValue(0, MoneyValue.Currency.InvalidCurrency);

    public static MoneyValue createMoneyValue(double amount, MoneyValue.Currency currency)
    {
        if(!validateAmount(amount))
        {
            System.out.println(INVALID_AMOUNT_MSG);
            return MoneyValue.INVALID_MONEY_VALUE;
        }

        if(!validateCurrency(currency))
        {
            System.out.println(INVALID_CURRENCY_MSG);
            return MoneyValue.INVALID_MONEY_VALUE;
        }

        return new MoneyValue(amount, currency);
    }

    public static MoneyValue createMoneyValue(String str)
    {
        MoneyValue ret = Converter.stringToMoneyValue(str);
        if(!validateAmount(ret.getAmount()))
        {
            System.out.println(INVALID_AMOUNT_MSG);
            return MoneyValue.INVALID_MONEY_VALUE;
        }

        if(!validateCurrency(ret.getCurrency()))
        {
            System.out.println(INVALID_CURRENCY_MSG);
            return MoneyValue.INVALID_MONEY_VALUE;
        }

        return ret;
    }

    private static boolean validateAmount(double amount)
    {
        return amount >= 0.0;
    }
    private static boolean validateCurrency(MoneyValue.Currency currency)
    {
        return currency != MoneyValue.Currency.InvalidCurrency;
    }
}
