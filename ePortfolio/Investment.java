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

    // Constructors

    /**
     * Constructs an {@code Investment} object with the specified attributes.
     * 
     * @param symbol the symbol representing the investment (e.g., AAPL for Apple stock)
     * @param name the name of the investment
     * @param quantity the quantity of units owned
     * @param price the price per unit of the investment
     * @param bookValue the total book value of the investment
     * @throws IllegalArgumentException if symbol or name is null or empty, quantity is non-positive, price is non-positive, or book value is negative
     */
    public Investment(String symbol, String name, int quantity, double price, double bookValue) {
        if (symbol == null || symbol.isEmpty()) {
            throw new IllegalArgumentException("Symbol cannot be null or empty.");
        }
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Name cannot be null or empty.");
        }
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than zero.");
        }
        if (price <= 0) {
            throw new IllegalArgumentException("Price must be greater than zero.");
        }
        if (bookValue < 0) {
            throw new IllegalArgumentException("Book value cannot be negative.");
        }

        this.symbol = symbol;
        this.name = name;
        this.quantity = quantity;
        this.price = price;
        this.bookValue = bookValue;
    }
    
    /**
     * Constructs an {@code Investment} object with the specified attributes.
     * The book value is automatically calculated as {@code quantity * price}.
     * 
     * @param symbol the symbol representing the investment (e.g., AAPL for Apple stock)
     * @param name the name of the investment
     * @param quantity the quantity of units owned
     * @param price the price per unit of the investment
     * @throws IllegalArgumentException if symbol or name is null or empty, quantity is non-positive, or price is non-positive
     */
    public Investment(String symbol, String name, int quantity, double price) {
        if (symbol == null || symbol.isEmpty()) {
            throw new IllegalArgumentException("Symbol cannot be null or empty.");
        }
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Name cannot be null or empty.");
        }
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than zero.");
        }
        if (price <= 0) {
            throw new IllegalArgumentException("Price must be greater than zero.");
        }

        this.symbol = symbol;
        this.name = name;
        this.quantity = quantity;
        this.price = price;
        this.bookValue = quantity * price;
    }

    // Getters

    /**
     * Returns the symbol representing the investment.
     * 
     * @return the investment symbol (e.g., AAPL for Apple stock)
     */
    public String getSymbol() {
        return this.symbol;
    }

     /**
     * Returns the name of the investment.
     * 
     * @return the name of the investment
     */
    public String getName() {
        return this.name;
    }

    /**
     * Returns the quantity of the investment owned.
     * 
     * @return the quantity of units owned
     */
    public int getQuantity() {
        return this.quantity;
    }

    /**
     * Returns the price per unit of the investment.
     * 
     * @return the current price per unit of the investment
     */
    public double getPrice() {
        return this.price;
    }

    /**
     * Returns the total book value of the investment.
     * 
     * @return the book value, representing the total value based on the purchase price
     */
    public double getBookValue() {
        return this.bookValue;
    }

    // Setters

    /**
     * Sets the price per unit of the investment.
     * 
     * @param price the new price per unit of the investment
     * @throws IllegalArgumentException if the price is non-positive
     */
    public void setPrice(double price) {
        if (price <= 0) {
            throw new IllegalArgumentException("Price must be greater than zero.");
        }
        this.price = price;
    }

    /**
     * Sets the quantity of the investment owned.
     * 
     * @param quantity the new quantity of units owned
     * @throws IllegalArgumentException if the quantity is non-positive
     */
    public void setQuantity(int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than zero.");
        }
        this.quantity = quantity;
    }

    /**
     * Sets the book value of the investment.
     * 
     * @param bookValue the new book value of the investment
     * @throws IllegalArgumentException if the book value is negative
     */
    public void setBookValue(double bookValue) {
        if (bookValue < 0) {
            throw new IllegalArgumentException("Book value cannot be negative.");
        }
        this.bookValue = bookValue;
    }

    // Buying and Selling methods on the individual investment level

    /**
     * Increases the quantity of the investment by buying more units.
     * The price is updated to the latest purchase price and the book value is adjusted accordingly.
     * 
     * @param quantity the quantity of units to buy
     * @param price the price per unit at the time of purchase
     * @throws IllegalArgumentException if the quantity or price is non-positive
     */
    public void buy(int quantity, double price) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than zero.");
        }
        if (price <= 0) {
            throw new IllegalArgumentException("Price must be greater than zero.");
        }

        this.quantity += quantity;
        this.price = price;
        this.bookValue += (quantity * price);
    }

    /**
     * Decreases the quantity of the investment by selling units.
     * Updates the book value based on the remaining units.
     * 
     * @param quantity the quantity of units to sell
     * @param price the price per unit at the time of sale
     * @return a message indicating the result of the transaction
     */
    public String sell(int quantity, double price) {
        try {
            if (quantity <= 0) {
                throw new IllegalArgumentException("Quantity must be greater than zero.");
            }
            if (price <= 0) {
                throw new IllegalArgumentException("Price must be greater than zero.");
            }
            if (quantity > this.quantity) {
                throw new IllegalArgumentException("Cannot sell more units than currently owned.");
            }

            double proportion = (double) (this.quantity - quantity) / this.quantity;
            this.bookValue *= proportion;
            this.quantity -= quantity;
            this.price = price;

            return String.format("Successfully sold %d units at %.2f per unit.", quantity, price);
        } catch (IllegalArgumentException ex) {
            return "error: " + ex.getMessage();
        }
    }

    /**
     * Converts the investment details into a format suitable for saving to a file.
     * This method is to be implemented by subclasses to provide specific formatting.
     * 
     * @return a formatted string representing the investment details for file storage
     */
    public abstract String toFileFormat();

    /**
     * Calculates the gain of the investment.
     * This method is to be implemented by subclasses to provide specific calculations.
     * 
     * @return the calculated gain of the investment
     */
    public abstract double getGain();
}
