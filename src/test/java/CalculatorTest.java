import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
@ExtendWith(MockitoExtension.class)
class CalculatorTest {
    @Mock
    private MoneyValue moneyValueMock;
    @Mock
    private Converter converterMock;

    @InjectMocks
    private Calculator calculator;

    @BeforeEach
    public void setUp() {
        moneyValueMock = new MoneyValue(100.0, Currency.US_DOLLAR);
        calculator = new Calculator(moneyValueMock, converterMock);
    }

    @Nested
    class testCalculatorConstructor {
        @Test
        public void testConstructorNullMoneyValue () {
            // Given
            Converter converter = new Converter(null);

            // When
            Exception exception = assertThrows(Exception.class, () -> {
                new Calculator(null, converter);
            });

            // Then
            assertEquals(RuntimeException.class, exception.getClass());
            assertEquals("Money Value can not be null", exception.getMessage());
        }

        @Test
        public void testConstructorNullConverter () {
            // Given
            MoneyValue moneyValue = new MoneyValue(100.0, Currency.US_DOLLAR);

            // When
            Exception exception = assertThrows(Exception.class, () -> {
                new Calculator(moneyValue, null);
            });

            // Then
            assertEquals(RuntimeException.class, exception.getClass());
            assertEquals("Converter can not be null", exception.getMessage());
        }
    }

    @Nested
    class testCalculatorGetterAndSetter {
        @Test
        public void testGetMoneyValueClient() {
            // Given
            MoneyValue moneyValue = new MoneyValue(100.0, Currency.US_DOLLAR);
            Converter cv = new Converter(null);
            Calculator calculator1 = new Calculator(moneyValue, cv);

            // When
            MoneyValueClient returnMoneyValueClient = calculator1.getMoneyValueClient();

            // Then
            assertEquals(moneyValue, returnMoneyValueClient);
        }

        @Test
        public void testGetConverterClient () {
            // Given
            MoneyValue moneyValue = new MoneyValue(100.0, Currency.US_DOLLAR);
            Converter cv = new Converter(null);
            Calculator calculator1 = new Calculator(moneyValue, cv);

            // When
            ConverterClient returnConverterClient = calculator1.getConverterClient();

            // Then
            assertEquals(cv, returnConverterClient);
        }

        @Test
        public void testSetConverterValidConverter () {
            // Given
            MoneyValue moneyValue = new MoneyValue(100.0, Currency.US_DOLLAR);
            Converter cv = new Converter(null);
            Calculator calculator1 = new Calculator(moneyValue, cv);

            // When
            assertDoesNotThrow(() -> calculator1.setConverter(cv));
        }

        @Test
        public void testSetConverterNullConverter () {
            // Given
            MoneyValue moneyValue = new MoneyValue(100.0, Currency.US_DOLLAR);
            Converter cv = new Converter(null);
            Calculator calculator1 = new Calculator(moneyValue, cv);

            // When
            Exception exception = assertThrows(Exception.class, () -> calculator1.setConverter(null));

            // Then
            assertEquals(exception.getClass(), RuntimeException.class);
            assertEquals("Converter can not be null", exception.getMessage() );
        }

        @Test
        public void testSetMoneyValueValidMoneyValue () {
            // Given
            MoneyValue moneyValue = new MoneyValue(100.0, Currency.US_DOLLAR);
            Converter cv = new Converter(null);
            Calculator calculator1 = new Calculator(moneyValue, cv);

            // When
            assertDoesNotThrow(() -> calculator1.setMoneyValue(moneyValue));
        }

        @Test
        public void testSetMoneyValueNullMoneyValue () {
            // Given
            MoneyValue moneyValue = new MoneyValue(100.0, Currency.US_DOLLAR);
            Converter cv = new Converter(null);
            Calculator calculator1 = new Calculator(moneyValue, cv);

            // When
            Exception exception = assertThrows(Exception.class, () -> calculator1.setMoneyValue(null));

            // Then
            assertEquals(exception.getClass(), RuntimeException.class);
            assertEquals("Money Value can not be null", exception.getMessage() );
        }
    }

    @Nested
    class testCalculatorAdd {
        @Test
        public void testAddMoneyValueNull () {
            // When
            Exception exception = assertThrows(Exception.class, () -> calculator.add(null));

            // Then
            assertEquals(exception.getClass(), RuntimeException.class);
            assertEquals("Invalid Money Value", exception.getMessage() );
        }
        @Test
        public void testAddValidSameCurrency () {
            // Given
            MoneyValue moneyValue = new MoneyValue(100.0, Currency.US_DOLLAR);
            MoneyValue expectedResult = new MoneyValue(200.0, Currency.US_DOLLAR);

            // When
            Calculator add = calculator.add(moneyValue);
            MoneyValue y = (MoneyValue)add.getMoneyValueClient();

            // Then
            assertEquals(expectedResult.getCurrency(), y.getCurrency());
            assertEquals(expectedResult.getAmount(), y.getAmount());
        }

        @Test
        public void testAddValidDifferentCurrency () {
            // Given
            MoneyValue moneyValue = new MoneyValue(100.0, Currency.EURO);

            when(converterMock.convertTo(moneyValue, Currency.US_DOLLAR))
                    .thenReturn(new MoneyValue(200.00, Currency.US_DOLLAR));

            MoneyValue expectedResult = new MoneyValue(300, Currency.US_DOLLAR);

            // When
            Calculator add = calculator.add(moneyValue);
            MoneyValue y = (MoneyValue)add.getMoneyValueClient();

            // Then
            assertEquals(expectedResult.getCurrency(), y.getCurrency());
            assertEquals(expectedResult.getAmount(), y.getAmount());
        }
    }

    @Nested
    class testCalculatorSubtract {
        @Test
        public void testSubtractMoneyValueNull () {
            // When
            Exception exception = assertThrows(Exception.class, () -> calculator.subtract(null));

            // Then
            assertEquals(exception.getClass(), RuntimeException.class);
            assertEquals("Invalid Money Value", exception.getMessage() );
        }
        @Test
        public void testSubtractValidSameCurrency () {
            // Given
            MoneyValue moneyValue = new MoneyValue(50.0, Currency.US_DOLLAR);
            MoneyValue expectedResult = new MoneyValue(50.0, Currency.US_DOLLAR);

            // When
            Calculator subtract = calculator.subtract(moneyValue);
            MoneyValue y = (MoneyValue)subtract.getMoneyValueClient();

            // Then
            assertEquals(expectedResult.getCurrency(), y.getCurrency());
            assertEquals(expectedResult.getAmount(), y.getAmount());
        }

        @Test
        public void testSubtractValidDifferentCurrency () {
            // Given
            MoneyValue moneyValue = new MoneyValue(100.0, Currency.EURO);

            when(converterMock.convertTo(moneyValue, Currency.US_DOLLAR))
                    .thenReturn(new MoneyValue(75.00, Currency.US_DOLLAR));

            MoneyValue expectedResult = new MoneyValue(25, Currency.US_DOLLAR);

            // When
            Calculator subtract = calculator.subtract(moneyValue);
            MoneyValue y = (MoneyValue)subtract.getMoneyValueClient();

            // Then
            assertEquals(expectedResult.getCurrency(), y.getCurrency());
            assertEquals(expectedResult.getAmount(), y.getAmount());
        }
    }

    @Nested
    class testCalculatorMultiply {
        @Test
        public void testMultiplyMoneyValueNull () {
            // When
            Exception exception = assertThrows(Exception.class, () -> calculator.multiply(null));

            // Then
            assertEquals(exception.getClass(), RuntimeException.class);
            assertEquals("Invalid Money Value", exception.getMessage() );
        }
        @Test
        public void testMultiplyValidSameCurrency () {
            // Given
            MoneyValue moneyValue = new MoneyValue(2.0, Currency.US_DOLLAR);
            MoneyValue expectedResult = new MoneyValue(200.0, Currency.US_DOLLAR);

            // When
            Calculator multiply = calculator.multiply(moneyValue);
            MoneyValue y = (MoneyValue)multiply.getMoneyValueClient();

            // Then
            assertEquals(expectedResult.getCurrency(), y.getCurrency());
            assertEquals(expectedResult.getAmount(), y.getAmount());
        }

        @Test
        public void testMultiplyValidDifferentCurrency () {
            // Given
            MoneyValue moneyValue = new MoneyValue(100.0, Currency.EURO);

            when(converterMock.convertTo(moneyValue, Currency.US_DOLLAR))
                    .thenReturn(new MoneyValue(3.00, Currency.US_DOLLAR));

            MoneyValue expectedResult = new MoneyValue(300, Currency.US_DOLLAR);

            // When
            Calculator multiply = calculator.multiply(moneyValue);
            MoneyValue y = (MoneyValue)multiply.getMoneyValueClient();

            // Then
            assertEquals(expectedResult.getCurrency(), y.getCurrency());
            assertEquals(expectedResult.getAmount(), y.getAmount());
        }
    }

    @Nested
    class testCalculatorDivide {
        @Test
        public void testDivideMoneyValueNull () {
            // When
            Exception exception = assertThrows(Exception.class, () -> calculator.divide(null));

            // Then
            assertEquals(exception.getClass(), RuntimeException.class);
            assertEquals("Invalid Money Value", exception.getMessage() );
        }
        @Test
        public void testDivideValidSameCurrency () {
            // Given
            MoneyValue moneyValue = new MoneyValue(100.0, Currency.US_DOLLAR);
            MoneyValue expectedResult = new MoneyValue(1.0, Currency.US_DOLLAR);

            // When
            Calculator divide = calculator.divide(moneyValue);
            MoneyValue y = (MoneyValue)divide.getMoneyValueClient();

            // Then
            assertEquals(expectedResult.getCurrency(), y.getCurrency());
            assertEquals(expectedResult.getAmount(), y.getAmount());
        }

        @Test
        public void testDivideValidDifferentCurrency () {
            // Given
            MoneyValue moneyValue = new MoneyValue(100.0, Currency.EURO);

            when(converterMock.convertTo(moneyValue, Currency.US_DOLLAR))
                    .thenReturn(new MoneyValue(50.00, Currency.US_DOLLAR));

            MoneyValue expectedResult = new MoneyValue(2, Currency.US_DOLLAR);

            // When
            Calculator divide = calculator.divide(moneyValue);
            MoneyValue y = (MoneyValue)divide.getMoneyValueClient();

            // Then
            assertEquals(expectedResult.getCurrency(), y.getCurrency());
            assertEquals(expectedResult.getAmount(), y.getAmount());
        }
    }

    @Nested
    class testMultiThreading {
        private final int numberOfThreads = 10;
        private ExecutorService service;
        private CountDownLatch latch;
        private Thread[] threads;
        private static MoneyValue oneDollar;
        private static int nrOfThreads;

        @BeforeAll
        public static void beforeAll() {
            oneDollar = new MoneyValue("$1");
            nrOfThreads = 10;
        }

        @BeforeEach
        void beforeEach() {
            threads = new Thread[nrOfThreads];
        }
        @Test
        public void testAddMultiThreadingSafe() throws InterruptedException {
            // Given
            MoneyValue expectedResult = new MoneyValue("$110");

            // When
            for(int i = 0; i < threads.length; ++i) {
                threads[i] = new Thread(() -> calculator.add(oneDollar));
                threads[i].start();
            }

            for(Thread t: threads)
                t.join();

            // Then
            MoneyValue mv = (MoneyValue) calculator.getMoneyValueClient();
            assertEquals(expectedResult.getAmount(), mv.getAmount());
            assertEquals(expectedResult.getCurrency(), mv.getCurrency());
        }

        @Test
        public void testSubtractMultiThreadingSafe() throws InterruptedException {
            // Given
            MoneyValue expectedResult = new MoneyValue("$90");

            // When
            for(int i = 0; i < threads.length; ++i) {
                threads[i] = new Thread(() -> calculator.subtract(oneDollar));
                threads[i].start();
            }

            for(Thread t: threads)
                t.join();

            // Then
            MoneyValue mv = (MoneyValue) calculator.getMoneyValueClient();
            assertEquals(expectedResult.getAmount(), mv.getAmount());
            assertEquals(expectedResult.getCurrency(), mv.getCurrency());
        }

        @Test
        public void testMultiplyMultiThreadingSafe() throws InterruptedException {
            // Given
            MoneyValue twoDollar = new MoneyValue("$2");
            MoneyValue expectedResult = new MoneyValue("$102400");

            // When
            for(int i = 0; i < threads.length; ++i) {
                threads[i] = new Thread(() -> calculator.multiply(twoDollar));
                threads[i].start();
            }

            for(Thread t: threads)
                t.join();

            // Then
            MoneyValue mv = (MoneyValue) calculator.getMoneyValueClient();
            assertEquals(expectedResult.getAmount(), mv.getAmount());
            assertEquals(expectedResult.getCurrency(), mv.getCurrency());
        }

        @Test
        public void testDivideMultiThreadingSafe() throws InterruptedException {
            // Given
            MoneyValue bigMoneyAmount = new MoneyValue("$204800");
            calculator = new Calculator(bigMoneyAmount, converterMock);
            MoneyValue twoDollar = new MoneyValue("$2");
            MoneyValue expectedResult = new MoneyValue("$200");

            // When
            for(int i = 0; i < threads.length; ++i) {
                threads[i] = new Thread(() -> calculator.divide(twoDollar));
                threads[i].start();
            }

            for(Thread t: threads)
                t.join();

            // Then
            MoneyValue mv = (MoneyValue) calculator.getMoneyValueClient();
            assertEquals(expectedResult.getAmount(), mv.getAmount());
            assertEquals(expectedResult.getCurrency(), mv.getCurrency());
        }

        @Test
        public void testMultiThreadingSafeChainingAdd() throws InterruptedException {
            // Given
            MoneyValue expectedResult = new MoneyValue("$120");

            // When
            for(int i = 0; i < threads.length; ++i) {
                threads[i] = new Thread(() -> calculator.add(oneDollar).add(oneDollar));
                threads[i].start();
            }

            for(Thread t: threads)
                t.join();

            // Then
            MoneyValue mv = (MoneyValue) calculator.getMoneyValueClient();
            assertEquals(expectedResult.getAmount(), mv.getAmount());
            assertEquals(expectedResult.getCurrency(), mv.getCurrency());
        }

        @Test
        public void testMultiThreadingSafeChainingSubtract() throws InterruptedException {
            // Given
            MoneyValue expectedResult = new MoneyValue("$80");

            // When
            for(int i = 0; i < threads.length; ++i) {
                threads[i] = new Thread(() -> calculator.subtract(oneDollar).subtract(oneDollar));
                threads[i].start();
            }

            for(Thread t: threads)
                t.join();

            // Then
            MoneyValue mv = (MoneyValue) calculator.getMoneyValueClient();
            assertEquals(expectedResult.getAmount(), mv.getAmount());
            assertEquals(expectedResult.getCurrency(), mv.getCurrency());
        }

        @Test
        public void testMultiThreadingSafeChainingMultiply() throws InterruptedException {
            // Given
            MoneyValue twoDollar = new MoneyValue("$2");
            MoneyValue expectedResult = new MoneyValue("$104857600");

            // When
            for(int i = 0; i < threads.length; ++i) {
                threads[i] = new Thread(() -> calculator.multiply(twoDollar).multiply(twoDollar));
                threads[i].start();
            }

            for(Thread t: threads)
                t.join();

            // Then
            MoneyValue mv = (MoneyValue) calculator.getMoneyValueClient();
            assertEquals(expectedResult.getAmount(), mv.getAmount());
            assertEquals(expectedResult.getCurrency(), mv.getCurrency());
        }

        @Test
        public void testMultiThreadingSafeChainingDivide() throws InterruptedException {
            // Given
            MoneyValue bigMoneyAmount = new MoneyValue("$104857600");
            calculator = new Calculator(bigMoneyAmount, converterMock);
            MoneyValue twoDollar = new MoneyValue("$2");
            MoneyValue expectedResult = new MoneyValue("$100");

            // When
            for(int i = 0; i < threads.length; ++i) {
                threads[i] = new Thread(() -> calculator.divide(twoDollar).divide(twoDollar));
                threads[i].start();
            }

            for(Thread t: threads)
                t.join();

            // Then
            MoneyValue mv = (MoneyValue) calculator.getMoneyValueClient();
            assertEquals(expectedResult.getAmount(), mv.getAmount());
            assertEquals(expectedResult.getCurrency(), mv.getCurrency());
        }

        @Test
        public void testMultiThreadingSafeChainingAllOperations() throws InterruptedException {
            // Given
            MoneyValue twoDollar = new MoneyValue("$2");
            MoneyValue expectedResult = new MoneyValue("$100");

            // When
            for(int i = 0; i < threads.length; ++i) {
                threads[i] = new Thread(() -> calculator.add(twoDollar).subtract(twoDollar).multiply(twoDollar).divide(twoDollar));
                threads[i].start();
            }

            for(Thread t: threads)
                t.join();

            // Then
            MoneyValue mv = (MoneyValue) calculator.getMoneyValueClient();
            assertEquals(expectedResult.getAmount(), mv.getAmount());
            assertEquals(expectedResult.getCurrency(), mv.getCurrency());
        }
    }

    @Nested
    public class testChaining {
        @Test
        public void testChainingAdd() {
            // Given
            MoneyValue moneyValue = new MoneyValue(100.0, Currency.US_DOLLAR);
            MoneyValue expected = new MoneyValue(300.0, Currency.US_DOLLAR);

            // When
            calculator.add(moneyValue).add(moneyValue);

            // Then
            assertEquals(expected, calculator.getMoneyValueClient());
        }

        @Test
        public void testChainingSubtract() {
            // Given
            MoneyValue moneyValue = new MoneyValue(20.0, Currency.US_DOLLAR);
            MoneyValue expected = new MoneyValue(60.0, Currency.US_DOLLAR);

            // When
            calculator.subtract(moneyValue).subtract(moneyValue);

            // Then
            assertEquals(expected, calculator.getMoneyValueClient());
        }

        @Test
        public void testChainingMultiply() {
            // Given
            MoneyValue moneyValue = new MoneyValue(2.0, Currency.US_DOLLAR);
            MoneyValue expected = new MoneyValue(400.0, Currency.US_DOLLAR);

            // When
            calculator.multiply(moneyValue).multiply(moneyValue);

            // Then
            assertEquals(expected, calculator.getMoneyValueClient());
        }

        @Test
        public void testChainingDivide() {
            // Given
            MoneyValue moneyValue = new MoneyValue(2.0, Currency.US_DOLLAR);
            MoneyValue expected = new MoneyValue(25.0, Currency.US_DOLLAR);

            // When
            calculator.divide(moneyValue).divide(moneyValue);

            // Then
            assertEquals(expected, calculator.getMoneyValueClient());
        }

        @Test
        public void testChainingAddAndSubtract() {
            // Given
            MoneyValue moneyValue = new MoneyValue(2.0, Currency.US_DOLLAR);
            MoneyValue expected = new MoneyValue(100.0, Currency.US_DOLLAR);

            // When
            calculator.add(moneyValue).subtract(moneyValue);

            // Then
            assertEquals(expected, calculator.getMoneyValueClient());
        }

        @Test
        public void testChainingMultiplyAndDivide() {
            // Given
            MoneyValue moneyValue = new MoneyValue(2.0, Currency.US_DOLLAR);
            MoneyValue expected = new MoneyValue(100.0, Currency.US_DOLLAR);

            // When
            calculator.multiply(moneyValue).divide(moneyValue);

            // Then
            assertEquals(expected, calculator.getMoneyValueClient());
        }

        @Test
        public void testChainingAllOperations() {
            // Given
            MoneyValue moneyValue = new MoneyValue(20.0, Currency.US_DOLLAR);
            MoneyValue expected = new MoneyValue(100.0, Currency.US_DOLLAR);

            // When
            calculator.add(moneyValue).subtract(moneyValue).multiply(moneyValue).divide(moneyValue);

            // Then
            assertEquals(expected, calculator.getMoneyValueClient());
        }
    }
}