package ePortfolio;

/** 
 * Represents a financial investment with attributes like symbol, name, quantity, price, and book value.
 * This class serves as a parent to specific investment types such as {@code MutualFund} and {@code Stock}.
 * It provides methods for managing investment transactions, including buying and selling.
 */
public abstract class Investment {
  
    // Attributes
    private String symbol;
    private String name;
    private int quantity;
    private double price;
    private double bookValue;

    /**
     * Primary constructor for the Investment class.
     * 
     * @param symbol the symbol representing the investment (e.g., AAPL for Apple stock)
     * @param name the name of the investment
     * @param quantity the quantity of units owned
     * @param price the price per unit of the investment
     * @param bookValue the total book value of the investment
     */
    public Investment(String symbol, String name, int quantity, double price, double bookValue) {
        this.symbol = symbol;
        this.name = name;
        this.quantity = quantity;
        this.price = price;
        this.bookValue = bookValue;
    }
    
    /**
     * Overloaded constructor for the Investment class that automatically calculates the book value.
     * 
     * @param symbol the symbol representing the investment (e.g., AAPL for Apple stock)
     * @param name the name of the investment
     * @param quantity the quantity of units owned
     * @param price the price per unit of the investment
     * 
     * The book value is automatically calculated as quantity * price.
     */
    public Investment(String symbol, String name, int quantity, double price) { 
        this.symbol = symbol;
        this.name = name;
        this.quantity = quantity;
        this.price = price;

        this.bookValue = quantity * price; 
    }

    /**
     * Getter for the investment symbol.
     * 
     * @return the symbol representing the investment (e.g., AAPL for Apple stock)
     */
    public String getSymbol() {
        return this.symbol;
    }

    /**
     * Getter for the investment name.
     * 
     * @return the name of the investment
     */
    public String getName() {
        return this.name;
    }

    /**
     * Getter for the quantity of the investment owned.
     * 
     * @return the quantity of units owned
     */
    public int getQuantity() {
        return this.quantity;
    }

    /**
     * Getter for the price per unit of the investment.
     * 
     * @return the current price per unit of the investment
     */
    public double getPrice() {
        return this.price;
    }

    /**
     * Getter for the total book value of the investment.
     * 
     * @return the book value, which represents the total investment value based on the purchase price
     */
    public double getBookValue() {
        return this.bookValue;
    }

    /**
     * Setter for the price of the investment.
     * 
     * @param price the new price per unit of the investment
     */
    public void setPrice(double price) {
        this.price = price;
    }

    /**
     * Setter for the quantity of the investment.
     * 
     * @param quantity the new quantity of units owned
     */
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    /**
     * Setter for the book value of the investment.
     * 
     * @param bookValue the new book value of the investment
     */
    public void setBookValue(double bookValue) {
        this.bookValue = bookValue;
    }

    /**
     * Method to increase the quantity of the investment by buying more units.
     * The price is updated to the latest purchase price and the book value is adjusted accordingly.
     * 
     * @param quantity the quantity of units to buy
     * @param price the price per unit at the time of purchase
     */
    public void buy(int quantity, double price) {
        this.quantity = this.quantity + quantity;
        this.price = price;
        this.bookValue = this.bookValue + (quantity * price);
    }

    /**
     * Method to decrease the quantity of the investment by selling units.
     * The price is updated to the selling price and the book value is adjusted based on the remaining units.
     * 
     * @param quantity the quantity of units to sell
     * @param price the price per unit at the time of sale
     */
    public void sell(int quantity, double price) {
        this.bookValue = this.bookValue * ((double) (this.quantity - quantity) / this.quantity);
        this.quantity = this.quantity - quantity;
        this.price = price;
    }

    // Abstract method to be implemented by subclasses
    public abstract String toFileFormat();
}
