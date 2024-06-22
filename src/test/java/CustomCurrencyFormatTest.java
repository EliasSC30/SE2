import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.text.FieldPosition;
import java.text.ParsePosition;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

class CustomCurrencyFormatTest {

    @Nested
    class testFormat {
        @Test
        public void testFormatDouble() {
            // Given
            double amount = 5.45;
            Locale local = Locale.US;
            String symbol = "$";
            CustomCurrencyFormat customCurrencyFormat = new CustomCurrencyFormat(local, symbol);
            StringBuffer result = new StringBuffer();
            FieldPosition pos = new FieldPosition(0);
            String expected = "$" + amount;

            // When
            StringBuffer str = customCurrencyFormat.format(amount, result, pos);

            // Then
            assertEquals(expected, str.toString());
        }

        @Test
        public void testFormatLong() {
            // Given
            long amount = 50;
            Locale local = Locale.US;
            String symbol = "$";
            CustomCurrencyFormat customCurrencyFormat = new CustomCurrencyFormat(local, symbol);
            StringBuffer result = new StringBuffer();
            FieldPosition pos = new FieldPosition(0);

            String expected = "$50.00";

            // When
            StringBuffer str = customCurrencyFormat.format(amount, result, pos);

            // Then
            assertEquals(expected, str.toString());
        }

        @Test
        public void testFormatStringBufferNull() {
            // Given
            long amount = 50;
            Locale local = Locale.US;
            String symbol = "$";
            CustomCurrencyFormat customCurrencyFormat = new CustomCurrencyFormat(local, symbol);
            StringBuffer result = null;
            FieldPosition pos = new FieldPosition(0);


            // When
            Exception exception = assertThrows(Exception.class, () -> customCurrencyFormat.format(amount, result, pos));

            // Then
            assertEquals(NullPointerException.class, exception.getClass());
        }

        @Test
        public void testFormatFieldPositionNull() {
            // Given
            double amount = 50.48528488;
            Locale local = Locale.US;
            String symbol = "$";
            CustomCurrencyFormat customCurrencyFormat = new CustomCurrencyFormat(local, symbol);
            StringBuffer result = new StringBuffer();
            FieldPosition pos = null;


            // When
            Exception exception = assertThrows(Exception.class, () -> customCurrencyFormat.format(amount, result, pos));

            // Then
            assertEquals(NullPointerException.class, exception.getClass());
        }

        @Test
        public void testFormatDoubleNull() {
            // Given
            Double amount = null;
            Locale local = Locale.US;
            String symbol = "$";
            CustomCurrencyFormat customCurrencyFormat = new CustomCurrencyFormat(local, symbol);
            StringBuffer result = new StringBuffer();
            FieldPosition pos = null;


            // When
            Exception exception = assertThrows(Exception.class, () -> customCurrencyFormat.format(amount, result, pos));

            // Then
            assertEquals(IllegalArgumentException.class, exception.getClass());
        }
    }
    @Test
    public void testParse() {
        // Given
        Locale local = Locale.US;
        String symbol = "$";
        CustomCurrencyFormat customCurrencyFormat = new CustomCurrencyFormat(local, symbol);

        String x = "$2";
        ParsePosition pos = new ParsePosition(0);
        Number expected = 2;

        // When
        Number result = customCurrencyFormat.parse(x, pos);

        // Then
        assertEquals(expected.doubleValue(), result.doubleValue());
    }

    @Test
    public void testParseStringNull() {
        // Given
        Locale local = Locale.US;
        String symbol = "$";
        CustomCurrencyFormat customCurrencyFormat = new CustomCurrencyFormat(local, symbol);

        String x = null;
        ParsePosition pos = new ParsePosition(0);


        // When
        Exception exception = assertThrows(Exception.class, () -> customCurrencyFormat.parse(x, pos));

        // Then
        assertEquals(NullPointerException.class, exception.getClass());
    }

    @Test
    public void testParsePosNull() {
        // Given
        Locale local = Locale.US;
        String symbol = "$";
        CustomCurrencyFormat customCurrencyFormat = new CustomCurrencyFormat(local, symbol);

        String x = "$2";
        ParsePosition pos = null;

        // When
        Exception exception = assertThrows(Exception.class, () -> customCurrencyFormat.parse(x, pos));

        // Then
        assertEquals(NullPointerException.class, exception.getClass());
    }
}