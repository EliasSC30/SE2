import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.*;

class MoneyValueTest {

    private final String INVALID_MONEY_VALUE = "Invalid Money Value";
    private final String CURRENCIES_NOT_EQUAL = "Currencies are not equal for operation";
    private final String DIVIDE_BY_ZERO = "Can not divide by zero";

    @Nested
    class testMoneyValueConstructor{
        @Test
        public void testMoneyValueConstructorValidValues() {
            // Given
            BigDecimal amount = new BigDecimal(100.00).setScale(2, RoundingMode.HALF_UP);
            Currency currency = Currency.US_DOLLAR;

            // When
            MoneyValue moneyValue = new MoneyValue(amount, currency);

            // Then
            assertEquals(currency, moneyValue.getCurrency());
        }

        @Test
        public void testMoneyValueConstructorInvalidAmount() {
            // Given
            double amount = Double.NaN;
            Currency currency = Currency.US_DOLLAR;

            // When & Then
            assertThrows(NumberFormatException.class, () -> new MoneyValue(amount, currency));
        }

        @Test
        public void testMoneyValueConstructorAmountValidDoubleValues() {
            // Given
            double amount = 100.0;
            Currency currency = Currency.US_DOLLAR;
            BigDecimal expectedAmount = new BigDecimal(amount).setScale(2, RoundingMode.HALF_UP);

            // When
            MoneyValue moneyValue = new MoneyValue(100, currency);

            // Then
            assertEquals(expectedAmount, moneyValue.getAmount());
            assertEquals(currency, moneyValue.getCurrency());
        }

        @Test
        public void testMoneyValueConstructorAmountValidIntValues() {
            // Given
            int amount = 100;
            Currency currency = Currency.US_DOLLAR;
            BigDecimal expectedAmount = new BigDecimal(amount).setScale(2, RoundingMode.HALF_UP);

            // When
            MoneyValue moneyValue = new MoneyValue(amount, currency);

            // Then
            assertEquals(expectedAmount,moneyValue.getAmount());
            assertEquals(currency, moneyValue.getCurrency());
        }

        @Test
        public void testMoneyValueConstructorAmountValidLongValues() {
            // Given
            long amount = 100;
            Currency currency = Currency.US_DOLLAR;
            BigDecimal expectedAmount = new BigDecimal(amount).setScale(2, RoundingMode.HALF_UP);

            // When
            MoneyValue moneyValue = new MoneyValue(amount, currency);

            // Then
            assertEquals(expectedAmount,moneyValue.getAmount());
            assertEquals(currency, moneyValue.getCurrency());
        }

        @Test
        public void testMoneyValueConstructorNullCurrency() {
            // Given
            double amount = 100.0;
            Currency currency = null;

            // When
            Exception exception =  assertThrows(Exception.class, () -> new MoneyValue(amount, currency));

            // Then
            assertEquals(exception.getClass(), RuntimeException.class);
            assertEquals(INVALID_MONEY_VALUE, exception.getMessage() );
        }

        @Test
        public void testMoneyValueConstructorNullAmount() {
            // Given
            BigDecimal amount = null;
            Currency currency = Currency.US_DOLLAR;

            // When
            Exception exception =  assertThrows(Exception.class, () -> new MoneyValue(amount, currency));

            // Then
            assertEquals(exception.getClass(), RuntimeException.class);
            assertEquals(INVALID_MONEY_VALUE, exception.getMessage() );
        }

        @Test
        public void testMoneyValueConstructorAmountInvalidAmount() {
            // Given
            double amount = Double.NaN;

            // When & Then
            assertThrows(NumberFormatException.class, () -> new MoneyValue(amount, Currency.US_DOLLAR));
        }

        @Test
        public void testMoneyValueConstructorAmountInfiniteAmount() {
            // Given
            double amount = Double.POSITIVE_INFINITY;

            // When & Then
            assertThrows(NumberFormatException.class, () -> new MoneyValue(amount, Currency.US_DOLLAR));
        }

        @Test
        public void testMoneyValueConstructorFrontString() {
            // Given
            String str = "$ 100.00";
            BigDecimal expectedAmount = new BigDecimal(100.00).setScale(2, RoundingMode.HALF_UP);

            // When
            MoneyValue mv = new MoneyValue(str);

            // Then
            assertEquals(expectedAmount, mv.getAmount());
            assertEquals(Currency.US_DOLLAR, mv.getCurrency());
        }

        @Test
        public void testMoneyValueConstructorBackString() {
            // Given
            String str = "100.0 $";
            BigDecimal expectedAmount = new BigDecimal(100.00).setScale(2, RoundingMode.HALF_UP);

            // When
            MoneyValue mv = new MoneyValue(str);

            // Then
            assertEquals(expectedAmount,mv.getAmount());
            assertEquals(Currency.US_DOLLAR, mv.getCurrency());
        }

        @Test
        public void testMoneyValueConstructorInvalidStringOnlyAmount() {
            // Given
            String str = "100.0";

            // When
            Exception exception =  assertThrows(Exception.class, () -> new MoneyValue(str));

            // Then
            assertEquals(exception.getClass(), RuntimeException.class);
            assertEquals(INVALID_MONEY_VALUE, exception.getMessage() );
        }

        @Test
        public void testMoneyValueConstructorInvalidStringInvalidCurrency() {
            // Given
            String str = "100.0 (";

            // When & Then
            // When
            Exception exception =  assertThrows(Exception.class, () -> new MoneyValue(str));

            // Then
            assertEquals(exception.getClass(), RuntimeException.class);
            assertEquals(INVALID_MONEY_VALUE, exception.getMessage() );
        }

        @Test
        public void testMoneyValueConstructorInvalidStringIntegerAmount() {
            // Given
            String str = "100 $";
            BigDecimal expectedAmount = new BigDecimal(100.00).setScale(2, RoundingMode.HALF_UP);

            // When
            MoneyValue mv = new MoneyValue(str);

            // Then
            assertEquals(expectedAmount,mv.getAmount());
            assertEquals(Currency.US_DOLLAR, mv.getCurrency());
        }

        @Test
        public void testMoneyValueConstructorInvalidStringOnlyCurrency() {
            // Given
            String str = "$";

            // When
            Exception exception =  assertThrows(Exception.class, () -> new MoneyValue(str));

            // Then
            assertEquals(exception.getClass(), RuntimeException.class);
            assertEquals(INVALID_MONEY_VALUE, exception.getMessage() );
        }

        @Test
        public void testStringToMoneyValues() {
            // Given
            // Test all currencies
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
                    String amount = "123.45";
                    String input = amount + " " + currencyStr;
                    BigDecimal expectedAmount = new BigDecimal(amount).setScale(2, RoundingMode.HALF_UP);
                    // When
                    MoneyValue result = new MoneyValue(input);
                    // Then
                    assertEquals(expectedAmount, result.getAmount());
                    assertEquals(expectedCurrency, result.getCurrency());
                }
            }
        }
    }

    @Test
    public void testGetCurrency() {
        // Given
        Currency expectedCurrency = Currency.EURO;
        MoneyValue moneyValue = new MoneyValue(100.0, Currency.EURO);

        // When
        Currency actualCurrency = moneyValue.getCurrency();

        // Then
        assertEquals(expectedCurrency, actualCurrency);
    }

    @Test
    public void testGetAmount() {
        // Given
        BigDecimal expectedAmount = BigDecimal.valueOf(100.0);
        MoneyValue moneyValue = new MoneyValue(100.0, Currency.US_DOLLAR);

        // When
        BigDecimal actualAmount = moneyValue.getAmount();

        // Then
        assertEquals(expectedAmount.doubleValue(), actualAmount.doubleValue(), BigDecimal.ZERO.doubleValue());
    }

    @Nested
    class testMoneyValueToString{
        @Test
        public void testToEURStringValid() {
            // Given
            double amount = 123.456;
            Currency currency = Currency.EURO;
            MoneyValue moneyValue = new MoneyValue(amount, currency);
            String expected = "123,46 €";

            // When
            String actual = moneyValue.toString();

            // Then
            assertEquals(expected, actual);
        }

        @Test
        public void testToUSDollarStringValid() {
            // Given
            double amount = 123.456;
            Currency currency = Currency.US_DOLLAR;
            MoneyValue moneyValue = new MoneyValue(amount, currency);
            String expected = "$ 123.46";

            // When
            String actual = moneyValue.toString();

            // Then
            assertEquals(expected, actual);
        }
    }

    @Nested
    class testMoneyValueToISOCode {
        @Test
        public void testToISOCodeEUR() {
            // Given
            double amount = 123.456;
            Currency currency = Currency.EURO;
            MoneyValue moneyValue = new MoneyValue(amount, currency);
            String expected = "123,46 EUR";

            // When
            String actual = moneyValue.toISOCode();

            // Then
            assertEquals(expected, actual);
        }

        @Test
        public void testToISOCode() {
            // Given
            double amount = 123.456;
            Currency currency = Currency.US_DOLLAR;
            MoneyValue moneyValue = new MoneyValue(amount, currency);
            String expected = "123.46 USD";

            // When
            String actual = moneyValue.toISOCode();

            // Then
            assertEquals(expected, actual);
        }
    }

    @Test
    void testHashCode(){
        // Given
        MoneyValue oneDollar = new MoneyValue(1.0, Currency.US_DOLLAR);

        //When & Then
        assertEquals(oneDollar.hashCode(), Objects.hash(1.0, Currency.US_DOLLAR.getIsoCode()));
    }

    @Nested
    class testMoneyValueToISOCodePrefix{
        @Test
        public void testToISOCodePrefixJAPYEN() {
            // Given
            double amount = 100.0;
            Currency currency = Currency.JAPANESE_YEN;
            MoneyValue moneyValue = new MoneyValue(amount, currency);
            String expected = "JPY 100.00";

            // When
            String actual = moneyValue.toISOCodePrefix();

            // Then
            assertEquals(expected, actual);
        }

        @Test
        public void testToISOCodePrefixEUR() {
            // Given
            double amount = 100.0;
            Currency currency = Currency.EURO;
            MoneyValue moneyValue = new MoneyValue(amount, currency);
            String expected = "EUR 100,00";

            // When
            String actual = moneyValue.toISOCodePrefix();

            // Then
            assertEquals(expected, actual);
        }
    }

    @Nested
    class testMoneyValueEqualsTest {
        @Test
        public void testEqualsSameObject() {
            // Given
            MoneyValue moneyValue = new MoneyValue(100.0, Currency.US_DOLLAR);

            // When & Then
            assertTrue(moneyValue.equals(moneyValue));
        }

        @Test
        public void testEqualsDifferentObjects() {
            // Given
            MoneyValue moneyValue1 = new MoneyValue(100.0, Currency.US_DOLLAR);
            MoneyValue moneyValue2 = new MoneyValue(100.0, Currency.US_DOLLAR);

            // When & Then
            assertTrue(moneyValue1.equals(moneyValue2));
            assertTrue(moneyValue2.equals(moneyValue1));
        }

        @Test
        public void testNotEqualsForDifferentAmounts() {
            // Given
            MoneyValue moneyValue1 = new MoneyValue(100.0, Currency.US_DOLLAR);
            MoneyValue moneyValue2 = new MoneyValue(200.0, Currency.US_DOLLAR);

            // When & Then
            assertFalse(moneyValue1.equals(moneyValue2));
            assertFalse(moneyValue2.equals(moneyValue1));
        }

        @Test
        public void testNotEqualsForDifferentCurrencies() {
            // Given
            MoneyValue moneyValue1 = new MoneyValue(100.00, Currency.US_DOLLAR);
            MoneyValue moneyValue2 = new MoneyValue(100.00, Currency.EURO);

            // When & Then
            assertFalse(moneyValue1.equals(moneyValue2));
            assertFalse(moneyValue2.equals(moneyValue1));
        }
    }

    @Nested
    class testMoneyValueCompareToTest {
      @Test
      public void testCompareToSameObject() {
          // Given
          MoneyValue moneyValue1 = new MoneyValue(100.0, Currency.US_DOLLAR);
          MoneyValue moneyValue2 = moneyValue1;

          // When
          int value = moneyValue1.compareTo(moneyValue2);

          // Then
          assertEquals(0, value);
      }

      @Test
      public void testCompareToEqualValues() {
          // Given
          MoneyValue moneyValue1 = new MoneyValue(100.0, Currency.US_DOLLAR);
          MoneyValue moneyValue2 = new MoneyValue(100.0, Currency.EURO);

          // When
          int value = moneyValue1.compareTo(moneyValue2);

          // Then
          assertTrue(value != 0);
      }

      @Test
      public void testCompareToGreaterValue() {
          // Given
          MoneyValue moneyValue1 = new MoneyValue(200.0, Currency.US_DOLLAR);
          MoneyValue moneyValue2 = new MoneyValue(100.0, Currency.EURO);

          // When
          int value = moneyValue1.compareTo(moneyValue2);

          // Then
          assertTrue(value < 0);
      }

      @Test
      public void testCompareToSmallerValue() {
          // Given
          MoneyValue moneyValue1 = new MoneyValue(100.0, Currency.US_DOLLAR);
          MoneyValue moneyValue2 = new MoneyValue(200.0, Currency.EURO);

          // When
          int value = moneyValue1.compareTo(moneyValue2);

          // Then
          assertTrue(value > 0);
      }
  }

    @Nested
    class testMoneyValueAddTest {
        @Test
        public void testAddValidAmounts() {
            // Given
            MoneyValue moneyValue1 = new MoneyValue(100.0, Currency.US_DOLLAR);
            MoneyValue moneyValue2 = new MoneyValue(50.0, Currency.US_DOLLAR);
            MoneyValue expected = new MoneyValue(150.0, Currency.US_DOLLAR);

            // When
            MoneyValue result = moneyValue1.add(moneyValue2);

            // Then
            assertEquals(expected, result);
        }

        @Test
        public void testAddDifferentCurrencies() {
            // Given
            MoneyValue moneyValue1 = new MoneyValue(100.0, Currency.US_DOLLAR);
            MoneyValue moneyValue2 = new MoneyValue(50.0, Currency.EURO);

            // When
            Exception exception =  assertThrows(Exception.class, () -> moneyValue1.add(moneyValue2));

            // Then
            assertEquals(exception.getClass(), RuntimeException.class);
            assertEquals(CURRENCIES_NOT_EQUAL, exception.getMessage() );
        }

        @Test
        public void testAddNegativeValue() {
            // Given
            MoneyValue moneyValue1 = new MoneyValue(100.0, Currency.EURO);
            MoneyValue moneyValue2 = new MoneyValue(-50.0, Currency.EURO);
            MoneyValue expected = new MoneyValue(50, Currency.EURO);

            // When
            MoneyValue result = moneyValue1.add(moneyValue2);

            // Then
            assertEquals(expected, result);
        }

        @Test
        public void testAddNullObject() {
            // Given
            MoneyValue moneyValue1 = new MoneyValue(100.0, Currency.EURO);
            MoneyValue moneyValue2 = null;

            // When
            Exception exception =  assertThrows(Exception.class, () -> moneyValue1.add(moneyValue2));

            // Then
            assertEquals(exception.getClass(), RuntimeException.class);
            assertEquals(INVALID_MONEY_VALUE, exception.getMessage() );
        }

    }

    @Nested
    public class testMoneyValueSubtractTest {
        @Test
        public void testSubtractValidAmounts() {
            // Given
            MoneyValue moneyValue1 = new MoneyValue(100.0, Currency.US_DOLLAR);
            MoneyValue moneyValue2 = new MoneyValue(50.0, Currency.US_DOLLAR);
            MoneyValue expected = new MoneyValue(50.0, Currency.US_DOLLAR);

            // When
            MoneyValue result = moneyValue1.subtract(moneyValue2);

            // Then
            assertEquals(expected, result);
        }

        @Test
        public void testSubtractDifferentCurrencies() {
            // Given
            MoneyValue moneyValue1 = new MoneyValue(100.0, Currency.US_DOLLAR);
            MoneyValue moneyValue2 = new MoneyValue(50.0, Currency.EURO);

            // When
            Exception exception =  assertThrows(Exception.class, () -> moneyValue1.subtract(moneyValue2));

            // Then
            assertEquals(exception.getClass(), RuntimeException.class);
            assertEquals(CURRENCIES_NOT_EQUAL, exception.getMessage() );
        }

        @Test
        public void testSubtractNegativeValue() {
            // Given
            MoneyValue moneyValue1 = new MoneyValue(100.0, Currency.US_DOLLAR);
            MoneyValue moneyValue2 = new MoneyValue(-50.0, Currency.US_DOLLAR);
            MoneyValue expected = new MoneyValue(150.0, Currency.US_DOLLAR);

            // When
            MoneyValue result = moneyValue1.subtract(moneyValue2);

            // Then
            assertEquals(expected, result);
        }

        @Test
        public void testSubtractNullObject() {
            // Given
            MoneyValue moneyValue1 = new MoneyValue(100.0, Currency.EURO);
            MoneyValue moneyValue2 = null;

            // When
            Exception exception =  assertThrows(Exception.class, () -> moneyValue1.subtract(moneyValue2));

            // Then
            assertEquals(exception.getClass(), RuntimeException.class);
            assertEquals(INVALID_MONEY_VALUE, exception.getMessage() );
        }
    }

    @Nested
    public class testMoneyValueMultiplyTest {
        @Test
        public void testMultiplyValidAmounts() {
            // Given
            MoneyValue moneyValue1 = new MoneyValue(10.0, Currency.US_DOLLAR);
            MoneyValue moneyValue2 = new MoneyValue(5.0, Currency.US_DOLLAR);
            MoneyValue expected = new MoneyValue(50.0, Currency.US_DOLLAR);

            // When
            MoneyValue result = moneyValue1.multiply(moneyValue2);

            // Then
            assertEquals(expected, result);
        }

        @Test
        public void testMultiplyDifferentCurrencies() {
            // Given
            MoneyValue moneyValue1 = new MoneyValue(10.0, Currency.US_DOLLAR);
            MoneyValue moneyValue2 = new MoneyValue(5.0, Currency.EURO);

            // When
            Exception exception =  assertThrows(Exception.class, () -> moneyValue1.multiply(moneyValue2));

            // Then
            assertEquals(exception.getClass(), RuntimeException.class);
            assertEquals(CURRENCIES_NOT_EQUAL, exception.getMessage() );
        }

        @Test
        public void testMultiplyNegativeValue() {
            // Given
            MoneyValue moneyValue1 = new MoneyValue(-10.0, Currency.US_DOLLAR);
            MoneyValue moneyValue2 = new MoneyValue(5.0, Currency.US_DOLLAR);
            MoneyValue expected = new MoneyValue(-50.0, Currency.US_DOLLAR);

            // When
            MoneyValue result = moneyValue1.multiply(moneyValue2);

            // Then
            assertEquals(expected, result);
        }

        @Test
        public void testMultiplyNullObject() {
            // Given
            MoneyValue moneyValue1 = new MoneyValue(100.0, Currency.EURO);
            MoneyValue moneyValue2 = null;

            // When
            Exception exception =  assertThrows(Exception.class, () -> moneyValue1.multiply(moneyValue2));

            // Then
            assertEquals(exception.getClass(), RuntimeException.class);
            assertEquals(INVALID_MONEY_VALUE, exception.getMessage() );
        }

        @Test
        public void testMultiplyZeroValue() {
            // Given
            MoneyValue moneyValue1 = new MoneyValue(0.0, Currency.US_DOLLAR);
            MoneyValue moneyValue2 = new MoneyValue(5.0, Currency.US_DOLLAR);
            MoneyValue expected = new MoneyValue(0.0, Currency.US_DOLLAR);

            // When
            MoneyValue result = moneyValue1.multiply(moneyValue2);

            // Then
            assertEquals(expected, result);
        }
    }

    @Nested
    public class testMoneyValueDivideTest {
        @Test
        public void testDivideValidAmounts() {
            // Given
            MoneyValue moneyValue1 = new MoneyValue(100.0, Currency.US_DOLLAR);
            MoneyValue moneyValue2 = new MoneyValue(50.0, Currency.US_DOLLAR);
            MoneyValue expected = new MoneyValue(2.0, Currency.US_DOLLAR);

            // When
            MoneyValue result = moneyValue1.divide(moneyValue2);

            // Then
            assertEquals(expected, result);
        }

        @Test
        public void testDivideDifferentCurrencies() {
            // Given
            MoneyValue moneyValue1 = new MoneyValue(100.0, Currency.US_DOLLAR);
            MoneyValue moneyValue2 = new MoneyValue(50.0, Currency.EURO);
            MoneyValue expected = new MoneyValue(2.0, Currency.US_DOLLAR);

            // When
            Exception exception =  assertThrows(Exception.class, () -> moneyValue1.divide(moneyValue2));

            // Then
            assertEquals(exception.getClass(), RuntimeException.class);
            assertEquals(CURRENCIES_NOT_EQUAL, exception.getMessage() );
        }

        @Test
        public void testDivideNegativeValue() {
            // Given
            MoneyValue moneyValue1 = new MoneyValue(100.0, Currency.US_DOLLAR);
            MoneyValue moneyValue2 = new MoneyValue(-50.0, Currency.US_DOLLAR);
            MoneyValue expected = new MoneyValue(-2.0, Currency.US_DOLLAR);

            // When
            MoneyValue result = moneyValue1.divide(moneyValue2);

            // Then
            assertEquals(expected, result);
        }

        @Test
        public void testDivideZeroValue() {
            // Given
            MoneyValue moneyValue1 = new MoneyValue(100.0, Currency.US_DOLLAR);
            MoneyValue moneyValue2 = new MoneyValue(0.0, Currency.US_DOLLAR);

            // When
            Exception exception =  assertThrows(Exception.class, () -> moneyValue1.divide(moneyValue2));

            // Then
            assertEquals(exception.getClass(), RuntimeException.class);
            assertEquals(DIVIDE_BY_ZERO, exception.getMessage() );
        }
    }

    @Nested
    public class testMoneyValueSynchronizedTests {
        private int numberOfThreads;
        private ExecutorService service;
        private CountDownLatch latch;

        @BeforeEach()
        void beforeEach() {
            numberOfThreads = 10;
            service = Executors.newFixedThreadPool(10);
            latch = new CountDownLatch(numberOfThreads);
        }

        @Test
        public void testAddMultiThreadingSafe() throws InterruptedException {
            MoneyValue moneyValue1 = new MoneyValue(0.0, Currency.EURO);
            MoneyValue moneyValue2 = new MoneyValue(1.0, Currency.EURO);
            MoneyValue expectedResult = new MoneyValue(10.0, Currency.EURO);

            for (int i = 0; i < numberOfThreads; i++) {
                service.submit(() -> {
                    moneyValue1.add(moneyValue2);
                    latch.countDown();
                });
            }
            latch.await();
            assertEquals(expectedResult, moneyValue1);
        }

        @Test
        public void testSubtractMultiThreadingSafe() throws InterruptedException {
            MoneyValue moneyValue1 = new MoneyValue(10.0, Currency.EURO);
            MoneyValue moneyValue2 = new MoneyValue(1.0, Currency.EURO);
            MoneyValue expectedResult = new MoneyValue(0.0, Currency.EURO);

            for (int i = 0; i < numberOfThreads; i++) {
                service.submit(() -> {
                    moneyValue1.subtract(moneyValue2);
                    latch.countDown();
                });
            }
            latch.await();
            assertEquals(expectedResult, moneyValue1);
        }

        @Test
        public void testMultiplyMultiThreadingSafe() throws InterruptedException {
            MoneyValue moneyValue1 = new MoneyValue(1.0, Currency.EURO);
            MoneyValue moneyValue2 = new MoneyValue(2.0, Currency.EURO);
            MoneyValue expectedResult = new MoneyValue(1024.0, Currency.EURO);

            for (int i = 0; i < numberOfThreads; i++) {
                service.submit(() -> {
                    moneyValue1.multiply(moneyValue2);
                    latch.countDown();
                });
            }
            latch.await();
            assertEquals(expectedResult, moneyValue1);
        }

        @Test
        public void testDivideMultiThreadingSafe() throws InterruptedException {
            MoneyValue moneyValue1 = new MoneyValue(2048.0, Currency.EURO);
            MoneyValue moneyValue2 = new MoneyValue(2.0, Currency.EURO);
            MoneyValue expectedResult = new MoneyValue(2.0, Currency.EURO);

            for (int i = 0; i < numberOfThreads; i++) {
                service.submit(() -> {
                    moneyValue1.divide(moneyValue2);
                    latch.countDown();
                });
            }
            latch.await();
            assertEquals(expectedResult, moneyValue1);
        }


        @Test
        public void testAddMultiThreadingSafeChaining() throws InterruptedException {
            MoneyValue moneyValue1 = new MoneyValue(0.0, Currency.EURO);
            MoneyValue moneyValue2 = new MoneyValue(1.0, Currency.EURO);
            MoneyValue expectedResult = new MoneyValue(20.0, Currency.EURO);

            for (int i = 0; i < numberOfThreads; i++) {
                service.submit(() -> {
                    moneyValue1.add(moneyValue2).add(moneyValue2);
                    latch.countDown();
                });
            }
            latch.await();
            assertEquals(expectedResult, moneyValue1);
        }

        @Test
        public void testSubtractMultiThreadingSafeChaining() throws InterruptedException {
            MoneyValue moneyValue1 = new MoneyValue(40.0, Currency.EURO);
            MoneyValue moneyValue2 = new MoneyValue(1.0, Currency.EURO);
            MoneyValue expectedResult = new MoneyValue(20.0, Currency.EURO);

            for (int i = 0; i < numberOfThreads; i++) {
                service.submit(() -> {
                    moneyValue1.subtract(moneyValue2).subtract(moneyValue2);
                    latch.countDown();
                });
            }
            latch.await();
            assertEquals(expectedResult, moneyValue1);
        }

        @Test
        public void testMultiplyMultiThreadingSafeChaining() throws InterruptedException {
            MoneyValue moneyValue1 = new MoneyValue(1.0, Currency.EURO);
            MoneyValue moneyValue2 = new MoneyValue(2.0, Currency.EURO);
            MoneyValue expectedResult = new MoneyValue(1048576.0, Currency.EURO);

            for (int i = 0; i < numberOfThreads; i++) {
                service.submit(() -> {
                    moneyValue1.multiply(moneyValue2).multiply(moneyValue2);
                    latch.countDown();
                });
            }
            latch.await();
            assertEquals(expectedResult, moneyValue1);
        }

        @Test
        public void testDivideMultiThreadingSafeChaining() throws InterruptedException {
            MoneyValue moneyValue1 = new MoneyValue(2097152.0, Currency.EURO);
            MoneyValue moneyValue2 = new MoneyValue(2.0, Currency.EURO);
            MoneyValue expectedResult = new MoneyValue(2.0, Currency.EURO);

            for (int i = 0; i < numberOfThreads; i++) {
                service.submit(() -> {
                    moneyValue1.divide(moneyValue2).divide(moneyValue2);
                    latch.countDown();
                });
            }
            latch.await();
            assertEquals(expectedResult, moneyValue1);
        }

        @Test
        public void testMultiThreadingSafeAllOperationsChaining() throws InterruptedException {
            MoneyValue moneyValue1 = new MoneyValue(100.0, Currency.EURO);
            MoneyValue moneyValue2 = new MoneyValue(2.0, Currency.EURO);
            MoneyValue expectedResult = new MoneyValue(100.0, Currency.EURO);

            for (int i = 0; i < numberOfThreads; i++) {
                service.submit(() -> {
                    moneyValue1.add(moneyValue2).subtract(moneyValue2).multiply(moneyValue2).divide(moneyValue2);
                    latch.countDown();
                });
            }
            latch.await();
            assertEquals(expectedResult, moneyValue1);
        }
    }

    @Nested
    public class testChaining {
        @Test
        public void testChainingAdd() {
            // Given
            MoneyValue moneyValue1 = new MoneyValue(100.0, Currency.US_DOLLAR);
            MoneyValue moneyValue2 = new MoneyValue(50.0, Currency.US_DOLLAR);
            MoneyValue expected = new MoneyValue(200.0, Currency.US_DOLLAR);

            // When
            moneyValue1.add(moneyValue2).add(moneyValue2);

            // Then
            assertEquals(expected, moneyValue1);
        }

        @Test
        public void testChainingSubtract() {
            // Given
            MoneyValue moneyValue1 = new MoneyValue(100.0, Currency.US_DOLLAR);
            MoneyValue moneyValue2 = new MoneyValue(50.0, Currency.US_DOLLAR);
            MoneyValue expected = new MoneyValue(0.0, Currency.US_DOLLAR);

            // When
            moneyValue1.subtract(moneyValue2).subtract(moneyValue2);

            // Then
            assertEquals(expected, moneyValue1);
        }

        @Test
        public void testChainingMultiply() {
            // Given
            MoneyValue moneyValue1 = new MoneyValue(100.0, Currency.US_DOLLAR);
            MoneyValue moneyValue2 = new MoneyValue(2.0, Currency.US_DOLLAR);
            MoneyValue expected = new MoneyValue(400.0, Currency.US_DOLLAR);

            // When
            moneyValue1.multiply(moneyValue2).multiply(moneyValue2);

            // Then
            assertEquals(expected, moneyValue1);
        }

        @Test
        public void testChainingDivide() {
            // Given
            MoneyValue moneyValue1 = new MoneyValue(100.0, Currency.US_DOLLAR);
            MoneyValue moneyValue2 = new MoneyValue(2.0, Currency.US_DOLLAR);
            MoneyValue expected = new MoneyValue(25.0, Currency.US_DOLLAR);

            // When
            moneyValue1.divide(moneyValue2).divide(moneyValue2);

            // Then
            assertEquals(expected, moneyValue1);
        }

        @Test
        public void testChainingAddAndSubtract() {
            // Given
            MoneyValue moneyValue1 = new MoneyValue(100.0, Currency.US_DOLLAR);
            MoneyValue moneyValue2 = new MoneyValue(10.0, Currency.US_DOLLAR);
            MoneyValue expected = new MoneyValue(100.0, Currency.US_DOLLAR);

            // When
            moneyValue1.add(moneyValue2).subtract(moneyValue2);

            // Then
            assertEquals(expected, moneyValue1);
        }

        @Test
        public void testChainingMultiplyAndDivide() {
            // Given
            MoneyValue moneyValue1 = new MoneyValue(100.0, Currency.US_DOLLAR);
            MoneyValue moneyValue2 = new MoneyValue(10.0, Currency.US_DOLLAR);
            MoneyValue expected = new MoneyValue(100.0, Currency.US_DOLLAR);

            // When
            moneyValue1.multiply(moneyValue2).divide(moneyValue2);

            // Then
            assertEquals(expected, moneyValue1);
        }

        @Test
        public void testChainingAllOperations() {
            // Given
            MoneyValue moneyValue1 = new MoneyValue(100.0, Currency.US_DOLLAR);
            MoneyValue moneyValue2 = new MoneyValue(10.0, Currency.US_DOLLAR);
            MoneyValue expected = new MoneyValue(100.0, Currency.US_DOLLAR);

            // When
            moneyValue1.add(moneyValue2).subtract(moneyValue2).multiply(moneyValue2).divide(moneyValue2);

            // Then
            assertEquals(expected, moneyValue1);
        }
    }
}