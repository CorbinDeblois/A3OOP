package ePortfolio;

import java.util.Scanner;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Map;

/**
 * The {@code Portfolio} class manages a collection of investments, including stocks and mutual funds.
 * It provides methods to buy, sell, update investments, calculate total gains, and search for investments
 * based on criteria like symbol, keywords, and price range.
 */
public class Portfolio {
    
    // Attributes
    private ArrayList<Investment> investments;
    private HashMap<String, ArrayList<Integer>> nameIndex;

    /**
     * Constructs a new, empty {@code Portfolio} object.
     */
    public Portfolio() {
        investments = new ArrayList<>();
        nameIndex = new HashMap<>(); 
    }

    /**
     * Buys an investment by adding to the existing quantity if already owned,
     * or creating a new investment if not owned.
     *
     * @param type            the type of investment, either "Stock" or "Mutual Fund"
     * @param investmentSymbol the symbol representing the investment (e.g., AAPL)
     * @param name            the name of the investment
     * @param price           the price per unit at the time of purchase
     * @param quantityPurchase the quantity of units to purchase
     * @return a message indicating the result of the purchase operation
     */
    public String buy(String type, String investmentSymbol, String name, double price, int quantityPurchase) {
        try {
            // Validate input
            if (investmentSymbol == null || investmentSymbol.trim().isEmpty()) {
                throw new IllegalArgumentException("Investment symbol cannot be empty.");
            }
            if (name == null || name.trim().isEmpty()) {
                throw new IllegalArgumentException("Investment name cannot be empty.");
            }
            if (quantityPurchase <= 0) {
                throw new IllegalArgumentException("Quantity must be greater than zero.");
            }
            if (price <= 0) {
                throw new IllegalArgumentException("Price must be greater than zero.");
            }

            Investment existingInvestment = null;

            // If investment exists:
            for (Investment i : investments) {
                if (i.getSymbol().equalsIgnoreCase(investmentSymbol)) {
                    existingInvestment = i;

                    // Check if the type matches
                    if ((type.equalsIgnoreCase("STOCK") && !(i instanceof Stock)) ||
                        (type.equalsIgnoreCase("MUTUAL FUND") && !(i instanceof MutualFund))) {
                        throw new IllegalArgumentException("The type selected doesn't match the type of the existing investment.");
                    }

                    // Check if the name matches
                    if (!i.getName().equalsIgnoreCase(name)) {
                        throw new IllegalArgumentException("The name of the investment corresponding to the given symbol is " + existingInvestment.getName() + " not " + name + ". Please try again with the correct name.");
                    }

                    // Add more quantity to the existing investment
                    existingInvestment.buy(quantityPurchase, price);
                    return "Successfully purchased an additional " + quantityPurchase + " " + type + "s of " + name + " (" + investmentSymbol + ")"; 
                } 
            }

            // If investment doesn't exist:
            if (existingInvestment == null) {
                Investment newInvestment;

                if (type.equalsIgnoreCase("STOCK")) {
                    newInvestment = new Stock(investmentSymbol, name, quantityPurchase, price);
                } else if (type.equalsIgnoreCase("MUTUAL FUND")) {
                    newInvestment = new MutualFund(investmentSymbol, name, quantityPurchase, price);
                } else {
                    throw new IllegalArgumentException("Invalid investment type. Must be 'Stock' or 'Mutual Fund'.");
                }

                // Add investment to ArrayList
                investments.add(newInvestment);
                return "Successfully purchased " + quantityPurchase + " " + type + "s of " + newInvestment.getName();
            }
        } catch (IllegalArgumentException ex) {
            return "Error: " + ex.getMessage();
        }
        return null;
    }

    /**
     * Sells a specified quantity of an investment, updating its quantity or removing it if sold completely.
     *
     * @param symbol        the symbol of the investment to be sold
     * @param sellingPrice  the price per unit at the time of sale
     * @param quantityToSell the quantity of units to sell
     * @return a message indicating the result of the sale operation
     */
    public String sell(String symbol, double sellingPrice, int quantityToSell) {
        try {
            // Validate input
            if (symbol == null || symbol.trim().isEmpty()) {
                throw new IllegalArgumentException("Investment symbol cannot be empty.");
            }
            if (quantityToSell <= 0) {
                throw new IllegalArgumentException("Quantity to sell must be greater than zero.");
            }
            if (sellingPrice <= 0) {
                throw new IllegalArgumentException("Selling price must be greater than zero.");
            }

            Investment investmentToSell = null;

            // Search if investment exists based on symbol
            for (Investment i : investments) {
                if (symbol.equalsIgnoreCase(i.getSymbol())) {
                    investmentToSell = i;
                    break;
                }
            }

            // If investment doesn't exist
            if (investmentToSell == null) {
                throw new IllegalArgumentException("Investment " + symbol + " not found.");
            }

            // If quantity to be sold exceeds the available quantity
            int currentQuantity = investmentToSell.getQuantity();
            if (currentQuantity < quantityToSell) {
                throw new IllegalArgumentException("Error: Not enough quantity to sell. You currently have " + currentQuantity + "/" + quantityToSell);
            }

            // Handle sale of entire or partial investment
            String message2 = investmentToSell.sell(quantityToSell, sellingPrice);
            if (quantityToSell == currentQuantity) {
                investments.remove(investmentToSell);
            }
            return "Successfully sold " + quantityToSell + " of " + symbol + "." + "\n" + message2;

        } catch (IllegalArgumentException ex) {
            return "Error: " + ex.getMessage();
        }
    }

    /**
     * Calculates and returns the gain for each investment individually.
     *
     * @return an {@code ArrayList} of {@code Double} representing the gain for each investment
     */ 
    public ArrayList<Double> getGain() {
        ArrayList<Double> individualGains = new ArrayList<>();

        // Calculate gain for all stocks
        int count = 0;
        for (Investment i : investments) {
            individualGains.add(i.getGain());
        }

        return individualGains;
    }
    
    /**
     * Searches for investments based on a symbol, keywords in the name, and a price range.
     *
     * @param tickerSymbol the symbol of the investment to search for (optional)
     * @param keywords     the keywords in the investment name to search for (optional)
     * @param lowPrice     the minimum price range for search
     * @param highPrice    the maximum price range for search
     * @return an {@code ArrayList} of {@code String} representing matching investments, or {@code null} if no matches are found
     */
    public ArrayList<String> search(String tickerSymbol, String keywords, double lowPrice, double highPrice) {
        updateKeywordIndex();

        // Trim and split keywords
        String[] nameKeywords = keywords.trim().split("\\s+");
        Set<Integer> matchingIndexes = new HashSet<>();

        // If keywords are provided, start filtering by the first keyword
        if (!keywords.trim().isEmpty()) {
            if (nameIndex.containsKey(nameKeywords[0].toLowerCase())) {
                matchingIndexes.addAll(nameIndex.get(nameKeywords[0].toLowerCase()));

                // For each subsequent keyword, perform an intersection with the matching indexes
                for (int i = 1; i < nameKeywords.length; i++) {
                    String keyword = nameKeywords[i].toLowerCase();
                    if (nameIndex.containsKey(keyword)) {
                        matchingIndexes.retainAll(nameIndex.get(keyword));
                    } else {
                        // If any keyword has no matches, return no results
                        return null;
                    }
                }
            } else {
                // If the first keyword doesn't match anything, return no results
                return null;
            }
        } else {
            // If no keywords are provided, include all investments initially
            for (int i = 0; i < investments.size(); i++) {
                matchingIndexes.add(i);
            }
        }

        // Now filter by symbol and price range
        ArrayList<String> resultList = new ArrayList<>();
        for (Integer index : matchingIndexes) {
            Investment investment = investments.get(index);
            if (matchesCriteria(investment, tickerSymbol, lowPrice, highPrice)) {
                resultList.add(investment.toString());
            }
        }

        return resultList.isEmpty() ? null : resultList;
    }


    /**
     * Helper method to determine if an investment matches the specified search criteria.
     *
     * @param investment the investment being checked
     * @param symbol     the desired symbol, or empty if not filtering by symbol
     * @param lowPrice   the minimum price, or 0 if not filtering by lower bound
     * @param highPrice  the maximum price, or {@code Double.MAX_VALUE} if not filtering by upper bound
     * @return {@code true} if the investment matches the criteria; {@code false} otherwise
     */
    private boolean matchesCriteria(Investment investment, String symbol, Double lowPrice, Double highPrice) {
        boolean matchesSymbol = symbol.isEmpty() || investment.getSymbol().equalsIgnoreCase(symbol);

        double investmentPrice = investment.getPrice();
        boolean matchesPrice = (lowPrice == null || investmentPrice >= lowPrice) && (highPrice == null || investmentPrice <= highPrice);
        return matchesSymbol && matchesPrice;
    }

    /**
     * Updates the keyword index for investment names, mapping each unique keyword
     * to the list of indexes where it appears within the portfolio. This facilitates
     * faster searching by keywords.
     */
    public void updateKeywordIndex() {

        nameIndex.clear(); // clear map and rebuild

        // Iterate through all investments
        for (int i = 0; i < investments.size(); i++) {
            
            // Get their name and split it up into keywords
            Investment investment = investments.get(i);
            String name = investment.getName();
            String[] keywords = name.split("\\s+");

            // For each keyword
            for (String keyword : keywords) {
                keyword = keyword.toLowerCase(); // Normalize the keyword to lowercase
                
                // 1. Check if the keyword already exists in the map
                if (nameIndex.containsKey(keyword)) {
                    ArrayList<Integer> indexes = nameIndex.get(keyword);

                    // If true check if the current index is already in the list
                    // if it is, that would be an error
                    if (!indexes.contains(i)) {
                        indexes.add(i);
                    }
                } else {
                    // If the keyword doesnt exist, create a new list and add the current index
                    ArrayList<Integer> newIndexes = new ArrayList<>();
                    newIndexes.add(i);
                    nameIndex.put(keyword, newIndexes);
                }

            }
        }
    }

    // Getters

    /**
     * Returns the list of investments in the portfolio.
     *
     * @return an {@code ArrayList} of {@code Investment} objects
     */
    public ArrayList<Investment> getInvestments() {
        return this.investments;
    }

    // Setters

    /**
     * Sets the list of investments in the portfolio.
     *
     * @param investments the {@code ArrayList} of investments to set in the portfolio
     */
    public void setInvestments(ArrayList<Investment> investments) {
        this.investments = investments;
    }
}

