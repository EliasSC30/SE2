import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.Locale;

public class CurrencyFormatter {


    public static String formatCurrency(MoneyValue mv) {
        NumberFormat nf = NumberFormat.getCurrencyInstance(Locale.getDefault());
        nf.setCurrency(java.util.Currency.getInstance(mv.getCurrency().getIsoCode()));
        return nf.format(mv.getAmount());
    }

    public static String formatCurrency(MoneyValue mv, Locale locale) {
        NumberFormat nf = NumberFormat.getCurrencyInstance(locale);
        nf.setCurrency(java.util.Currency.getInstance(mv.getCurrency().getIsoCode()));
        return nf.format(mv.getAmount());
    }
    public static String formatISOCode(MoneyValue mv, Locale locale) {
        NumberFormat nf = NumberFormat.getCurrencyInstance(locale);


        NumberFormat nfJustNumber = NumberFormat.getNumberInstance(locale);
        nfJustNumber.setRoundingMode(RoundingMode.HALF_UP);
        nfJustNumber.setMinimumFractionDigits(2);

        java.util.Currency currency = java.util.Currency.getInstance(mv.getCurrency().getIsoCode());
        nf.setCurrency(currency);

        String formattedAmount = nf.format(mv.getAmount());
        String symbol = currency.getSymbol(locale);
        if (formattedAmount.startsWith(symbol)) {
            return currency.getCurrencyCode() + " " + nfJustNumber.format(mv.getAmount());
        } else {
            return nfJustNumber.format(mv.getAmount()) + " " + currency.getCurrencyCode();
        }
    }
}
