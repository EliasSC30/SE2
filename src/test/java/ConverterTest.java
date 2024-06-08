import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ConverterTest {

    @Test
    void getConversionFactor() {
        double euroToDollarExp = 1.09;
        double dollarToDollar =
                        Converter.getConversionFactor(MoneyValue.Currency.Dollar, MoneyValue.Currency.Dollar);
        double dollarToEuro =
                        Converter.getConversionFactor(MoneyValue.Currency.Dollar, MoneyValue.Currency.Euro);
        double euroToDollar =
                Converter.getConversionFactor(MoneyValue.Currency.Euro, MoneyValue.Currency.Dollar);

        assertEquals(1.0, dollarToDollar);
        assertEquals(euroToDollarExp, euroToDollar);
        assertEquals(1.0 / euroToDollarExp, dollarToEuro);
    }

    @Test
    void convertToNeutral() {
        MoneyValue mvNeutral = new MoneyValue(1.0);
        MoneyValue mvEuro = new MoneyValue(1.09, MoneyValue.Currency.Euro);
        MoneyValue mvInv = MoneyValue.INVALID_MONEY_VALUE;


        MoneyValue neutralToNeutral = Converter.convertToNeutral(mvNeutral);
        MoneyValue euroToNeutral = Converter.convertToNeutral(mvEuro);
        MoneyValue invToNeutral = Converter.convertToNeutral(mvInv);

        assertEquals(mvNeutral, neutralToNeutral);
        assertEquals(mvEuro, euroToNeutral);
        assertEquals(mvInv, invToNeutral);
    }

    @Test
    void convertTo() {
        MoneyValue oneDollar = new MoneyValue(1.0, MoneyValue.Currency.Dollar);
        MoneyValue invalidMv = MoneyValue.INVALID_MONEY_VALUE;

        MoneyValue expEuroOfOneDollar = new MoneyValue(0.92, MoneyValue.Currency.Euro);
        MoneyValue oneDollarInEuro = Converter.convertTo(oneDollar, MoneyValue.Currency.Euro);

        assertEquals(oneDollarInEuro, expEuroOfOneDollar);

        MoneyValue invToDollar = Converter.convertTo(MoneyValue.INVALID_MONEY_VALUE, MoneyValue.Currency.Dollar);
        assertEquals(invToDollar, invalidMv);

        MoneyValue dollarToInv = Converter.convertTo(oneDollar, MoneyValue.Currency.InvalidCurrency);
        assertEquals(dollarToInv, invalidMv);

    }

    @Test
    void stringToMoneyValue() {
        MoneyValue oneEuro = new MoneyValue(1.0, MoneyValue.Currency.Euro);

        assertEquals(oneEuro, Converter.stringToMoneyValue("1€"));
        assertEquals(oneEuro, Converter.stringToMoneyValue("1 €"));
        assertEquals(oneEuro, Converter.stringToMoneyValue("€1"));
        assertEquals(oneEuro, Converter.stringToMoneyValue("€ 1"));

        assertEquals(MoneyValue.INVALID_MONEY_VALUE, Converter.stringToMoneyValue(""));
        assertEquals(MoneyValue.INVALID_MONEY_VALUE, Converter.stringToMoneyValue(" "));
        assertEquals(MoneyValue.INVALID_MONEY_VALUE, Converter.stringToMoneyValue("asdf"));
        assertEquals(MoneyValue.INVALID_MONEY_VALUE, Converter.stringToMoneyValue("$"));
        assertEquals(MoneyValue.INVALID_MONEY_VALUE, Converter.stringToMoneyValue("$a"));
        assertEquals(MoneyValue.INVALID_MONEY_VALUE, Converter.stringToMoneyValue("a$"));
        assertEquals(MoneyValue.INVALID_MONEY_VALUE, Converter.stringToMoneyValue("2$s"));
        assertEquals(MoneyValue.INVALID_MONEY_VALUE, Converter.stringToMoneyValue("s2$"));
        assertEquals(MoneyValue.INVALID_MONEY_VALUE, Converter.stringToMoneyValue("2$2"));
        assertEquals(MoneyValue.INVALID_MONEY_VALUE, Converter.stringToMoneyValue("02.2$"));
        assertEquals(MoneyValue.INVALID_MONEY_VALUE, Converter.stringToMoneyValue("$0s2$"));
        assertEquals(MoneyValue.INVALID_MONEY_VALUE, Converter.stringToMoneyValue("02.0 $"));
    }

    @Test
    void roundTwoPlaces() {
        double a = 1.49999999;
        double aRounded = Converter.roundTwoPlaces(a);
        assertEquals(aRounded, 1.50);

        double b = -1.4999999;
        double bRounded = Converter.roundTwoPlaces(b);
        assertEquals(bRounded, -1.50);

        double c = 0.004;
        double cRounded = Converter.roundTwoPlaces(c);
        assertEquals(cRounded, 0.0);

        double d = 0.005;
        double dRounded = Converter.roundTwoPlaces(d);
        assertEquals(dRounded, 0.01);

        double naN = Double.NaN;
        double naNRounded = Converter.roundTwoPlaces(naN);
        assertEquals(naNRounded, naN);
    }
}