import static org.junit.jupiter.api.Assertions.*;

class MoneyValueTest {

    @org.junit.jupiter.api.Test
    void getCurrency() {
        MoneyValue mv = new MoneyValue(1.0, MoneyValue.Currency.Euro);
        assertEquals(mv.getCurrency(), MoneyValue.Currency.Euro);
    }

    @org.junit.jupiter.api.Test
    void getAmount() {
        MoneyValue mv = new MoneyValue(1.0);
        assertEquals(1.0, mv.getAmount());
    }

    @org.junit.jupiter.api.Test
    void testToString() {
        String expectedResult = "100.0 €";
        MoneyValue result = new MoneyValue(100, MoneyValue.Currency.Euro);
        assertEquals(expectedResult, result.toString());

        String expectedFail = "0xdeadbeef";
        MoneyValue failed = Converter.stringToMoneyValue(expectedFail);
        assertEquals(MoneyValue.INVALID_MONEY_VALUE_AS_STRING, failed.toString());
    }

    @org.junit.jupiter.api.Test
    void toStringPrefix() {
        String expected = "$ 100.0";
        MoneyValue mv = new MoneyValue(100, MoneyValue.Currency.Dollar);
        assertEquals(expected, mv.toStringPrefix());
    }

    @org.junit.jupiter.api.Test
    void testEquals() {
        MoneyValue mv = new MoneyValue(1.0, MoneyValue.Currency.Dollar);
        Object failO = new Object();
        MoneyValue otherAmount = new MoneyValue(2.0);
        MoneyValue otherCurrency = new MoneyValue(1.0, MoneyValue.Currency.Euro);
        MoneyValue equal = new MoneyValue(1.0, MoneyValue.Currency.Dollar);


        assertNotEquals(mv, failO);
        assertNotEquals(mv, otherAmount);
        assertNotEquals(mv, otherCurrency);

        assertEquals(mv, equal);
    }

    @org.junit.jupiter.api.Test
    void toISOCode() {
        String expectedISOCode = "100.0 EUR";
        MoneyValue mv = new MoneyValue(100.0, MoneyValue.Currency.Euro);

        assertEquals(expectedISOCode, mv.toISOCode());
        assertEquals(MoneyValue.INVALID_MONEY_VALUE_AS_STRING, MoneyValue.INVALID_MONEY_VALUE.toISOCode());
    }

    @org.junit.jupiter.api.Test
    void toISOCodePrefix() {
        String expectedISOCode = "JPY 100.0";
        MoneyValue mv = new MoneyValue(100.0, MoneyValue.Currency.JapaneseYen);

        assertEquals(expectedISOCode, mv.toISOCodePrefix());
        assertEquals(MoneyValue.INVALID_MONEY_VALUE_AS_STRING, MoneyValue.INVALID_MONEY_VALUE.toISOCodePrefix());
    }

    @org.junit.jupiter.api.Test
    void compareTo() {
        MoneyValue greaterZero = new MoneyValue(1.0);
        MoneyValue zero = new MoneyValue(0.0);
        MoneyValue smallerZero = new MoneyValue(-1.0);

        assertTrue(greaterZero.compareTo(zero) > 0 && greaterZero.compareTo(smallerZero) > 0);
        assertTrue(0 < zero.compareTo(smallerZero) && zero.compareTo(greaterZero) < 0);
        assertTrue(smallerZero.compareTo(zero) < 0 && smallerZero.compareTo(greaterZero) < 0);
        assertEquals(0.0, smallerZero.compareTo(smallerZero));
        assertEquals(0.0, zero.compareTo(zero));
        assertEquals(0.0, greaterZero.compareTo(greaterZero));
    }

    @org.junit.jupiter.api.Test
    void isValid() {
        MoneyValue valid = new MoneyValue(1.0);
        MoneyValue invalid = MoneyValue.INVALID_MONEY_VALUE;

        assertTrue(valid.isValid());
        assertFalse(invalid.isValid());
    }

    @org.junit.jupiter.api.Test
    void convertTo() {
        MoneyValue mvInDollar = new MoneyValue(1.0, MoneyValue.Currency.Dollar);
        MoneyValue mvInEuro = mvInDollar.convertTo(MoneyValue.Currency.Euro);
        MoneyValue invalid = MoneyValue.INVALID_MONEY_VALUE;

        final double oneDollarInEuroRounded = Converter.roundTwoPlaces(1.0/1.09);
        assertEquals(mvInEuro.getAmount(), oneDollarInEuroRounded);

        MoneyValue invalidConverted = invalid.convertTo(MoneyValue.Currency.Euro);
        assertEquals(MoneyValue.INVALID_MONEY_VALUE, invalidConverted);
    }

    @org.junit.jupiter.api.Test
    void add() {
        MoneyValue oneDollar = new MoneyValue(1.0, MoneyValue.Currency.Dollar);
        MoneyValue minusOneDollar = new MoneyValue(-1.0, MoneyValue.Currency.Dollar);
        MoneyValue onePound = new MoneyValue(1.0, MoneyValue.Currency.Pound);

        assertEquals(2.0, oneDollar.add(oneDollar).getAmount());
        assertEquals(2.0, onePound.add(onePound).getAmount());
        assertEquals(0.0, oneDollar.add(minusOneDollar).getAmount());
        assertEquals(0.0, minusOneDollar.add(oneDollar).getAmount());

        assertEquals(Converter.roundTwoPlaces(2.0 * 1.28), onePound.add(onePound, MoneyValue.Currency.Dollar).getAmount());
        assertEquals(MoneyValue.Currency.Dollar, onePound.add(onePound, MoneyValue.Currency.Dollar).getCurrency());

        assertEquals(2.0, onePound.add(onePound).getAmount());
        assertEquals(2.28, oneDollar.add(onePound).getAmount());

        MoneyValue invalid = MoneyValue.INVALID_MONEY_VALUE;

        assertEquals(MoneyValue.INVALID_MONEY_VALUE, invalid.add(oneDollar));
        assertEquals(MoneyValue.INVALID_MONEY_VALUE, oneDollar.add(invalid));
        assertEquals(MoneyValue.INVALID_MONEY_VALUE, invalid.add(invalid));
    }

    @org.junit.jupiter.api.Test
    void testAdd() {
    }

    @org.junit.jupiter.api.Test
    void subtract() {
        MoneyValue oneDollar = new MoneyValue(1.0, MoneyValue.Currency.Dollar);
        MoneyValue minusOneDollar = new MoneyValue(-1.0, MoneyValue.Currency.Dollar);
        MoneyValue onePound = new MoneyValue(1.0, MoneyValue.Currency.Pound);

        assertEquals(0.0, oneDollar.subtract(oneDollar).getAmount());
        assertEquals(0.0, onePound.subtract(onePound).getAmount());
        assertEquals(2.0, oneDollar.subtract(minusOneDollar).getAmount());
        assertEquals(-2.0, minusOneDollar.subtract(oneDollar).getAmount());

        assertEquals(-2.28, minusOneDollar.subtract(onePound, MoneyValue.Currency.Dollar).getAmount());
        assertEquals(MoneyValue.Currency.Dollar, onePound.subtract(onePound, MoneyValue.Currency.Dollar).getCurrency());

        assertEquals(0.0, onePound.subtract(onePound).getAmount());
        final double onePoundInDollarRounded = Converter.roundTwoPlaces(1.28);
        assertEquals(Converter.roundTwoPlaces(onePoundInDollarRounded - 1.0), onePound.subtract(oneDollar).getAmount());

        MoneyValue invalid = MoneyValue.INVALID_MONEY_VALUE;

        assertEquals(MoneyValue.INVALID_MONEY_VALUE, invalid.subtract(oneDollar));
        assertEquals(MoneyValue.INVALID_MONEY_VALUE, oneDollar.subtract(invalid));
        assertEquals(MoneyValue.INVALID_MONEY_VALUE, invalid.subtract(invalid));
    }

    @org.junit.jupiter.api.Test
    void testSubtract() {
    }

    @org.junit.jupiter.api.Test
    void multiply() {
        MoneyValue oneDollar = new MoneyValue(1.0, MoneyValue.Currency.Dollar);
        MoneyValue minusOneDollar = new MoneyValue(-1.0, MoneyValue.Currency.Dollar);
        MoneyValue onePound = new MoneyValue(1.0, MoneyValue.Currency.Pound);

        assertEquals(1.0, oneDollar.multiply(oneDollar).getAmount());
        assertEquals(1.64, onePound.multiply(onePound).getAmount());
        assertEquals(-1.0, oneDollar.multiply(minusOneDollar).getAmount());
        assertEquals(-1.0, minusOneDollar.multiply(oneDollar).getAmount());
        assertEquals(1.0, minusOneDollar.multiply(minusOneDollar).getAmount());

        assertEquals(1.64,
                     onePound.multiply(onePound, MoneyValue.Currency.Dollar).getAmount());
        assertEquals(MoneyValue.Currency.Dollar,
                     onePound.multiply(onePound, MoneyValue.Currency.Dollar).getCurrency());

        assertEquals(1.28, oneDollar.multiply(onePound).getAmount());

        MoneyValue invalid = MoneyValue.INVALID_MONEY_VALUE;

        assertEquals(MoneyValue.INVALID_MONEY_VALUE, invalid.multiply(oneDollar));
        assertEquals(MoneyValue.INVALID_MONEY_VALUE, oneDollar.multiply(invalid));
        assertEquals(MoneyValue.INVALID_MONEY_VALUE, invalid.multiply(invalid));
    }

    @org.junit.jupiter.api.Test
    void testMultiply() {
    }

    @org.junit.jupiter.api.Test
    void divide() {
        MoneyValue oneDollar = new MoneyValue(1.0, MoneyValue.Currency.Dollar);
        MoneyValue minusOneDollar = new MoneyValue(-1.0, MoneyValue.Currency.Dollar);
        MoneyValue onePound = new MoneyValue(1.0, MoneyValue.Currency.Pound);

        assertEquals(1.0, oneDollar.divide(oneDollar).getAmount());
        assertEquals(1.0, onePound.divide(onePound).getAmount());
        assertEquals(-1.0, oneDollar.divide(minusOneDollar).getAmount());
        assertEquals(-1.0, minusOneDollar.divide(oneDollar).getAmount());
        assertEquals(-1.0, oneDollar.divide(minusOneDollar).getAmount());

        assertEquals(1.0,
                onePound.divide(onePound, MoneyValue.Currency.Dollar).getAmount());
        assertEquals(MoneyValue.Currency.Dollar,
                onePound.divide(onePound, MoneyValue.Currency.Dollar).getCurrency());

        assertEquals(Converter.roundTwoPlaces(1.0/1.28), oneDollar.divide(onePound).getAmount());

        MoneyValue invalid = MoneyValue.INVALID_MONEY_VALUE;

        assertEquals(MoneyValue.INVALID_MONEY_VALUE, invalid.divide(oneDollar));
        assertEquals(MoneyValue.INVALID_MONEY_VALUE, oneDollar.divide(invalid));
        assertEquals(MoneyValue.INVALID_MONEY_VALUE, invalid.divide(invalid));
        final MoneyValue zeroValue = new MoneyValue(0.0);
        assertEquals(MoneyValue.INVALID_MONEY_VALUE, oneDollar.divide(zeroValue));
    }

    @org.junit.jupiter.api.Test
    void testDivide() {
    }

    @org.junit.jupiter.api.Test
    void testConcatenation() {
        double expAddAmount = 2.0;
        double expSubAmount = 0.0;
        double expMulAmount = 0.0;
        double expDivAmount = 1e10;

        MoneyValue mvAddResult = new MoneyValue(1.0);
        MoneyValue mvSubResult = new MoneyValue(1.0);
        MoneyValue mvMulResult = new MoneyValue(1.0);
        MoneyValue mvDivResult = new MoneyValue(1.0);

        MoneyValue mvConst = new MoneyValue(0.1);

        for(int i = 0; i < 10; ++i)
        {
            mvAddResult = mvAddResult.add(mvConst);
            mvSubResult = mvSubResult.subtract(mvConst);
            mvMulResult = mvMulResult.multiply(mvConst);
            mvDivResult = mvDivResult.divide(mvConst);
        }

        assertEquals(expAddAmount, mvAddResult.getAmount());
        assertEquals(expSubAmount, mvSubResult.getAmount());
        assertEquals(expMulAmount, mvMulResult.getAmount());
        assertEquals(expDivAmount, mvDivResult.getAmount());


        MoneyValue stressAddSubMv = new MoneyValue(0.0);

        int nrOfAdditionsAndSubtractions = 1_000_000;
        for(int i = 0; i < nrOfAdditionsAndSubtractions; ++i)
            stressAddSubMv = stressAddSubMv.add(new MoneyValue(0.1));
        assertEquals(stressAddSubMv.getAmount(), 100_000);

        for(int i = 0; i < nrOfAdditionsAndSubtractions; ++i)
            stressAddSubMv = stressAddSubMv.subtract(new MoneyValue(0.1));

        assertEquals(stressAddSubMv.getAmount(), 0.0);


        MoneyValue stressMulDivMv = new MoneyValue(1.0);

        int nrOfMultAndDivs = 30;
        for(int i = 0; i < nrOfMultAndDivs; ++i)
            stressMulDivMv = stressMulDivMv.multiply(new MoneyValue(2.0));
        assertEquals(stressMulDivMv.getAmount(), (1 << nrOfMultAndDivs));

        for(int i = 0; i < nrOfMultAndDivs; ++i)
            stressMulDivMv = stressMulDivMv.divide(new MoneyValue(2.0));


        assertEquals(stressMulDivMv.getAmount(), 1.0);

    }
}