import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MoneyValueFactoryTest {

     @Test
    public void testCreateMoneyValueWithValidAmount() {
        // Given
        double amount = 100.50;
        MoneyValue.Currency currency = MoneyValue.Currency.Dollar;

        // When
        MoneyValue moneyValue = MoneyValueFactory.createMoneyValue(amount, currency);

        // Then
        assertEquals(amount, moneyValue.getAmount());
        assertEquals(currency, moneyValue.getCurrency());
    }

     @Test
    public void testCreateMoneyValueWithInvalidAmount() {
        // Given
        double amount = -10.0;
        MoneyValue.Currency currency = MoneyValue.Currency.Euro;

        // When and Then
        assertThrows(MoneyValue.InvalidMoneyValueException.class, () -> {
            MoneyValueFactory.createMoneyValue(amount, currency);
        });
    }

     @Test
    public void testCreateMoneyValueWithValidAmountString() {
        // Given
        String validString = "100.50 USD";

        // When
        MoneyValue moneyValue = MoneyValueFactory.createMoneyValue(validString);

        // Then
        assertEquals(100.50, moneyValue.getAmount());
        assertEquals(MoneyValue.Currency.Dollar, moneyValue.getCurrency());
    }

    @Test
    public void testCreateMoneyValueWithInvalidAmountString() {
        // Given
        String invalidString = "invalid amount EUR";

        // When and Then
        assertThrows(MoneyValue.InvalidMoneyValueException.class, () -> {
            MoneyValueFactory.createMoneyValue(invalidString);
        });
    }

}