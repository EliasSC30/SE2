
public interface MoneyValueClient {
    Currency getCurrency();
    MoneyValue add(MoneyValue other);
    MoneyValue subtract(MoneyValue other);
    MoneyValue multiply(MoneyValue other);
    MoneyValue divide(MoneyValue other);
}
