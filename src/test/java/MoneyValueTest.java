import static org.junit.jupiter.api.Assertions.*;

class MoneyValueTest {

    @org.junit.jupiter.api.Test
    void print() {
    }

    @org.junit.jupiter.api.Test
    void getCurrency() {
    }

    @org.junit.jupiter.api.Test
    void getAmount() {
    }

    @org.junit.jupiter.api.Test
    void testToString() {
        String expected = "100.0 $";
        MoneyValue mv = new MoneyValue(100, MoneyValue.Currency.Dollar);
        assertEquals(expected, mv.toString());
    }

    @org.junit.jupiter.api.Test
    void toStringPrefix() {
        String expected = "$ 100.0";
        MoneyValue mv = new MoneyValue(100, MoneyValue.Currency.Dollar);
        assertEquals(expected, mv.toStringPrefix());
    }

    @org.junit.jupiter.api.Test
    void testEquals() {
    }

    @org.junit.jupiter.api.Test
    void toISOCode() {
    }

    @org.junit.jupiter.api.Test
    void toISOCodePrefix() {
    }

    @org.junit.jupiter.api.Test
    void compareTo() {
    }

    @org.junit.jupiter.api.Test
    void isValid() {
    }

    @org.junit.jupiter.api.Test
    void convertTo() {
    }

    @org.junit.jupiter.api.Test
    void add() {
    }

    @org.junit.jupiter.api.Test
    void testAdd() {
    }

    @org.junit.jupiter.api.Test
    void subtract() {
    }

    @org.junit.jupiter.api.Test
    void testSubtract() {
    }

    @org.junit.jupiter.api.Test
    void multiply() {
    }

    @org.junit.jupiter.api.Test
    void testMultiply() {
    }

    @org.junit.jupiter.api.Test
    void divide() {
    }

    @org.junit.jupiter.api.Test
    void testDivide() {
    }

    @org.junit.jupiter.api.Test
    void testConcatenation() {
        double expAddAmount = 2.0;
        double expSubAmount = 0.0;
        double expMultAmount = 0.0;
        double expDivAmount = 1e10;

        MoneyValue mvAddResult = new MoneyValue(1.0, MoneyValue.Currency.Dollar);
        MoneyValue mvSubResult = new MoneyValue(1.0, MoneyValue.Currency.Dollar);
        MoneyValue mvMultResult = new MoneyValue(1.0, MoneyValue.Currency.Dollar);
        MoneyValue mvDivResult = new MoneyValue(1.0, MoneyValue.Currency.Dollar);

        MoneyValue mvConst = new MoneyValue(0.1, MoneyValue.Currency.Dollar);

        for(int i = 0; i < 10; ++i)
        {
            mvAddResult = mvAddResult.add(mvConst);
            mvSubResult = mvSubResult.subtract(mvConst);
            mvMultResult = mvMultResult.multiply(mvConst);
            mvDivResult = mvDivResult.divide(mvConst);
        }

        assertEquals(expAddAmount, mvAddResult.getAmount());
        assertEquals(expSubAmount, mvSubResult.getAmount());
        assertEquals(expMultAmount, mvMultResult.getAmount());
        assertEquals(expDivAmount, mvDivResult.getAmount());


    }
}