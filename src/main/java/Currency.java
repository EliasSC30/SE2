import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Enum representing various currencies with their ISO codes and symbols.
 */
public enum Currency {
    /**
     * US Dollar currency.
     */
    US_DOLLAR("USD", "$"),

    /**
     * Euro currency.
     */
    EURO("EUR", "€"),

    /**
     * Japanese Yen currency.
     */
    JAPANESE_YEN("JPY", "¥"),

    /**
     * British Pound currency.
     */
    BRITISH_POUND("GBP", "£");

    private final String isoCode;
    private final String symbol;

    // Static maps for efficient lookup
    private static final Map<Character, Currency> SYMBOL_TO_CURRENCY;
    private static final Map<String, Currency> ISO_TO_CURRENCY;

    // Static initialization block to populate the maps
    static {
        Map<Character, Currency> symbolToCurrency = new HashMap<>();
        Map<String, Currency> isoToCurrency = new HashMap<>();

        // Populate the maps with enum values
        for (Currency currency : Currency.values()) {
            symbolToCurrency.put(currency.symbol.charAt(0), currency);
            isoToCurrency.put(currency.isoCode, currency);
        }
        // Make the maps unmodifiable
        SYMBOL_TO_CURRENCY = Collections.unmodifiableMap(symbolToCurrency);
        ISO_TO_CURRENCY = Collections.unmodifiableMap(isoToCurrency);
    }

    /**
     * Constructor for Currency enum.
     *
     * @param isoCode The ISO code of the currency.
     * @param symbol  The symbol representing the currency.
     */
    Currency(String isoCode, String symbol) {
        this.isoCode = isoCode;
        this.symbol = symbol;
    }

    /**
     * Get the ISO code of the currency.
     *
     * @return The ISO code as a String.
     */
    public String getIsoCode() {
        return isoCode;
    }

    /**
     * Get the symbol of the currency.
     *
     * @return The symbol as a String.
     */
    public String getSymbol() {
        return symbol;
    }

    /**
     * Retrieve the Currency enum based on its symbol.
     *
     * @param symbol The symbol character of the currency.
     * @return The Currency enum corresponding to the symbol.
     */
    public static Currency fromSymbol(char symbol) {
        return SYMBOL_TO_CURRENCY.get(symbol);
    }

    /**
     * Retrieve the Currency enum based on its ISO code.
     *
     * @param isoCode The ISO code of the currency.
     * @return The Currency enum corresponding to the ISO code.
     */
    public static Currency fromIsoCode(String isoCode) {
        return ISO_TO_CURRENCY.get(isoCode);
    }
}
