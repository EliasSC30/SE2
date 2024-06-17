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
    class ConverterStringToMoneyValueTest{
        @Test
        public void testStringToMoneyValueCurrencyFrontString() {
            // Given
            String str = "$ 100.00";

            // When
            MoneyValue mv = Converter.stringToMoneyValue(str);

            // Then
            assertEquals(100.0, mv.getAmount());
            assertEquals(MoneyValue.NEUTRAL_CURRENCY, mv.getCurrency());
        }

        @Test
        public void testStringToMoneyValueCurrencyBackString() {
            // Given
            String str = "100.0 $";

            // When
            MoneyValue mv = Converter.stringToMoneyValue(str);

            // Then
            assertEquals(100.0, mv.getAmount());
            assertEquals(MoneyValue.NEUTRAL_CURRENCY, mv.getCurrency());
        }

        @Test
        public void testStringToMoneyValues() {
            Map<String, Currency>[] currencySigns = new HashMap[]{
                    new HashMap<String, Currency>() {{
                        put("$", Currency.US_DOLLAR);
                        put("USD", Currency.US_DOLLAR);
                    }},
                    new HashMap<String, Currency>() {{
                        put("€", Currency.EURO);
                        put("EUR", Currency.EURO);
                    }},
                    new HashMap<String, Currency>() {{
                        put("¥", Currency.JAPANESE_YEN);
                        put("JPY", Currency.JAPANESE_YEN);
                    }},
                    new HashMap<String, Currency>() {{
                        put("£", Currency.BRITISH_POUND);
                        put("GBP", Currency.BRITISH_POUND);
                    }}
            };

            for (Map<String, Currency> map : currencySigns) {
                for (Map.Entry<String, Currency> entry : map.entrySet()) {
                    String currencyStr = entry.getKey();
                    Currency expectedCurrency = entry.getValue();
                    String input = "123.45 " + currencyStr;
                    MoneyValue result = Converter.stringToMoneyValue(input);
                    assertEquals(123.45, result.getAmount());
                    assertEquals(expectedCurrency, result.getCurrency());
                }
            }
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