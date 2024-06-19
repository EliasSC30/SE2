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
    class ConverterConvertToTest{
        @Test
        public void testConvertToValidMoneyValue() {
            // Given
            MoneyValue mv = new MoneyValue(100.0, dollar);

            // When
            MoneyValue euroValue = Converter.convertTo(mv, euro);

            // Then
            assertTrue( euroValue.getAmount().compareTo(mv.getAmount()) > 0);
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
}