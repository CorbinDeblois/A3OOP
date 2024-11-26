package ePortfolio;

/**
 * Represents a stock, which is a type of investment.
 * This class extends the {@code Investment} class and includes commission handling
 * for stock transactions. A fixed commission is applied to each purchase and sale transaction.
 */
public class Stock extends Investment {
    
    /** A fixed commission applied to each stock transaction. */
    public static final double COMISSION = 9.99;

    /**
     * Constructs a Stock object with a specified symbol, name, quantity, price, and book value.
     * 
     * @param symbol the symbol of the stock
     * @param name the name of the stock
     * @param quantity the quantity of the stock
     * @param price the price of the stock
     * @param bookValue the book value of the stock, includes an additional commission
     */
    public Stock(String symbol, String name, int quantity, double price, double bookValue) {
        super(symbol, name, quantity, price, bookValue);    // Calls parent constructor
    }
    
    /**
     * Constructs a Stock object with a specified symbol, name, quantity, and price.
     * The book value is calculated based on the quantity and price, with an additional commission.
     * 
     * @param symbol the symbol of the stock
     * @param name the name of the stock
     * @param quantity the quantity of the stock
     * @param price the price of the stock
     */
    public Stock(String symbol, String name, int quantity, double price) {
        super(symbol, name, quantity, price);
        setBookValue(quantity * price + COMISSION);
    }

    // Getter and setter methods are inherited from investment parent class

    /**
     * Buys more stocks by updating the quantity and book value.
     * Adds an additional commission to the book value for each purchase.
     * 
     * @param quantity the quantity of stocks to buy
     * @param price the price at which the stocks are purchased
     */
    @Override
    public void buy(int quantity, double price) {
        super.buy(quantity, price);
        setBookValue(getBookValue() + COMISSION);
    }

    /**
     * Sells a portion of stocks by updating the quantity and book value.
     * Prints the payment received after deducting the commission from the sale.
     * 
     * @param quantity the quantity of stocks to sell
     * @param price the price at which the stocks are sold
     */
    @Override
    public void sell(int quantity, double price) {
        double paymentReceived = quantity * price - COMISSION;
        System.out.println(String.format("Payment received by investor is $%.2f", paymentReceived));
        super.sell(quantity, price);
    }

    /**
     * Overridden toString method to provide a formatted string representation of the investment.
     * 
     * @return a string displaying the name, symbol, quantity, price, and book value of the investment
     */
    @Override
    public String toString() {
        return String.format("Stock: %s (%s) | Quantity: %d | Price: %.2f | Book Value: %.2f", super.getName(), super.getSymbol(), super.getQuantity(), super.getPrice(), super.getBookValue());
    }

    /**
     * Overridden toFileFormat method to provide a formatted output to file.
     * 
     * @return a string in the correct format displaying the investment
     */
    @Override
    public String toFileFormat() {
        return "type = \"stock\"\n" +
               "symbol = \"" + this.getSymbol() + "\"\n" +
               "name = \"" + this.getName() + "\"\n" +
               "quantity = \"" + this.getQuantity() + "\"\n" +
               "price = \"" + this.getPrice() + "\"\n" +
               "bookValue = \"" + this.getBookValue() + "\"\n";
    }
}
