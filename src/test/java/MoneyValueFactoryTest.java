import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class MoneyValueFactoryTest {

     @Test
    public void testCreateMoneyValueWithValidAmount() {
        // Given
        BigDecimal amount = BigDecimal.valueOf(100.0);
        Currency currency = Currency.US_DOLLAR;

        // When
        MoneyValue moneyValue = MoneyValueFactory.createMoneyValue(amount, currency);

        // Then
        assertEquals(amount, moneyValue.getAmount());
        assertEquals(currency, moneyValue.getCurrency());
    }

     @Test
    public void testCreateMoneyValueWithInvalidAmount() {
        // Given
        BigDecimal amount = BigDecimal.valueOf(-100.0);
        Currency currency = Currency.EURO;

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
        assertEquals(BigDecimal.valueOf(100.50), moneyValue.getAmount());
        assertEquals(Currency.US_DOLLAR, moneyValue.getCurrency());
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