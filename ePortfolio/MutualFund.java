package ePortfolio;

/**
 * Represents a mutual fund, which is a type of investment.
 * This class extends the {@code Investment} class and includes a redemption fee
 * when selling mutual fund shares.
 * <p>
 * The {@code REDEMPTIONFEE} constant represents the fixed fee deducted from each sale of mutual fund shares.
 * </p>
 */
public class MutualFund extends Investment {
    
    /** 
     * A fixed fee applied when selling mutual fund shares, deducted from the sale payment.
     */
    public static final double REDEMPTIONFEE = 45;

    // Constructors

    /**
     * Constructs a {@code MutualFund} object with a specified symbol, name, quantity, price, and book value.
     * 
     * @param symbol the symbol of the mutual fund
     * @param name the name of the mutual fund
     * @param quantity the quantity of the mutual fund shares
     * @param price the price per mutual fund share
     * @param bookValue the book value of the mutual fund
     * @throws IllegalArgumentException if the symbol or name is null or empty, quantity is non-positive, 
     *                                  price is non-positive, or book value is negative
     */
    public MutualFund(String symbol, String name, int quantity, double price, double bookValue) {
        super(symbol, name, quantity, price, bookValue);
    }

    /**
     * Constructs a {@code MutualFund} object with a specified symbol, name, quantity, and price.
     * The book value is calculated based on the quantity and price.
     * 
     * @param symbol the symbol of the mutual fund
     * @param name the name of the mutual fund
     * @param quantity the quantity of the mutual fund shares
     * @param price the price per mutual fund share
     * @throws IllegalArgumentException if the symbol or name is null or empty, quantity is non-positive, 
     *                                  or price is non-positive
     */
    public MutualFund(String symbol, String name, int quantity, double price) {
        super(symbol, name, quantity, price);
    }

    // Getter and setter methods are inherited from investment parent class
    
    // Method to buy investment inherited from investment class
    
    /**
     * Sells a portion of mutual fund shares by updating the quantity and book value.
     * Calculates the payment received after deducting the redemption fee.
     * 
     * @param quantity the quantity of mutual fund shares to sell
     * @param price the price at which the mutual fund shares are sold
     * @return a string message with the payment details
     * @throws IllegalArgumentException if the quantity is non-positive, the price is non-positive,
     *                                  the quantity exceeds the available quantity, or if the redemption fee
     *                                  results in a negative payment
     */
    @Override
    public String sell(int quantity, double price) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than zero.");
        }
        if (price <= 0) {
            throw new IllegalArgumentException("Price must be greater than zero.");
        }
        if (quantity > this.getQuantity()) {
            throw new IllegalArgumentException("Cannot sell more units than currently owned.");
        }

        double paymentReceived = quantity * price - REDEMPTIONFEE;

        String message = String.format("Payment received by investor is %.2f", paymentReceived);
        super.sell(quantity, price);
        return message;
    }

    /**
     * Returns a formatted string representation of the mutual fund investment.
     * 
     * @return a string displaying the name, symbol, quantity, price, and book value of the mutual fund
     */
    @Override
    public String toString() {
        return String.format("Mutual Fund: %s (%s) | Quantity: %d | Price: %.2f | Book Value: %.2f", super.getName(), super.getSymbol(), super.getQuantity(), super.getPrice(), super.getBookValue());
    }

    /**
     * Returns a string representation of the mutual fund in a format suitable for saving to a file.
     * This includes the type, symbol, name, quantity, price, and book value.
     * 
     * @return a string formatted for file output, representing the mutual fund's attributes
     */
    @Override
    public String toFileFormat() {
        return "type = \"mutualfund\"\n" +
               "symbol = \"" + this.getSymbol() + "\"\n" +
               "name = \"" + this.getName() + "\"\n" +
               "quantity = \"" + this.getQuantity() + "\"\n" +
               "price = \"" + this.getPrice() + "\"\n" +
               "bookValue = \"" + this.getBookValue() + "\"\n";
    }

    /**
     * Calculates the gain for the mutual fund investment.
     * The gain is computed as the current value of the investment minus the redemption fee and book value.
     * 
     * @return the calculated gain for the mutual fund
     */
    @Override
    public double getGain() {
        double currentValue = this.getPrice() * this.getQuantity();
        double gain = currentValue - REDEMPTIONFEE - this.getBookValue();
        return gain;
    }
}
