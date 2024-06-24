import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.Locale;

/**
 * Utility class for formatting monetary values (MoneyValue objects) in various ways.
 * This class provides methods to format monetary values as per specified locales and currency formats.
 */
public class CurrencyFormatter {

    /**
     * Format the given MoneyValue using the default locale.
     *
     * @param mv The MoneyValue object representing the monetary value to format.
     * @return A formatted String representation of the monetary value according to the default locale.
     */
    public static String formatCurrency(MoneyValue mv) {
        return formatCurrency(mv, Locale.getDefault());
    }

    /**
     * Format the given MoneyValue using the specified locale.
     *
     * @param mv     The MoneyValue object representing the monetary value to format.
     * @param locale The locale specifying the formatting rules (e.g., language, country).
     * @return A formatted String representation of the monetary value according to the specified locale.
     */
    public static String formatCurrency(MoneyValue mv, Locale locale) {
        Currency currency = mv.getCurrency();
        CustomCurrencyFormat customFormat = new CustomCurrencyFormat(locale, currency.getSymbol());
        return customFormat.format(mv.getAmount());
    }

    /**
     * Format the given MoneyValue with its ISO code and amount, using the default locale.
     * @param mv The MoneyValue object representing the monetary value to format.
     * @return A formatted String representation of the monetary value with ISO code and amount.
     */
    public static String formatISOCode(MoneyValue mv){
        return formatISOCode(mv, Locale.getDefault());
    }

    /**
     * Format the given MoneyValue with its ISO code and amount, using the specified locale.
     * This method formats the monetary value without the currency symbol, showing only the ISO code.
     *
     * @param mv     The MoneyValue object representing the monetary value to format.
     * @param locale The locale specifying the formatting rules (e.g., language, country).
     * @return A formatted String representation of the monetary value with ISO code and amount.
     */
    public static String formatISOCode(MoneyValue mv, Locale locale) {
        NumberFormat nfJustNumber = NumberFormat.getNumberInstance(locale);
        nfJustNumber.setRoundingMode(RoundingMode.HALF_UP);
        nfJustNumber.setMinimumFractionDigits(2);

        Currency currency = mv.getCurrency();
        CustomCurrencyFormat customFormat = new CustomCurrencyFormat(locale, currency.getSymbol());

        String formattedAmount = customFormat.format(mv.getAmount());
        String amount = nfJustNumber.format(mv.getAmount());

        if (formattedAmount.startsWith(currency.getSymbol())) {
            return currency.getIsoCode() + " " + amount;
        } else {
            return amount + " " + currency.getIsoCode();
        }
    }
}
