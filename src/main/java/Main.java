public class Main {
    public static void main(String[] args) {
        Converter cv = new Converter(new ExchangeRateProvider() {
            @Override
            public Double getExchangeRate(Currency from, Currency to, ExchangeRateType type) {
                return 1.0;
            }
        });

        Calculator c = new Calculator(new MoneyValue("100$"), cv);
        MoneyValue oneDollar = new MoneyValue(1.0, Currency.US_DOLLAR);
        MoneyValue twoDollar = new MoneyValue(2.0, Currency.US_DOLLAR);

        System.out.println(c.getMoneyValueClient());
        c.add(oneDollar);
        System.out.println(c.getMoneyValueClient());
        c.add(oneDollar).add(oneDollar);
        System.out.println(c.getMoneyValueClient());
        c.add(oneDollar).subtract(oneDollar);
        System.out.println(c.getMoneyValueClient());
        c.multiply(twoDollar);
        System.out.println(c.getMoneyValueClient());
        c.divide(twoDollar);
        System.out.println(c.getMoneyValueClient());
        c.multiply(twoDollar).divide(twoDollar).add(twoDollar).subtract(twoDollar);
        System.out.println(c.getMoneyValueClient());

    }
}
