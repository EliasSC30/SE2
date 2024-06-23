import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.Locale;

public class CurrencyFormatter {

    public static String formatCurrency(MoneyValue mv) {
        return formatCurrency(mv, Locale.getDefault());
    }

    public static String formatCurrency(MoneyValue mv, Locale locale) {
        Currency currency = mv.getCurrency();
        CustomCurrencyFormat customFormat = new CustomCurrencyFormat(locale, currency.getSymbol());
        return customFormat.format(mv.getAmount());
    }

    public static String formatISOCode(MoneyValue mv){
        return formatISOCode(mv, Locale.getDefault());
    }

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
