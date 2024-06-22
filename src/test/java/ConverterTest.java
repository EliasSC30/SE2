import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static org.junit.jupiter.api.Assertions.*;
        import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ConverterTest {
    @Mock
    private ExchangeRateProvider exchangeRateProvider;

    @InjectMocks
    private Converter converter;

    private MoneyValue mv;

    @BeforeEach
    public void setUp() {
        mv = new MoneyValue(100.0, Currency.US_DOLLAR);
    }

    @Nested
    class testConverterConvertToNullValues {
        @Test
        public void testConvertToWithNullMoneyValue() {
            // Given & When
            Exception exception = assertThrows(Exception.class, () -> converter.convertTo(null, Currency.EURO));

            // Then
            assertEquals(RuntimeException.class, exception.getClass());
            assertEquals("Invalid Money Value", exception.getMessage());
        }

        @Test
        public void testConvertToWithNullCurrency() {
            // Given & When
            Exception exception = assertThrows(Exception.class, () -> converter.convertTo(mv, null));

            // Then
            assertEquals(RuntimeException.class, exception.getClass());
            assertEquals("Converter can not be null", exception.getMessage());
        }
    }

    @Nested
    class testConverterConvertTo {
        @Test
        public void testConvertTo() {
            // Given
            when(exchangeRateProvider.getExchangeRate(Currency.US_DOLLAR, Currency.EURO, ExchangeRateProvider.ExchangeRateType.REALTIME))
                    .thenReturn(0.93);
            BigDecimal expectedAmount = new BigDecimal(93).setScale(2, RoundingMode.HALF_UP);

            // When
            MoneyValue result = converter.convertTo(mv, Currency.EURO);

            // Then
            assertNotNull(result);
            assertEquals(Currency.EURO, result.getCurrency());
            assertEquals(expectedAmount, result.getAmount());
        }
    }

    @Nested
    class testConverterConvertToWithExchangeRateType {
        @Test
        public void testConvertToWithTypeRealTime() {
            // Given
            when(exchangeRateProvider.getExchangeRate(Currency.US_DOLLAR, Currency.EURO, ExchangeRateProvider.ExchangeRateType.REALTIME))
                    .thenReturn(0.85);
            BigDecimal expectedAmount = new BigDecimal(85).setScale(2, RoundingMode.HALF_UP);

            // When
            MoneyValue resultRealtime = converter.convertTo(mv, Currency.EURO, ExchangeRateProvider.ExchangeRateType.REALTIME);

            // Then
            assertNotNull(resultRealtime);
            assertEquals(Currency.EURO, resultRealtime.getCurrency());
            assertEquals(expectedAmount, resultRealtime.getAmount());
        }

        @Test
        public void testConvertToWithTypeDaily () {
            // Given
            when(exchangeRateProvider.getExchangeRate(Currency.US_DOLLAR, Currency.EURO, ExchangeRateProvider.ExchangeRateType.DAILY))
                    .thenReturn(0.86);
            BigDecimal expectedAmount = new BigDecimal(86).setScale(2, RoundingMode.HALF_UP);

            // When
            MoneyValue resultDaily = converter.convertTo(mv, Currency.EURO, ExchangeRateProvider.ExchangeRateType.DAILY);

            // Then
            assertNotNull(resultDaily);
            assertEquals(Currency.EURO, resultDaily.getCurrency());
            assertEquals(expectedAmount, resultDaily.getAmount());
        }

        @Test
        public void testConvertToWithTypeMonthly () {
            // Given
            when(exchangeRateProvider.getExchangeRate(Currency.US_DOLLAR, Currency.EURO, ExchangeRateProvider.ExchangeRateType.MONTHLY))
                    .thenReturn(0.90);
            BigDecimal expectedAmount = new BigDecimal(90).setScale(2, RoundingMode.HALF_UP);

            // When
            MoneyValue resultDaily = converter.convertTo(mv, Currency.EURO, ExchangeRateProvider.ExchangeRateType.MONTHLY);

            // Then
            assertNotNull(resultDaily);
            assertEquals(Currency.EURO, resultDaily.getCurrency());
            assertEquals(expectedAmount, resultDaily.getAmount());
        }

        @Test
        public void testConvertToWithTypeCurrencyNull () {
            // When
            Exception exception = assertThrows(Exception.class, () -> converter.convertTo(mv, null, ExchangeRateProvider.ExchangeRateType.MONTHLY));

            // Then
            assertEquals(exception.getClass(), RuntimeException.class);
            assertEquals("Converter can not be null", exception.getMessage());
        }

        @Test
        public void testConvertToWithTypeNull () {
            // When
            Exception exception = assertThrows(Exception.class, () -> converter.convertTo(mv, Currency.EURO, null));

            // Then
            assertEquals(exception.getClass(), RuntimeException.class);
            assertEquals("ExchangeRateType can not be null", exception.getMessage());
        }
    }
}
