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
     * @param symbol the symbol of the stock (e.g., AAPL for Apple Inc.)
     * @param name the name of the stock
     * @param quantity the quantity of the stock owned
     * @param price the price per unit of the stock
     * @param bookValue the book value of the stock, includes an additional commission fee
     */
    public Stock(String symbol, String name, int quantity, double price, double bookValue) {
        super(symbol, name, quantity, price, bookValue);    // Calls parent constructor
    }
    
    /**
     * Constructs a Stock object with a specified symbol, name, quantity, and price.
     * The book value is calculated based on the quantity and price, with an additional commission fee.
     * 
     * @param symbol the symbol of the stock (e.g., AAPL for Apple Inc.)
     * @param name the name of the stock
     * @param quantity the quantity of the stock owned
     * @param price the price per unit of the stock
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
     * Sells a specified quantity of stocks by updating the quantity and book value.
     * Calculates the payment received after deducting the commission from the sale.
     * 
     * @param quantity the quantity of stocks to sell
     * @param price the price per unit at which the stocks are sold
     * @return a string message indicating the payment received after deducting the commission
     */
    @Override
    public String sell(int quantity, double price) {
        double paymentReceived = quantity * price - COMISSION;
        String message = "Payment received by investor is " + paymentReceived;
        super.sell(quantity, price);
        return message;
    }

    /**
     * Provides a formatted string representation of the stock.
     * 
     * @return a string displaying the name, symbol, quantity, price, and book value of the stock
     */
    @Override
    public String toString() {
        return String.format("Stock: %s (%s) | Quantity: %d | Price: %.2f | Book Value: %.2f", super.getName(), super.getSymbol(), super.getQuantity(), super.getPrice(), super.getBookValue());
    }

    /**
     * Returns a string representation of the stock in a format suitable for saving to a file.
     * This includes the type, symbol, name, quantity, price, and book value.
     * 
     * @return a string formatted for file output, representing the stock's attributes
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

    /**
     * Calculates the gain for the stock.
     * The gain is calculated as the current value of the stock minus the commission and the book value.
     * 
     * @return the calculated gain for the stock
     */
    @Override
    public double getGain() {
        return (this.getPrice() * this.getQuantity() - COMISSION) - this.getBookValue();
    }
}
