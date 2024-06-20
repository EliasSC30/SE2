public class Main {
    public static void main(String[] args) {
        MoneyValue m1 = MoneyValueFactory.createMoneyValue("1.0 £");
        MoneyValue m2 = MoneyValueFactory.createMoneyValue("1.6 ¥");

        MoneyValue c1 = new  MoneyValue(1, Currency.US_DOLLAR);
        MoneyValue c2 = new MoneyValue(1, Currency.EURO);

        System.out.println(Converter.convertTo(c1, Currency.EURO));
        System.out.println(Converter.convertTo(c2, Currency.US_DOLLAR));

        System.out.println(m1.add(m2).multiply(m1).toISOCode());
        System.out.println(m1.subtract(m2).toISOCode());
        System.out.println(m1.divide(m2).toISOCode());
        System.out.println(m1.multiply(m2).toISOCode());
    }
}
