import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.HashMap;


import static org.junit.jupiter.api.Assertions.*;

class ConverterTest {

    private Currency dollar;
    private Currency euro;

    @BeforeEach
    void setUp() {
        dollar = Currency.US_DOLLAR;
        euro = Currency.EURO;
    }

    @Nested
    class ConverterConvertToNeutralTest{
        @Test
        public void testConvertToNeutralValidMoneyValue() {
            // Given
            MoneyValue mv = new MoneyValue(100.0, dollar);

            // When
            MoneyValue neutralValue = Converter.convertToNeutral(mv);

            // Then
            assertEquals(100.0, neutralValue.getAmount());
            assertEquals(MoneyValue.NEUTRAL_CURRENCY, neutralValue.getCurrency());
        }

        @Test
        public void testConvertToNeutralNullMoneyValue() {
            // Given & When & Then
            assertThrows(MoneyValue.InvalidMoneyValueException.class,
                    () -> Converter.convertToNeutral(null));
        }
    }

    @Nested
    class ConverterConvertToTest{
        @Test
        public void testConvertToValidMoneyValue() {
            // Given
            MoneyValue mv = new MoneyValue(100.0, dollar);

            // When
            MoneyValue euroValue = Converter.convertTo(mv, euro);

            // Then
            assertTrue( euroValue.getAmount() < mv.getAmount());
            assertEquals(euro, euroValue.getCurrency());
        }

        @Test
        public void testConvertToInvalidMoneyValue() {
            // Given
            MoneyValue mv = new MoneyValue(100.0, dollar);

            // When & Then
            assertThrows(MoneyValue.InvalidMoneyValueException.class,
                    () -> Converter.convertTo(null, euro));

            assertThrows(MoneyValue.InvalidMoneyValueException.class,
                    () -> Converter.convertTo(mv, null));
        }
    }


    @Nested
    class ConverterRoundTwoPlacesTest{
        @Test
        public void testRoundTwoPlacesWithPositiveValue() {
            // Given
            double a = 1.49999999;

            // When
            double aRounded = Converter.roundTwoPlaces(a);

            // Then
            assertEquals(1.50, aRounded);
        }

        @Test
        public void testRoundTwoPlacesWithNegativeValue() {
            // Given
            double b = -1.4999999;

            // When
            double bRounded = Converter.roundTwoPlaces(b);

            // Then
            assertEquals(-1.50, bRounded);
        }

        @Test
        public void testRoundTwoPlacesRoundDown() {
            // Given
            double c = 0.004;

            // When
            double cRounded = Converter.roundTwoPlaces(c);

            // Then
            assertEquals(0.0, cRounded);
        }

        @Test
        public void testRoundTwoPlacesRoundUp() {
            // Given
            double d = 0.005;

            // When
            double dRounded = Converter.roundTwoPlaces(d);

            // Then
            assertEquals(0.01, dRounded);
        }

        @Test
        public void testRoundTwoPlacesNaN() {
            // Given
            double naN = Double.NaN;

            // When
            double naNRounded = Converter.roundTwoPlaces(naN);

            // Then
            assertEquals(naN, naNRounded);
        }
    }
}