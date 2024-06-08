public class MoneyValueFactory {
    private static final String INVALID_AMOUNT_MSG = "Invalid amount";


    public static MoneyValue createMoneyValue(double amount, MoneyValue.Currency currency)
    {
        if(!validateAmount(amount))
        {
            System.out.println(INVALID_AMOUNT_MSG);
            throw new MoneyValue.InvalidMoneyValueException(INVALID_AMOUNT_MSG);
        }

        return new MoneyValue(Converter.roundTwoPlaces(amount), currency);
    }

    public static MoneyValue createMoneyValue(String str)
    {
        MoneyValue ret = Converter.stringToMoneyValue(str);
        if(!validateAmount(ret.getAmount()))
        {
            System.out.println(INVALID_AMOUNT_MSG);
            throw new MoneyValue.InvalidMoneyValueException(INVALID_AMOUNT_MSG);
        }

        return ret;
    }

    private static boolean validateAmount(double amount)
    {
        return amount >= 0.0;
    }

}
