import org.junit.jupiter.api.Test;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

class CurrencyFormatterTest {

    @Test
    public void testFormatCurrency() {
        // Given
        MoneyValue mv = new MoneyValue(100.00, Currency.US_DOLLAR);
        char nonBreakingSpace = (char)160;
        String expected = "100,00" + nonBreakingSpace + "$";

        // When
        String formatted = CurrencyFormatter.formatCurrency(mv);

        // Then
        assertEquals(expected, formatted);
    }

    @Test
    public void testFormatCurrencyLocaleUS() {
        // Given
        MoneyValue mv = new MoneyValue(100.00, Currency.EURO);
        char nonBreakingSpace = (char)160;
        String expected = "100,00" + nonBreakingSpace + "€";

        // When
        String formatted = CurrencyFormatter.formatCurrency(mv, Locale.GERMAN);

        // Then
        assertEquals(expected, formatted);
    }

    @Test
    public void testToISOCode() {
        // Given
        MoneyValue mv = new MoneyValue(100.00, Currency.EURO);
        char nonBreakingSpace = (char)160;
        String expected = "100,00 EUR";

        // When
        String formatted = CurrencyFormatter.formatISOCode(mv);

        // Then
        assertEquals(expected, formatted);
    }

    @Test
    public void testToISOCodeLocaleUS() {
        // Given
        MoneyValue mv = new MoneyValue(100.00, Currency.EURO);
        char nonBreakingSpace = (char)160;
        String expected = "100,00 EUR";

        // When
        String formatted = CurrencyFormatter.formatISOCode(mv, Locale.GERMAN);

        // Then
        assertEquals(expected, formatted);
    }



}