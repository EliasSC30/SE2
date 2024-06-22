import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CurrencyTest {
    @Test
    void getIsoCode() {
        // Given
        String isoCode = "EUR";

        // When
        String result = Currency.EURO.getIsoCode();

        // Then
        assertEquals(isoCode, result);
    }

    @Test
    void getSymbol() {
        // Given
        String symbol = "€";

        // When
        String result = Currency.EURO.getSymbol();

        // Then
        assertEquals(symbol, result);
    }

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