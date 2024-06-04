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
}