import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class MoneyValueTest {

    @Nested
    class testMoneyValueConstructor{
        @Test
        public void testMoneyValueConstructorValidValues() {
            // Given
            double amount = 100.0;
            Currency currency = Currency.US_DOLLAR;

            // When
            MoneyValue moneyValue = new MoneyValue(amount, currency);

            // Then
            assertEquals(amount, moneyValue.getAmount());
            assertEquals(currency, moneyValue.getCurrency());
        }

        @Test
        public void testMoneyValueConstructorInvalidAmount() {
            // Given
            double amount = Double.NaN;
            Currency currency = Currency.US_DOLLAR;

            // When & Then
            assertThrows(MoneyValue.InvalidMoneyValueException.class, () -> new MoneyValue(amount, currency));
        }

        @Test
        public void testMoneyValueConstructorNullCurrency() {
            // Given
            double amount = 100.0;
            Currency currency = null;

            // When & Then
            assertThrows(MoneyValue.InvalidMoneyValueException.class, () -> new MoneyValue(amount, currency));
        }

        @Test
        public void testMoneyValueConstructorAmountValidValues() {
            // Given
            double amount = 100.0;

            // When
            MoneyValue moneyValue = new MoneyValue(amount, Currency.US_DOLLAR);

            // Then
            assertEquals(amount, moneyValue.getAmount());
            assertEquals(Currency.US_DOLLAR, moneyValue.getCurrency());
        }

        @Test
        public void testMoneyValueConstructorNegativeAmountInvalidAmount() {
            // Given
            double amount = -5.0;

            // When & Then
            assertThrows(MoneyValue.InvalidMoneyValueException.class, () -> new MoneyValue(amount, Currency.US_DOLLAR));
        }

        @Test
        public void testMoneyValueConstructorAmountInvalidAmount() {
            // Given
            double amount = Double.NaN;

            // When & Then
            assertThrows(MoneyValue.InvalidMoneyValueException.class, () -> new MoneyValue(amount, Currency.US_DOLLAR));
        }

        @Test
        public void testMoneyValueConstructorAmountInfiniteAmount() {
            // Given
            double amount = Double.POSITIVE_INFINITY;

            // When & Then
            assertThrows(MoneyValue.InvalidMoneyValueException.class, () -> new MoneyValue(amount, Currency.US_DOLLAR));
        }

        @Test
        public void testMoneyValueConstructorFrontString() {
            // Given
            String str = "$ 100.00";

            // When
            MoneyValue mv = new MoneyValue(str);

            // Then
            assertEquals(100.0, mv.getAmount());
            assertEquals(Currency.US_DOLLAR, mv.getCurrency());
        }

        @Test
        public void testMoneyValueConstructorBackString() {
            // Given
            String str = "100.0 $";

            // When
            MoneyValue mv = new MoneyValue(str);

            // Then
            assertEquals(100.0, mv.getAmount());
            assertEquals(Currency.US_DOLLAR, mv.getCurrency());
        }

        @Test
        public void testMoneyValueConstructorInvalidStringOnlyAmount() {
            // Given
            String str = "100.0";

            // When & Then
            assertThrows(MoneyValue.InvalidMoneyValueException.class, () -> new MoneyValue(str));
        }

        @Test
        public void testMoneyValueConstructorInvalidStringNegativeAmount() {
            // Given
            String str = "-100.0 $";

            // When & Then
            assertThrows(MoneyValue.InvalidMoneyValueException.class, () -> new MoneyValue(str));
        }

        @Test
        public void testMoneyValueConstructorInvalidStringInvalidCurrency() {
            // Given
            String str = "100.0 (";

            // When & Then
            assertThrows(MoneyValue.InvalidMoneyValueException.class, () -> new MoneyValue(str));
        }

        @Test
        public void testMoneyValueConstructorInvalidStringIntegerAmount() {
            // Given
            String str = "100 $";

            // When
            MoneyValue mv = new MoneyValue(str);

            // Then
            assertEquals(100.0, mv.getAmount());
            assertEquals(Currency.US_DOLLAR, mv.getCurrency());
        }

        @Test
        public void testMoneyValueConstructorInvalidStringOnlyCurrency() {
            // Given
            String str = "$";

            // When & Then
            assertThrows(MoneyValue.InvalidMoneyValueException.class, () -> new MoneyValue(str));
        }

        @Test
        public void testMoneyValueConstructorStringDifferentCurrencies() {
            // Given
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
                    // When
                    MoneyValue result = new MoneyValue(input);
                    // Then
                    assertEquals(123.45, result.getAmount());
                    assertEquals(expectedCurrency, result.getCurrency());
                }
            }
        }
    }

    @Test
    public void testGetCurrency() {
        // Given
        Currency expectedCurrency = Currency.EURO;
        MoneyValue moneyValue = new MoneyValue(100.0, expectedCurrency);

        // When
        Currency actualCurrency = moneyValue.getCurrency();

        // Then
        assertEquals(expectedCurrency, actualCurrency);
    }

    @Test
    public void testGetAmount() {
        // Given
        double expectedAmount = 100.0;
        MoneyValue moneyValue = new MoneyValue(expectedAmount, Currency.US_DOLLAR);

        // When
        double actualAmount = moneyValue.getAmount();

        // Then
        assertEquals(expectedAmount, actualAmount, 0.0);
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
            String expected = "$ 123,46";

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

    @Nested
    class testMoneyValueToISOCodePrefix{
        @Test
        public void testToISOCodePrefixJAPYEN() {
            // Given
            double amount = 100.0;
            Currency currency = Currency.JAPANESE_YEN;
            MoneyValue moneyValue = new MoneyValue(amount, currency);
            String expected = "JPY 100.0";

            // When
            String actual = moneyValue.toISOCodePrefix();

            // Then
            assertEquals(expected, actual);
        }

        @Test
        public void testToISOCodePrefixEUR() {
            // Given
            double amount = 100.0;
            Currency currency = Currency.JAPANESE_YEN;
            MoneyValue moneyValue = new MoneyValue(amount, currency);
            String expected = "EUR 100,0";

            // When
            String actual = moneyValue.toISOCodePrefix();

            // Then
            assertEquals(expected, actual);
        }
    }

    @Nested
    class MoneyValueEqualsTest {
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
        public void testEqualsForSameAmountsDifferentCurrencies() {
            // Given
            MoneyValue moneyValue1 = new MoneyValue(100.0, Currency.US_DOLLAR);
            MoneyValue moneyValue2 = moneyValue1.convertTo(Currency.EURO);

            // When & Then
            assertTrue(moneyValue1.equals(moneyValue2));
            assertTrue(moneyValue2.equals(moneyValue1));
        }

        @Test
        public void testNotEqualsForDifferentCurrencies() {
            // Given
            MoneyValue moneyValue1 = new MoneyValue(100.0, Currency.US_DOLLAR);
            MoneyValue moneyValue2 = new MoneyValue(100.0, Currency.EURO);

            // When & Then
            assertFalse(moneyValue1.equals(moneyValue2));
            assertFalse(moneyValue2.equals(moneyValue1));
        }
    }

  @Nested
  class MoneyValueCompareToTest {
      @Test
      public void testCompareToSameObject() {
          // Given
          MoneyValue moneyValue1 = new MoneyValue(100.0, Currency.US_DOLLAR);
          MoneyValue moneyValue2 = moneyValue1;

          // When & Then
          assertEquals(0, moneyValue1.compareTo(moneyValue2));
      }
      @Test
      public void testCompareToEqualValues() {
          // Given
          MoneyValue moneyValue1 = new MoneyValue(100.0, Currency.US_DOLLAR);
          MoneyValue moneyValue2 = new MoneyValue(100.0, Currency.EURO);

          // When & Then
          assertTrue(moneyValue1.compareTo(moneyValue2) != 0);
      }
      @Test
      public void testCompareToEqualValuesWithDifferentCurrencies() {
          // Given
          MoneyValue moneyValue1 = new MoneyValue(100.0, Currency.US_DOLLAR);
          MoneyValue moneyValue2 = moneyValue1.convertTo(Currency.EURO);

          // When & Then
          assertEquals(0, moneyValue1.compareTo(moneyValue2));
      }
      @Test
      public void testCompareToGreaterValue() {
          // Given
          MoneyValue moneyValue1 = new MoneyValue(200.0, Currency.US_DOLLAR);
          MoneyValue moneyValue2 = new MoneyValue(100.0, Currency.EURO);

          // When & Then
          assertTrue(moneyValue1.compareTo(moneyValue2) > 0);
      }
      @Test
      public void testCompareToSmallerValue() {
          // Given
          MoneyValue moneyValue1 = new MoneyValue(100.0, Currency.US_DOLLAR);
          MoneyValue moneyValue2 = new MoneyValue(200.0, Currency.EURO);

          // When & Then
          assertTrue(moneyValue1.compareTo(moneyValue2) < 0);
      }
  }

    @Test
    void isValid() {
        // Given
        MoneyValue valid = new MoneyValue(1.0, Currency.US_DOLLAR);

        // When & Then
        assertTrue(valid.isValid());
    }

   @Nested
    public class MoneyValueConvertToTest {
        @Test
        public void testConvertToSameCurrency() {
            // Given
            MoneyValue moneyValue = new MoneyValue(100.0, Currency.US_DOLLAR);
            Currency toCurrency = Currency.US_DOLLAR;

            // When
            MoneyValue converted = moneyValue.convertTo(toCurrency);

            // Then
            assertEquals(moneyValue, converted);
        }

        @Test
        public void testConvertToDifferentCurrency() {
            // Given
            double amount = 1.0;
            double exchangeRate = 1.07;
            double expected = Converter.roundTwoPlaces(amount/exchangeRate);

            MoneyValue moneyValue = new MoneyValue(amount, Currency.US_DOLLAR);
            Currency toCurrency = Currency.EURO;

            // When
            MoneyValue converted = moneyValue.convertTo(toCurrency);

            // Then
            assertEquals(expected, converted.getAmount());
            assertEquals(toCurrency, converted.getCurrency());
        }
    }

    @Nested
    class MoneyValueAddTest {
        @Test
        public void testAddSameCurrency() {
            // Given
            MoneyValue moneyValue1 = new MoneyValue(100.0, Currency.US_DOLLAR);
            MoneyValue moneyValue2 = new MoneyValue(50.0, Currency.US_DOLLAR);
            MoneyValue expected = new MoneyValue(150.0, Currency.US_DOLLAR);

            // When
            MoneyValue result = moneyValue1.add(moneyValue2, Currency.US_DOLLAR);

            // Then
            assertEquals(expected, result);
        }

        @Test
        public void testAddDifferentCurrencies() {
            // Given
            MoneyValue moneyValue1 = new MoneyValue(100.0, Currency.US_DOLLAR);
            MoneyValue moneyValue2 = new MoneyValue(50.0, Currency.EURO);
            MoneyValue expected = new MoneyValue(150.0, Currency.US_DOLLAR);

            // When
            MoneyValue result = moneyValue1.add(moneyValue2, Currency.US_DOLLAR);

            // Then
            assertEquals(expected, result);
        }

        @Test
        public void testAddNegativeValue() {
            // TODO Negative Values should not be possible to add. Use Subtract instead
            // Given
            MoneyValue moneyValue1 = new MoneyValue(100.0, Currency.EURO);
            MoneyValue moneyValue2 = new MoneyValue(-50.0, Currency.EURO);
            MoneyValue expected = new MoneyValue(50, Currency.EURO);

            // When
            MoneyValue result = moneyValue1.add(moneyValue2, Currency.EURO);

            // Then
            assertEquals(expected, result);
        }
    }

    @Nested
    public class MoneyValueSubtractTest {

        @Test
        public void testSubtractSameCurrency() {
            // Given
            MoneyValue moneyValue1 = new MoneyValue(100.0, Currency.US_DOLLAR);
            MoneyValue moneyValue2 = new MoneyValue(50.0, Currency.US_DOLLAR);
            MoneyValue expected = new MoneyValue(50.0, Currency.US_DOLLAR);

            // When
            MoneyValue result = moneyValue1.subtract(moneyValue2, Currency.US_DOLLAR);

            // Then
            assertEquals(expected, result);
        }

        @Test
        public void testSubtractDifferentCurrencies() {
            // Given
            MoneyValue moneyValue1 = new MoneyValue(100.0, Currency.US_DOLLAR);
            MoneyValue moneyValue2 = new MoneyValue(50.0, Currency.EURO);
            MoneyValue expected = new MoneyValue(50.0, Currency.US_DOLLAR);

            // When
            MoneyValue result = moneyValue1.subtract(moneyValue2, Currency.US_DOLLAR);

            // Then
            assertEquals(expected, result);
        }

        @Test
        public void testSubtractNegativeValue() {
            // TODO Negative Values should not be possible to subtract. Use Add instead
            // Given
            MoneyValue moneyValue1 = new MoneyValue(100.0, Currency.US_DOLLAR);
            MoneyValue moneyValue2 = new MoneyValue(-50.0, Currency.US_DOLLAR);
            MoneyValue expected = new MoneyValue(150.0, Currency.US_DOLLAR);

            // When
            MoneyValue result = moneyValue1.subtract(moneyValue2, Currency.US_DOLLAR);

            // Then
            assertEquals(expected, result);
        }
    }

    @Nested
    public class MoneyValueMultiplyTest {
        @Test
        public void testMultiplySameCurrency() {
            // Given
            MoneyValue moneyValue1 = new MoneyValue(10.0, Currency.US_DOLLAR);
            MoneyValue moneyValue2 = new MoneyValue(5.0, Currency.US_DOLLAR);
            MoneyValue expected = new MoneyValue(50.0, Currency.US_DOLLAR);

            // When
            MoneyValue result = moneyValue1.multiply(moneyValue2, Currency.US_DOLLAR);

            // Then
            assertEquals(expected, result);
        }

        @Test
        public void testMultiplyDifferentCurrencies() {
            // Given
            double amount1 = 10.0;
            double amount2 = 5.0;
            double exchangeRate = 1.07;
            double expectedValue = (amount2 * exchangeRate) * amount1;

            MoneyValue moneyValue1 = new MoneyValue(amount1, Currency.US_DOLLAR);
            MoneyValue moneyValue2 = new MoneyValue(amount2, Currency.EURO);
            MoneyValue expected = new MoneyValue(expectedValue, Currency.US_DOLLAR);

            // When
            MoneyValue result = moneyValue1.multiply(moneyValue2, Currency.US_DOLLAR);

            // Then
            assertEquals(expected, result);
        }

        @Test
        public void testMultiplyNegativeValue() {
            // TODO What use-case use multiply negative values?
            // Given
            MoneyValue moneyValue1 = new MoneyValue(-10.0, Currency.US_DOLLAR);
            MoneyValue moneyValue2 = new MoneyValue(5.0, Currency.US_DOLLAR);
            MoneyValue expected = new MoneyValue(-50.0, Currency.US_DOLLAR);

            // When
            MoneyValue result = moneyValue1.multiply(moneyValue2, Currency.US_DOLLAR);

            // Then
            assertEquals(expected, result);
        }

        @Test
        public void testMultiplyZeroValue() {
            // Given
            MoneyValue moneyValue1 = new MoneyValue(0.0, Currency.US_DOLLAR);
            MoneyValue moneyValue2 = new MoneyValue(5.0, Currency.US_DOLLAR);
            MoneyValue expected = new MoneyValue(0.0, Currency.US_DOLLAR);

            // When
            MoneyValue result = moneyValue1.multiply(moneyValue2, Currency.US_DOLLAR);

            // Then
            assertEquals(expected, result);
        }
    }

    @Nested
    public class MoneyValueDivideTest {
        @Test
        public void testDivideSameCurrency() {
            // Given
            MoneyValue moneyValue1 = new MoneyValue(100.0, Currency.US_DOLLAR);
            MoneyValue moneyValue2 = new MoneyValue(50.0, Currency.US_DOLLAR);
            MoneyValue expected = new MoneyValue(2.0, Currency.US_DOLLAR);

            // When
            MoneyValue result = moneyValue1.divide(moneyValue2, Currency.US_DOLLAR);

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
            MoneyValue result = moneyValue1.divide(moneyValue2, Currency.US_DOLLAR);

            // Then
            assertEquals(expected, result);
        }

        @Test
        public void testDivideNegativeValue() {
            // TODO What use-case use multiply negative values?
            // Given
            MoneyValue moneyValue1 = new MoneyValue(100.0, Currency.US_DOLLAR);
            MoneyValue moneyValue2 = new MoneyValue(-50.0, Currency.US_DOLLAR);
            MoneyValue expected = new MoneyValue(-2.0, Currency.US_DOLLAR);

            // When
            MoneyValue result = moneyValue1.divide(moneyValue2, Currency.US_DOLLAR);

            // Then
            assertEquals(expected, result);
        }

        @Test
        public void testDivideZeroValue() {
            // Given
            MoneyValue moneyValue1 = new MoneyValue(100.0, Currency.US_DOLLAR);
            MoneyValue moneyValue2 = new MoneyValue(0.0, Currency.US_DOLLAR);

            // When & Then
            assertThrows(MoneyValue.InvalidMoneyValueException.class, () -> moneyValue1.divide(moneyValue2, Currency.US_DOLLAR));
        }
    }

    @Test
    void testConcatenation() {
        // TODO @I573040 Please write comments in the test to make test understandable
        double expAddAmount = 2.0;
        double expSubAmount = 0.0;
        double expMulAmount = 0.0;
        double expDivAmount = 1e10;

        MoneyValue mvAddResult = new MoneyValue(1.0, Currency.US_DOLLAR);
        MoneyValue mvSubResult = new MoneyValue(1.0, Currency.US_DOLLAR);
        MoneyValue mvMulResult = new MoneyValue(1.0, Currency.US_DOLLAR);
        MoneyValue mvDivResult = new MoneyValue(1.0, Currency.US_DOLLAR);

        MoneyValue mvConst = new MoneyValue(0.1, Currency.US_DOLLAR);

        for(int i = 0; i < 10; ++i)
        {
            mvAddResult = mvAddResult.add(mvConst, Currency.US_DOLLAR);
            mvSubResult = mvSubResult.subtract(mvConst, Currency.US_DOLLAR);
            mvMulResult = mvMulResult.multiply(mvConst, Currency.US_DOLLAR);
            mvDivResult = mvDivResult.divide(mvConst, Currency.US_DOLLAR);
        }

        assertEquals(expAddAmount, mvAddResult.getAmount());
        assertEquals(expSubAmount, mvSubResult.getAmount());
        assertEquals(expMulAmount, mvMulResult.getAmount());
        assertEquals(expDivAmount, mvDivResult.getAmount());


        MoneyValue stressAddSubMv = new MoneyValue(0.0, Currency.US_DOLLAR);

        int nrOfAdditionsAndSubtractions = 1_000_000;
        for(int i = 0; i < nrOfAdditionsAndSubtractions; ++i)
            stressAddSubMv = stressAddSubMv.add(new MoneyValue(0.1, Currency.US_DOLLAR), Currency.US_DOLLAR);
        assertEquals(stressAddSubMv.getAmount(), 100_000);

        for(int i = 0; i < nrOfAdditionsAndSubtractions; ++i)
            stressAddSubMv = stressAddSubMv.subtract(new MoneyValue(0.1, Currency.US_DOLLAR), Currency.US_DOLLAR);

        assertEquals(stressAddSubMv.getAmount(), 0.0);


        MoneyValue stressMulDivMv = new MoneyValue(1.0, Currency.US_DOLLAR);

        int nrOfMultAndDivs = 30;
        for(int i = 0; i < nrOfMultAndDivs; ++i)
            stressMulDivMv = stressMulDivMv.multiply(new MoneyValue(2.0, Currency.US_DOLLAR), Currency.US_DOLLAR);
        assertEquals(stressMulDivMv.getAmount(), (1 << nrOfMultAndDivs));

        for(int i = 0; i < nrOfMultAndDivs; ++i)
            stressMulDivMv = stressMulDivMv.divide(new MoneyValue(2.0, Currency.US_DOLLAR), Currency.US_DOLLAR);


        assertEquals(stressMulDivMv.getAmount(), 1.0);
    }
}