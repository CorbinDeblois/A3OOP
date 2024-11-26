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
     * or creating a new investment if not owned. Prompts the user to input
     * details such as type, symbol, quantity, and price.
     * 
     * @param scanner the {@code Scanner} object to read user input
     */
    public void buy(Scanner scanner) {
        
        // Prompt user to enter the kind of Investment
        boolean correctInvestmentType = false;
        System.out.println("Please enter the kind of investment you are buying (stock or mutualfund): ");
        String investmentType = null;
        while (!correctInvestmentType) {
            investmentType = scanner.nextLine().toLowerCase().replaceAll("\\s", "");;
            if (investmentType.startsWith("s") || investmentType.startsWith("m")) {
                correctInvestmentType = true;
            } else {
                System.out.println("Invalid investment type. Please enter a valid investment type (stock or mutualfund): ");
            }
        }

        // Prompt user to enter the Symbol of the investment
        System.out.println("Please enter the symbol of the investment: ");
        String investmentSymbol = scanner.nextLine().toLowerCase().replaceAll("\\s", "");

        // Prompt the user to enter the price of the Investment
        boolean correctPriceType = false;
        double price = 0;
        System.out.println("Enter the price of " + investmentSymbol + ": ");
        while (!correctPriceType) {
            /* The below blocks of code is inspired from 
             * Geeks for Geeks. 
             * https://www.geeksforgeeks.org/exceptions-in-java/
             */
            try {
                price = scanner.nextDouble();
                scanner.nextLine();
                if (price <= 0) {
                    System.out.println("Price cannot be negative. Please enter a positive numeric value: ");
                } else {
                    correctPriceType = true;
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid investment price input. Please enter a numeric value without characters:");
                scanner.nextLine();
            } 
        }

        // Prompt the user to enter the quantity of the investment
        boolean correctQuantityInput = false;
        System.out.println("Enter how many " + investmentType + "s you want to purchase: ");
        int quantityPurchase = 0; 
        while (!correctQuantityInput) { 
            try {
                quantityPurchase = scanner.nextInt();
                scanner.nextLine();
                if (quantityPurchase <= 0) {
                    System.out.println("Quantity cannot be negative. Please enter a positive numeric value: ");
                } else {
                    correctQuantityInput = true;
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid investment quantity input. Please enter a numeric value without character or decimal values: ");
                scanner.nextLine();
            }
        }
            
        // If investment exists:
        Investment existingInvestment = null;
        for (Investment i : investments) {
            if (i.getSymbol().equals(investmentSymbol)) {
                existingInvestment = i;
                // Update the stocks related attributes
                existingInvestment.buy(quantityPurchase, price);
                System.out.println("Successfully purchased " + existingInvestment.getQuantity() + " " + investmentType + " of " + existingInvestment.getName());
                break;     
            }
        }  
                
        // If stock doesnt exist:
        if (existingInvestment == null) {
                    
            // Prompt user to enter name of stock
            System.out.println("Enter the name of the investment you are buying: ");
            String investmentName = scanner.nextLine();

            // Create new object stock
            Investment newInvestment = null;
            if (investmentType.startsWith("s")) {
                newInvestment = new Stock(investmentSymbol, investmentName, quantityPurchase, price);
            } else if (investmentType.startsWith("m")) {
                newInvestment = new MutualFund(investmentSymbol, investmentName, quantityPurchase, price);
            }

            // Add stock to arraylist
            investments.add(newInvestment);
            System.out.println("Successfully purchased " + newInvestment.getQuantity() + " " + investmentType + " of " + newInvestment.getName());
        }
    }

    /**
     * Sells a specified quantity of an investment, updating its quantity or removing it if sold completely.
     * The user specifies the symbol, quantity, and price, with validation of input values.
     * 
     * @param scanner the {@code Scanner} object to read user input
     */
    public void sell(Scanner scanner) {

        // Prompt user to provide symbol
        System.out.println("Enter the symbol of the investment you are selling: ");
        String symbol = scanner.nextLine().toLowerCase().replaceAll("\\s", "");

        Investment investmentToSell = null;

        // Search if investment exists based off symbol
        for (Investment i : investments) {
            if (symbol.equals(i.getSymbol())) {
                investmentToSell = i; 
                break;
            }
        } 

        // If investment doesnt exist, print error message and exit
        if (investmentToSell == null) {
            System.out.println("Investment " + symbol + " not found.");
            return;
        }

        // Prompt user to provide price when being sold
        System.out.println("Enter the price that you are selling " + symbol + " at: ");
        double actualPrice = 0;
        boolean actualPriceIsDouble = false;
        while (!actualPriceIsDouble) {
            try {
                actualPrice = scanner.nextDouble();
                scanner.nextLine();
                if (actualPrice <= 0) {
                    System.out.println("Invalid price input. Please enter a positive value: ");
                } else {
                    actualPriceIsDouble = true; 
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid investment actual price input. Please enter a numeric value without a character: ");
                scanner.nextLine();
            }
        }

        // Prompt user to provide quantity to sell
        System.out.print("Enter the quantity of " + symbol + " that you are selling: ");
        int quantityToSell = 0;
        boolean isQuantityToSellInputCorrect = false;
        
        while (!isQuantityToSellInputCorrect) {
            try {
                quantityToSell = scanner.nextInt();
                scanner.nextLine();
                if (quantityToSell <= 0) {
                    System.out.println("Please enter a positive integer value. Quantity cannot be negative: ");;
                } else {
                    isQuantityToSellInputCorrect = true;
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid investment quantity input. Please enter a numeric value without characters or decimal values:  ");
                scanner.nextLine();
            }
        }

        // If quantity to be sold is < quantity available.
        int currentQuantity = investmentToSell.getQuantity();
        if (currentQuantity < quantityToSell) {
            System.out.println("Error: Not enough quantity to sell. You currently have " + currentQuantity + "/" + quantityToSell);
            return;
        }

        // If quantity to sell is the same as quantity available to sell
        if (quantityToSell == currentQuantity) {
                    
            // Remove from arraylist
            investmentToSell.sell(quantityToSell, actualPrice);
            investments.remove(investmentToSell);
            System.out.println("Successfully sold " + quantityToSell + " of " + symbol + ".");

        // If quantity to sell is less then quantity available to sell
        } else if (quantityToSell < currentQuantity) {

            // Update attributes
            investmentToSell.sell(quantityToSell, actualPrice);
            System.out.println("Successfully sold " + quantityToSell + " of " + symbol + ".");
        }
    }

    /**
     * Updates the price of each investment in the portfolio by prompting
     * the user for new values, validating that the input price is non-negative.
     * 
     * @param scanner the {@code Scanner} object to read user input
     */
    public void update(Scanner scanner) {
        
        // Update prices for all stocks
        System.out.println("Updating prices for all investments...");
        double newPrice = 0;
        boolean correctPriceUpdate;
        for (Investment i : investments) {
            correctPriceUpdate = false;
            
            if (i instanceof Stock) {
                System.out.println("Enter the new price for stock " + i.getName() + " (" + i.getSymbol() + ", Current Price: " + i.getPrice() + "): ");
            } else if (i instanceof MutualFund) {
                System.out.println("Enter the new price for mutualfund " + i.getName() + " (" + i.getSymbol() + ", Current Price: " + i.getPrice() + "): ");
            }
            
            while (!correctPriceUpdate) {
                try {
                    newPrice = scanner.nextDouble();
                    scanner.nextLine();
                    if (newPrice < 0) {
                        System.out.println("Please enter a positve price value: ");
                    } else {
                        correctPriceUpdate = true;
                        i.setPrice(newPrice);
                        System.out.println("Updated " + i.getName() + " to new price: " + newPrice);
                    }
                } catch (InputMismatchException e) {
                    System.out.println("Invalid Investment price type. Please enter a value that is only numeric and doesnt contain any characters: ");
                    scanner.nextLine();
                }
            }
        }
    }

    /**
     * Calculates and displays the gain for each investment individually,
     * as well as the total portfolio gain.
     */  
    public void getGain() {
        double totalGain = 0.0;
        
        // Calculate gain for all stocks
        System.out.println("Calculating gain for all investments...");
        double gain;
        for (Investment i : investments) {
            if (i instanceof Stock) {
                gain = (i.getPrice() * i.getQuantity() - Stock.COMISSION) - i.getBookValue();
                System.out.printf("Gain for stock %s (%s): %.2f%n", i.getName(), i.getSymbol(), gain);
                totalGain += gain;
            } else if (i instanceof MutualFund) {
                gain = (i.getPrice() * i.getQuantity() - MutualFund.REDEMPTIONFEE) - i.getBookValue();
                System.out.printf("Gain for mutual fund %s (%s): %.2f%n", i.getName(), i.getSymbol(), gain);
                totalGain += gain;
            }
        }
    
        // Print total gain
        System.out.printf("Total portfolio gain: %.2f%n", totalGain);
    }
    
    /**
     * Searches for investments based on a symbol, keywords in the name, and a price range.
     * Displays matching investments if found, or a message if no matches are found.
     * 
     * @param scanner the {@code Scanner} object to read user input
     */
    public void search(Scanner scanner) {

        // Prompt user to enter the symbol (optional)
        System.out.println("Enter the symbol of the investment (or leave blank): ");
        String tickerSymbol = scanner.nextLine().toLowerCase().trim();

        // Prompt user to enter multiple keywords for the name (optional)
        System.out.println("Enter keyword(s) for the name (separate with spaces). Leave blank if not needed: ");
        String[] nameKeywords = scanner.nextLine().toLowerCase().trim().split("\\s+");

        // Prompt user to enter the price range (optional)
        System.out.println("Enter the price range (e.g., 10.00-100.00 for $10 to $100, -100.00 for $100 or lower, 10.00- for $10 or more, 15 for exactly $15): ");
        String priceRange = scanner.nextLine().trim();

        Double lowPriceRange = 0.0;
        Double highPriceRange = Double.MAX_VALUE; // Set high to max by default
        
        // Parsing the price input properly
        if (!priceRange.isEmpty()) {
            try {
                String[] tempValues = priceRange.split("-");

                if (tempValues.length == 1) { // Only one value given
                    if (priceRange.startsWith("-")) {
                        // Case: "-100" (upper bound only)
                        highPriceRange = Double.parseDouble(tempValues[0].substring(1));
                        lowPriceRange = 0.0;
                    } else if (priceRange.endsWith("-")) {
                        // Case: "100" (lower bound only)
                        lowPriceRange = Double.parseDouble(tempValues[0]);
                        highPriceRange = Double.MAX_VALUE;
                    } else {
                        lowPriceRange = Double.parseDouble(tempValues[0]);
                        highPriceRange = Double.parseDouble(tempValues[0]);
                    }
                } else if (tempValues.length == 2) { // Two values given, e.g., "10-100"
                    if (!tempValues[0].isEmpty()) {
                        lowPriceRange = Double.parseDouble(tempValues[0]);
                    }
                    if (!tempValues[1].isEmpty()) {
                        highPriceRange = Double.parseDouble(tempValues[1]);
                    }
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid price range format. Please enter a valid numeric range.");
                return;
            }
        }

        // create set of all the matching indexes 
        Set<Integer> matchingIndexes = new HashSet<>(); // to gather matching indexes

        // If keywords provided, initialize mathcing Indexes with the indexes for the first keyword
        String keyword;
        if (nameKeywords.length > 0 && nameIndex.containsKey(nameKeywords[0])) {
            matchingIndexes.addAll(nameIndex.get(nameKeywords[0]));

            // For each subsequent keyword, perform an intersection with matchingKeywords
            for (int i = 1; i < nameKeywords.length; i++) {
                keyword = nameKeywords[i];
                
                if (nameIndex.containsKey(keyword)) {
                    // Get indexes for current keyword
                    Set<Integer> keywordIndexes = new HashSet<>(nameIndex.get(keyword));
                    // keep only indexes common to both sets
                    matchingIndexes.retainAll(keywordIndexes);
                } else {
                    // if any keyword has no matches, clear
                    matchingIndexes.clear();
                    break;
                }
            }
        } else {
            // if no keywords are provided, add all indexes to start
            for (int i = 0; i < investments.size(); i++) {
                matchingIndexes.add(i);
            }
        }

        System.out.println("Search Results: ");
        boolean found = false;

        // now iterate over the matching indexes and apply the symbols and price filters
        for (Integer index : matchingIndexes) {
            Investment investment = investments.get(index);
            if (matchesCriteria(investment, tickerSymbol, lowPriceRange, highPriceRange)) {
                System.out.println(investment);
                found = true;
                System.out.println("--------------------------------------------------");
            }
        }

        if (!found) {
            System.out.println("No investments found matching the criteria.");
        }
    }

    /**
     * Helper method to determine if an investment matches the specified search criteria
     * 
     * @param investment the investment being checked
     * @param symbol the desired symbol, or empty if not filtering by symbol
     * @param lowPrice the minimum price, or 0 if not filtering by lower bound
     * @param highPrice the maximum price, or Double.MAX_VALUE if not filtering by upper bound
     * @return {@code true} if the investment matches the criteria; {@code false} otherwise
     */
    private boolean matchesCriteria(Investment investment, String symbol, Double lowPrice, Double highPrice) {
        boolean matchesSymbol = symbol.isEmpty() || investment.getSymbol().equalsIgnoreCase(symbol);

        double investmentPrice = investment.getPrice();
        boolean matchesPrice = investmentPrice >= lowPrice && investmentPrice <= highPrice;

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

    /**
     * Returns the list of investments in the portfolio.
     * 
     * @return an {@code ArrayList} of {@code Investment} objects
     */
    public ArrayList<Investment> getInvestments() {
        return this.investments;
    }

    /**
     * Sets the list of investments in the portfolio.
     * 
     * @param investments the {@code ArrayList} of investments to set in the portfolio
     */
    public void setInvestments(ArrayList<Investment> investments) {
        this.investments = investments;
    }
}

