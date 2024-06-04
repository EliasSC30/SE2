public class Main {
    public static void main(String[] args) {
        MoneyValue m1 = MoneyValueFactory.createMoneyValue("1.0 $");
        MoneyValue m2 = MoneyValueFactory.createMoneyValue("1.6 $");

        System.out.println(m1.add(m2).convertTo(MoneyValue.Currency.JapaneseYen).multiply(m1).toISOCode());
        System.out.println(m1.subtract(m2).toISOCode());
        System.out.println(m1.divide(m2).toISOCode());
        System.out.println(m1.multiply(m2).toISOCode());

    }
}
