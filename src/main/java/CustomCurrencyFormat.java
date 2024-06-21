import java.text.*;
import java.util.Locale;

public class CustomCurrencyFormat extends NumberFormat {
    private final DecimalFormat df;

    public CustomCurrencyFormat(Locale locale, String symbol) {
        this.df = (DecimalFormat) NumberFormat.getCurrencyInstance(locale);
        DecimalFormatSymbols dfs = df.getDecimalFormatSymbols();
        dfs.setCurrencySymbol(symbol);
        df.setDecimalFormatSymbols(dfs);
    }

    @Override
    public StringBuffer format(double number, StringBuffer toAppendTo, FieldPosition pos) {
        return df.format(number, toAppendTo, pos);
    }

    @Override
    public StringBuffer format(long number, StringBuffer toAppendTo, FieldPosition pos) {
        return df.format(number, toAppendTo, pos);
    }

    @Override
    public Number parse(String source, ParsePosition parsePosition) {
        return df.parse(source, parsePosition);
    }
}
