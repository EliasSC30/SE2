import java.text.*;
import java.util.Locale;

/**
 * CustomCurrencyFormat extends NumberFormat to provide a custom currency formatting with a specified symbol.
 * This class modifies the currency symbol used in formatting according to the provided locale.
 */
public class CustomCurrencyFormat extends NumberFormat {
    private final DecimalFormat df;

    /**
     * Constructs a CustomCurrencyFormat object with the specified locale and currency symbol.
     *
     * @param locale The locale specifying the formatting rules (e.g., language, country).
     * @param symbol The currency symbol to use in formatting.
     */
    public CustomCurrencyFormat(Locale locale, String symbol) {
        this.df = (DecimalFormat) NumberFormat.getCurrencyInstance(locale);
        DecimalFormatSymbols dfs = df.getDecimalFormatSymbols();
        dfs.setCurrencySymbol(symbol);
        df.setDecimalFormatSymbols(dfs);
    }

    /**
     * Formats the given double number into a StringBuffer using the custom currency format.
     *
     * @param number      The double number to format.
     * @param toAppendTo  The StringBuffer to which the formatted number is appended.
     * @param pos         On input: an alignment field, if desired. On output: the offsets of the alignment field.
     * @return The StringBuffer containing the formatted number.
     */
    @Override
    public StringBuffer format(double number, StringBuffer toAppendTo, FieldPosition pos) {
        return df.format(number, toAppendTo, pos);
    }

    /**
     * Formats the given long number into a StringBuffer using the custom currency format.
     *
     * @param number      The long number to format.
     * @param toAppendTo  The StringBuffer to which the formatted number is appended.
     * @param pos         On input: an alignment field, if desired. On output: the offsets of the alignment field.
     * @return The StringBuffer containing the formatted number.
     */
    @Override
    public StringBuffer format(long number, StringBuffer toAppendTo, FieldPosition pos) {
        return df.format(number, toAppendTo, pos);
    }

    /**
     * Parses the text from the beginning of the given string to produce a number.
     *
     * @param source         A String containing the text to be parsed.
     * @param parsePosition  On input: the position at which to start parsing. On output: the position at which parsing stopped.
     * @return               The parsed Number or null if the beginning of the specified string cannot be parsed.
     */
    @Override
    public Number parse(String source, ParsePosition parsePosition) {
        return df.parse(source, parsePosition);
    }
}
