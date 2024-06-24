import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

import static org.junit.jupiter.api.Assertions.*;

class FixedExchangeRateProviderTest {

    private ExchangeRateProvider exchangeRateProvider;

    @BeforeEach
    public void init() {
        exchangeRateProvider = new FixedExchangeRateProvider();
    }


    @Test
    public void testGetExchangeRateSameCurrencies() {
        // Given
        Currency from = Currency.US_DOLLAR;
        Currency to = Currency.US_DOLLAR;
        ExchangeRateProvider.ExchangeRateType exchangeRateType = ExchangeRateProvider.ExchangeRateType.REALTIME;
        double expectedExchangeRate = 1;

        // When
        double result = exchangeRateProvider.getExchangeRate(from, to, exchangeRateType);

        // Then
        assertEquals(expectedExchangeRate, result);
    }

    @Test
    public void testGetExchangeRateRealTime() {
       // Given
       Currency from = Currency.US_DOLLAR;
       Currency to = Currency.EURO;
       ExchangeRateProvider.ExchangeRateType exchangeRateType = ExchangeRateProvider.ExchangeRateType.REALTIME;
       double expectedExchangeRate = 0.93;

       // When
       double result = exchangeRateProvider.getExchangeRate(from, to, exchangeRateType);

       // Then
       assertEquals(expectedExchangeRate, result);
   }

    @Test
    public void testGetExchangeRateMonthly() {
        // Given
        Currency from = Currency.US_DOLLAR;
        Currency to = Currency.EURO;
        ExchangeRateProvider.ExchangeRateType exchangeRateType = ExchangeRateProvider.ExchangeRateType.MONTHLY;
        double expectedExchangeRate = 0.92;

        // When
        double result = exchangeRateProvider.getExchangeRate(from, to, exchangeRateType);

        // Then
        assertEquals(expectedExchangeRate, result);
    }

    @Test
    public void testGetExchangeRateDaily() {
        // Given
        Currency from = Currency.US_DOLLAR;
        Currency to = Currency.EURO;
        ExchangeRateProvider.ExchangeRateType exchangeRateType = ExchangeRateProvider.ExchangeRateType.DAILY;
        double expectedExchangeRate = 0.91;

        // When
        double result = exchangeRateProvider.getExchangeRate(from, to, exchangeRateType);

        // Then
        assertEquals(expectedExchangeRate, result);
    }

    @Test
    public void testGetExchangeRateTypeNull() {
        // Given
        Currency from = Currency.US_DOLLAR;
        Currency to = Currency.EURO;
        ExchangeRateProvider.ExchangeRateType exchangeRateType = null;

        // When
        Exception exception = assertThrows(Exception.class, () -> exchangeRateProvider.getExchangeRate(from, to, exchangeRateType));

        // Then
        assertEquals(NullPointerException.class, exception.getClass());
    }

    @Test
    public void testGetExchangeRateFromCurrencyNull() {
        // Given
        Currency from = null;
        Currency to = Currency.EURO;
        ExchangeRateProvider.ExchangeRateType exchangeRateType = ExchangeRateProvider.ExchangeRateType.REALTIME;

        // When
        Exception exception = assertThrows(Exception.class, () -> exchangeRateProvider.getExchangeRate(from, to, exchangeRateType));

        // Then
        assertEquals(exception.getClass(), NullPointerException.class);
    }

    @Test
    public void testGetExchangeRateToCurrencyNull() {
        // Given
        Currency from = Currency.US_DOLLAR;
        Currency to = null;
        ExchangeRateProvider.ExchangeRateType exchangeRateType = ExchangeRateProvider.ExchangeRateType.REALTIME;

        // When
        Exception exception = assertThrows(Exception.class, () -> exchangeRateProvider.getExchangeRate(from, to, exchangeRateType));

        // Then
        assertEquals(exception.getClass(), NullPointerException.class);
    }
}