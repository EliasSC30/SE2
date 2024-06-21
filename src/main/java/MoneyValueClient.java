/**
 * A client interface for operations on MoneyValue objects.
 */
public interface MoneyValueClient {

    /**
     * Retrieves the currency of the MoneyValue.
     *
     * @return The currency of the MoneyValue.
     */
    Currency getCurrency();

    /**
     * Adds another MoneyValue to this MoneyValue.
     *
     * @param other The MoneyValue to add.
     * @return The resulting MoneyValue after addition.
     */
    MoneyValue add(MoneyValue other);

    /**
     * Subtracts another MoneyValue from this MoneyValue.
     *
     * @param other The MoneyValue to subtract.
     * @return The resulting MoneyValue after subtraction.
     */
    MoneyValue subtract(MoneyValue other);

    /**
     * Multiplies this MoneyValue by another MoneyValue.
     *
     * @param other The MoneyValue to multiply by.
     * @return The resulting MoneyValue after multiplication.
     */
    MoneyValue multiply(MoneyValue other);

    /**
     * Divides this MoneyValue by another MoneyValue.
     *
     * @param other The MoneyValue to divide by.
     * @return The resulting MoneyValue after division.
     */
    MoneyValue divide(MoneyValue other);
}
