import java.math.BigDecimal;

public class MoneyValueFactory {
    private static final String INVALID_AMOUNT_MSG = "Invalid amount";


    public static MoneyValue createMoneyValue(BigDecimal amount, Currency currency)
    {
        if(validateAmount(amount))
        {
            System.out.println(INVALID_AMOUNT_MSG);
            throw new MoneyValue.InvalidMoneyValueException(INVALID_AMOUNT_MSG);
        }

        return new MoneyValue(amount, currency);
    }



    public static MoneyValue createMoneyValue(String str)
    {
        MoneyValue ret = new MoneyValue(str);
        if(validateAmount(ret.getAmount()))
        {
            System.out.println(INVALID_AMOUNT_MSG);
            throw new MoneyValue.InvalidMoneyValueException(INVALID_AMOUNT_MSG);
        }

        return ret;
    }

    private static boolean validateAmount(BigDecimal amount)
    {
        return amount == null || amount.compareTo(BigDecimal.ZERO) < 0;
    }

}
