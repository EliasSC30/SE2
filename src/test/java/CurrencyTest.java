import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CurrencyTest {
    @Test
    void fromSymbol() {
        // Given
        char symbol = '€';

        // When
        Currency result = Currency.fromSymbol(symbol);

        // Then
        assertEquals(Currency.EURO, result);
    }

    @Test
    void fromIsoCode() {
        // Given
        String isoCode = "EUR";

        // When
        Currency result = Currency.fromIsoCode(isoCode);

        // Then
        assertEquals(Currency.EURO, result);
    }
}