import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MoneyValueTest {

    @Nested
    class testMoneyValueConstructor{
        @Test
        public void testMoneyValueConstructorValidValues() {
            // Given
            double amount = 100.0;
            MoneyValue.Currency currency = MoneyValue.Currency.Dollar;

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
            MoneyValue.Currency currency = MoneyValue.Currency.Dollar;

            // When & Then
            assertThrows(MoneyValue.InvalidMoneyValueException.class, () -> new MoneyValue(amount, currency));
        }

        @Test
        public void testMoneyValueConstructorNullCurrency() {
            // Given
            double amount = 100.0;
            MoneyValue.Currency currency = null;

            // When & Then
            assertThrows(MoneyValue.InvalidMoneyValueException.class, () -> new MoneyValue(amount, currency));
        }

        @Test
        public void testMoneyValueConstructorAmountValidValues() {
            // Given
            double amount = 100.0;

            // When
            MoneyValue moneyValue = new MoneyValue(amount);

            // Then
            assertEquals(amount, moneyValue.getAmount());
            assertEquals(MoneyValue.NEUTRAL_CURRENCY, moneyValue.getCurrency());
        }

        @Test
        public void testMoneyValueConstructorAmountInvalidAmount() {
            // Given
            double amount = Double.NaN;

            // When & Then
            assertThrows(MoneyValue.InvalidMoneyValueException.class, () -> new MoneyValue(amount));
        }

        @Test
        public void testMoneyValueConstructorAmountInfiniteAmount() {
            // Given
            double amount = Double.POSITIVE_INFINITY;

            // When & Then
            assertThrows(MoneyValue.InvalidMoneyValueException.class, () -> new MoneyValue(amount));
        }
    }

    @Test
    public void testGetCurrency() {
        // Given
        MoneyValue.Currency expectedCurrency = MoneyValue.Currency.Euro;
        MoneyValue moneyValue = new MoneyValue(100.0, MoneyValue.Currency.Euro);

        // When
        MoneyValue.Currency actualCurrency = moneyValue.getCurrency();

        // Then
        assertEquals(expectedCurrency, actualCurrency);
    }

    @Test
    public void testGetAmount() {
        // Given
        double expectedAmount = 100.0;
        MoneyValue moneyValue = new MoneyValue(100.0, MoneyValue.Currency.Dollar);

        // When
        double actualAmount = moneyValue.getAmount();

        // Then
        assertEquals(expectedAmount, actualAmount, 0.0);
    }

    @Test
    public void testToStringValid() {
        // Given
        double amount = 123.456;
        MoneyValue.Currency currency = MoneyValue.Currency.Euro;
        MoneyValue moneyValue = new MoneyValue(amount, currency);
        String expected = "123.46 €";

        // When
        String actual = moneyValue.toString();

        // Then
        assertEquals(expected, actual);
    }

    @Test
    public void testToStringPrefixValid() {
        // Given
        double amount = 123.456;
        MoneyValue.Currency currency = MoneyValue.Currency.Euro;
        MoneyValue moneyValue = new MoneyValue(amount, currency);
        String expected = "€ 123.46";

        // When
        String actual = moneyValue.toStringPrefix();

        // Then
        assertEquals(expected, actual);
    }

    @Test
    public void testToISOCode() {
        // Given
        double amount = 123.456;
        MoneyValue.Currency currency = MoneyValue.Currency.Euro;
        MoneyValue moneyValue = new MoneyValue(amount, currency);
        String expected = "123.46 EUR";

        // When
        String actual = moneyValue.toISOCode();

        // Then
        assertEquals(expected, actual);
    }

    @Test
    public void testToISOCodePrefix() {
        // Given
        double amount = 100.0;
        MoneyValue.Currency currency = MoneyValue.Currency.JapaneseYen;
        MoneyValue moneyValue = new MoneyValue(amount, currency);
        String expected = "JPY 100.0";

        // When
        String actual = moneyValue.toISOCodePrefix();

        // Then
        assertEquals(expected, actual);
    }

    @Nested
    class MoneyValueEqualsTest {
        @Test
        public void testEqualsSameObject() {
            // Given
            MoneyValue moneyValue = new MoneyValue(100.0, MoneyValue.Currency.Dollar);

            // When & Then
            assertTrue(moneyValue.equals(moneyValue));
        }

        @Test
        public void testEqualsDifferentObjects() {
            // Given
            MoneyValue moneyValue1 = new MoneyValue(100.0, MoneyValue.Currency.Dollar);
            MoneyValue moneyValue2 = new MoneyValue(100.0, MoneyValue.Currency.Dollar);

            // When & Then
            assertTrue(moneyValue1.equals(moneyValue2));
            assertTrue(moneyValue2.equals(moneyValue1));
        }

        @Test
        public void testNotEqualsForDifferentAmounts() {
            // Given
            MoneyValue moneyValue1 = new MoneyValue(100.0, MoneyValue.Currency.Dollar);
            MoneyValue moneyValue2 = new MoneyValue(200.0, MoneyValue.Currency.Dollar);

            // When & Then
            assertFalse(moneyValue1.equals(moneyValue2));
            assertFalse(moneyValue2.equals(moneyValue1));
        }

        @Test
        public void testEqualsForSameAmountsDifferentCurrencies() {
            // Given
            MoneyValue moneyValue1 = new MoneyValue(100.0, MoneyValue.Currency.Dollar);
            MoneyValue moneyValue2 = moneyValue1.convertTo(MoneyValue.Currency.Euro);

            // When & Then
            assertTrue(moneyValue1.equals(moneyValue2));
            assertTrue(moneyValue2.equals(moneyValue1));
        }

        @Test
        public void testNotEqualsForDifferentCurrencies() {
            // Given
            MoneyValue moneyValue1 = new MoneyValue(100.0, MoneyValue.Currency.Dollar);
            MoneyValue moneyValue2 = new MoneyValue(100.0, MoneyValue.Currency.Euro);

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
          MoneyValue moneyValue1 = new MoneyValue(100.0, MoneyValue.Currency.Dollar);
          MoneyValue moneyValue2 = moneyValue1;

          // When & Then
          assertEquals(0, moneyValue1.compareTo(moneyValue2));
      }

      @Test
      public void testCompareToEqualValues() {
          // Given
          MoneyValue moneyValue1 = new MoneyValue(100.0, MoneyValue.Currency.Dollar);
          MoneyValue moneyValue2 = new MoneyValue(100.0, MoneyValue.Currency.Euro);

          // When & Then
          assertTrue(moneyValue1.compareTo(moneyValue2) != 0);
      }

      @Test
      public void testCompareToEqualValuesWithDifferentCurrencies() {
          // Given
          MoneyValue moneyValue1 = new MoneyValue(100.0, MoneyValue.Currency.Dollar);
          MoneyValue moneyValue2 = moneyValue1.convertTo(MoneyValue.Currency.Euro);

          // When & Then
          assertEquals(0, moneyValue1.compareTo(moneyValue2));
      }



      @Test
      public void testCompareToGreaterValue() {
          // Given
          MoneyValue moneyValue1 = new MoneyValue(200.0, MoneyValue.Currency.Dollar);
          MoneyValue moneyValue2 = new MoneyValue(100.0, MoneyValue.Currency.Euro);

          // When & Then
          assertTrue(moneyValue1.compareTo(moneyValue2) > 0);
      }

      @Test
      public void testCompareToSmallerValue() {
          // Given
          MoneyValue moneyValue1 = new MoneyValue(100.0, MoneyValue.Currency.Dollar);
          MoneyValue moneyValue2 = new MoneyValue(200.0, MoneyValue.Currency.Euro);

          // When & Then
          assertTrue(moneyValue1.compareTo(moneyValue2) < 0);
      }
  }

    @Test
    void isValid() {
        // Given
        MoneyValue valid = new MoneyValue(1.0);

        // When & Then
        assertTrue(valid.isValid());
    }

   @Nested
    public class MoneyValueConvertToTest {

        @Test
        public void testConvertToSameCurrency() {
            // Given
            MoneyValue moneyValue = new MoneyValue(100.0, MoneyValue.Currency.Dollar);
            MoneyValue.Currency toCurrency = MoneyValue.Currency.Dollar;

            // When
            MoneyValue converted = moneyValue.convertTo(toCurrency);

            // Then
            assertEquals(moneyValue, converted);
        }

        @Test
        public void testConvertToDifferentCurrency() {
            // Given
            MoneyValue moneyValue = new MoneyValue(1.0, MoneyValue.Currency.Dollar);
            MoneyValue.Currency toCurrency = MoneyValue.Currency.Euro;
            double expected = Converter.roundTwoPlaces(1.0/1.09);
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
            MoneyValue moneyValue1 = new MoneyValue(100.0, MoneyValue.Currency.Dollar);
            MoneyValue moneyValue2 = new MoneyValue(50.0, MoneyValue.Currency.Dollar);
            MoneyValue expected = new MoneyValue(150.0, MoneyValue.Currency.Dollar);

            // When
            MoneyValue result = moneyValue1.add(moneyValue2);

            // Then
            assertEquals(expected, result);
        }

        @Test
        public void testAddDifferentCurrencies() {
            // Given
            MoneyValue.Currency neutralCurrency = MoneyValue.NEUTRAL_CURRENCY;
            MoneyValue moneyValue1 = new MoneyValue(100.0, neutralCurrency);
            MoneyValue moneyValue2 = new MoneyValue(50.0, MoneyValue.Currency.Euro);
            MoneyValue expected = moneyValue2.convertTo(neutralCurrency).add(moneyValue1);

            // When
            MoneyValue result = moneyValue1.add(moneyValue2);

            // Then
            assertEquals(expected, result);
        }

        @Test
        public void testAddNegativeValue() {
            // Given
            MoneyValue moneyValue1 = new MoneyValue(100.0, MoneyValue.Currency.Euro);
            MoneyValue moneyValue2 = new MoneyValue(-50.0, MoneyValue.Currency.Euro);
            MoneyValue expected = new MoneyValue(50, MoneyValue.Currency.Euro);

            // When
            MoneyValue result = moneyValue1.add(moneyValue2);

            // Then
            assertEquals(expected, result);
        }

    }

    @Nested
    public class MoneyValueSubtractTest {

        @Test
        public void testSubtractSameCurrency() {
            // Given
            MoneyValue moneyValue1 = new MoneyValue(100.0, MoneyValue.Currency.Dollar);
            MoneyValue moneyValue2 = new MoneyValue(50.0, MoneyValue.Currency.Dollar);
            MoneyValue expected = new MoneyValue(50.0, MoneyValue.Currency.Dollar);

            // When
            MoneyValue result = moneyValue1.subtract(moneyValue2);

            // Then
            assertEquals(expected, result);
        }

        @Test
        public void testSubtractDifferentCurrencies() {
            // Given
            MoneyValue.Currency neutralCurrency = MoneyValue.NEUTRAL_CURRENCY;
            MoneyValue moneyValue1 = new MoneyValue(100.0, neutralCurrency);
            MoneyValue moneyValue2 = new MoneyValue(50.0, MoneyValue.Currency.Euro);
            MoneyValue expected = moneyValue1.subtract(moneyValue2.convertTo(neutralCurrency));

            // When
            MoneyValue result = moneyValue1.subtract(moneyValue2);

            // Then
            assertEquals(expected, result);
        }

        @Test
        public void testSubtractNegativeValue() {
            // Given
            MoneyValue moneyValue1 = new MoneyValue(100.0, MoneyValue.Currency.Dollar);
            MoneyValue moneyValue2 = new MoneyValue(-50.0, MoneyValue.Currency.Dollar);
            MoneyValue expected = new MoneyValue(150.0, MoneyValue.Currency.Dollar);

            // When
            MoneyValue result = moneyValue1.subtract(moneyValue2);

            // Then
            assertEquals(expected, result);
        }
    }

    @Nested
    public class MoneyValueMultiplyTest {

        @Test
        public void testMultiplySameCurrency() {
            // Given
            MoneyValue moneyValue1 = new MoneyValue(10.0, MoneyValue.Currency.Dollar);
            MoneyValue moneyValue2 = new MoneyValue(5.0, MoneyValue.Currency.Dollar);
            MoneyValue expected = new MoneyValue(50.0, MoneyValue.Currency.Dollar);

            // When
            MoneyValue result = moneyValue1.multiply(moneyValue2);

            // Then
            assertEquals(expected, result);
        }

        @Test
        public void testMultiplyDifferentCurrencies() {
            // Given
            MoneyValue.Currency neutralCurrency = MoneyValue.NEUTRAL_CURRENCY;
            MoneyValue moneyValue1 = new MoneyValue(10.0, neutralCurrency);
            MoneyValue moneyValue2 = new MoneyValue(5.0, MoneyValue.Currency.Euro);
            MoneyValue expected = moneyValue1.multiply(moneyValue2.convertTo(neutralCurrency));

            // When
            MoneyValue result = moneyValue1.multiply(moneyValue2);

            // Then
            assertEquals(expected, result);
        }

        @Test
        public void testMultiplyNegativeValue() {
            // Given
            MoneyValue moneyValue1 = new MoneyValue(-10.0, MoneyValue.Currency.Dollar);
            MoneyValue moneyValue2 = new MoneyValue(5.0, MoneyValue.Currency.Dollar);
            MoneyValue expected = new MoneyValue(-50.0, MoneyValue.Currency.Dollar);

            // When
            MoneyValue result = moneyValue1.multiply(moneyValue2);

            // Then
            assertEquals(expected, result);
        }

        @Test
        public void testMultiplyZeroValue() {
            // Given
            MoneyValue moneyValue1 = new MoneyValue(0.0, MoneyValue.Currency.Dollar);
            MoneyValue moneyValue2 = new MoneyValue(5.0, MoneyValue.Currency.Dollar);
            MoneyValue expected = new MoneyValue(0.0, MoneyValue.Currency.Dollar);

            // When
            MoneyValue result = moneyValue1.multiply(moneyValue2);

            // Then
            assertEquals(expected, result);
        }
    }

    @Nested
    public class MoneyValueDivideTest {

        @Test
        public void testDivideSameCurrency() {
            // Given
            MoneyValue moneyValue1 = new MoneyValue(100.0, MoneyValue.Currency.Dollar);
            MoneyValue moneyValue2 = new MoneyValue(50.0, MoneyValue.Currency.Dollar);
            MoneyValue expected = new MoneyValue(2.0, MoneyValue.Currency.Dollar);

            // When
            MoneyValue result = moneyValue1.divide(moneyValue2);

            // Then
            assertEquals(expected, result);
        }

        @Test
        public void testDivideDifferentCurrencies() {
            // Given
            MoneyValue.Currency neutralCurrency = MoneyValue.NEUTRAL_CURRENCY;
            MoneyValue moneyValue1 = new MoneyValue(100.0, neutralCurrency);
            MoneyValue moneyValue2 = new MoneyValue(50.0, MoneyValue.Currency.Euro);
            MoneyValue expected = moneyValue1.divide(moneyValue2.convertTo(neutralCurrency));

            // When
            MoneyValue result = moneyValue1.divide(moneyValue2);

            // Then
            assertEquals(expected, result);
        }

        @Test
        public void testDivideNegativeValue() {
            // Given
            MoneyValue moneyValue1 = new MoneyValue(100.0, MoneyValue.Currency.Dollar);
            MoneyValue moneyValue2 = new MoneyValue(-50.0, MoneyValue.Currency.Dollar);
            MoneyValue expected = new MoneyValue(-2.0, MoneyValue.Currency.Dollar);

            // When
            MoneyValue result = moneyValue1.divide(moneyValue2);

            // Then
            assertEquals(expected, result);
        }

        @Test
        public void testDivideZeroValue() {
            // Given
            MoneyValue moneyValue1 = new MoneyValue(100.0, MoneyValue.Currency.Dollar);
            MoneyValue moneyValue2 = new MoneyValue(0.0, MoneyValue.Currency.Dollar);

            // When & Then
            assertThrows(MoneyValue.InvalidMoneyValueException.class, () -> moneyValue1.divide(moneyValue2));
        }
    }

    @Test
    void testConcatenation() {
        double expAddAmount = 2.0;
        double expSubAmount = 0.0;
        double expMulAmount = 0.0;
        double expDivAmount = 1e10;

        MoneyValue mvAddResult = new MoneyValue(1.0);
        MoneyValue mvSubResult = new MoneyValue(1.0);
        MoneyValue mvMulResult = new MoneyValue(1.0);
        MoneyValue mvDivResult = new MoneyValue(1.0);

        MoneyValue mvConst = new MoneyValue(0.1);

        for(int i = 0; i < 10; ++i)
        {
            mvAddResult = mvAddResult.add(mvConst);
            mvSubResult = mvSubResult.subtract(mvConst);
            mvMulResult = mvMulResult.multiply(mvConst);
            mvDivResult = mvDivResult.divide(mvConst);
        }

        assertEquals(expAddAmount, mvAddResult.getAmount());
        assertEquals(expSubAmount, mvSubResult.getAmount());
        assertEquals(expMulAmount, mvMulResult.getAmount());
        assertEquals(expDivAmount, mvDivResult.getAmount());


        MoneyValue stressAddSubMv = new MoneyValue(0.0);

        int nrOfAdditionsAndSubtractions = 1_000_000;
        for(int i = 0; i < nrOfAdditionsAndSubtractions; ++i)
            stressAddSubMv = stressAddSubMv.add(new MoneyValue(0.1));
        assertEquals(stressAddSubMv.getAmount(), 100_000);

        for(int i = 0; i < nrOfAdditionsAndSubtractions; ++i)
            stressAddSubMv = stressAddSubMv.subtract(new MoneyValue(0.1));

        assertEquals(stressAddSubMv.getAmount(), 0.0);


        MoneyValue stressMulDivMv = new MoneyValue(1.0);

        int nrOfMultAndDivs = 30;
        for(int i = 0; i < nrOfMultAndDivs; ++i)
            stressMulDivMv = stressMulDivMv.multiply(new MoneyValue(2.0));
        assertEquals(stressMulDivMv.getAmount(), (1 << nrOfMultAndDivs));

        for(int i = 0; i < nrOfMultAndDivs; ++i)
            stressMulDivMv = stressMulDivMv.divide(new MoneyValue(2.0));


        assertEquals(stressMulDivMv.getAmount(), 1.0);
    }
}