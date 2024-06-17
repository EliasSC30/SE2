import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public enum Currency {
    US_DOLLAR("USD", "$"),
    EURO("EUR", "€"),
    JAPANESE_YEN("JPY", "¥"),
    BRITISH_POUND("GBP", "£");

    private final String isoCode;
    private final String symbol;

    private static final Map<Character, Currency> SYMBOL_TO_CURRENCY;
    private static final Map<String, Currency> ISO_TO_CURRENCY;

    static {
        Map<Character, Currency> symbolToCurrency = new HashMap<>();
        Map<String, Currency> isoToCurrency = new HashMap<>();

        for (Currency currency : Currency.values()) {
            symbolToCurrency.put(currency.symbol.charAt(0), currency);
            isoToCurrency.put(currency.isoCode, currency);
        }

        SYMBOL_TO_CURRENCY = Collections.unmodifiableMap(symbolToCurrency);
        ISO_TO_CURRENCY = Collections.unmodifiableMap(isoToCurrency);
    }

    Currency(String isoCode, String symbol) {
        this.isoCode = isoCode;
        this.symbol = symbol;
    }

    public String getIsoCode() {
        return isoCode;
    }

    public String getSymbol() {
        return symbol;
    }

    public static Currency fromSymbol(char symbol) {
        return SYMBOL_TO_CURRENCY.get(symbol);
    }

    public static Currency fromIsoCode(String isoCode) {
        return ISO_TO_CURRENCY.get(isoCode);
    }
}
