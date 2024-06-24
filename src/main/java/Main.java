import javax.swing.text.html.MinimalHTMLWriter;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        Converter cv = new Converter(new FixedExchangeRateProvider());

        MoneyValue mv1 = new MoneyValue(1, Currency.US_DOLLAR);
        MoneyValue mv2 = new MoneyValue("2 $");
        MoneyValue mv3 = new MoneyValue("3 EUR");

        System.out.println(mv1);

        mv1.add(mv2);

        System.out.println(mv1);

        Calculator c = new Calculator(mv1, cv);

        c.add(mv3);

        System.out.println(c.getMoneyValueClient());

        MoneyValue mv4 = new MoneyValue("USD 1");
        MoneyValue mv5 = new MoneyValue("$ 2.0");

        mv4.add(mv5).subtract(mv5).multiply(mv5).divide(mv5);

        System.out.println(mv4);
    }
}
