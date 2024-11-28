package ePortfolio;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

/**
 * The {@code PortfolioFileReader} class provides file I/O operations for managing investment data.
 * It includes methods to read investment data from a file and to save a list of investments to a file.
 */
public class PortfolioFileReader {

    /**
     * Reads investments from a specified file. This method parses the file line by line,
     * creates {@code Stock} or {@code MutualFund} objects based on the parsed investment type,
     * and populates an {@code ArrayList} with the investment objects.
     * <p>
     * The file should contain each investment's attributes (e.g., type, symbol, name, quantity, price, book value),
     * formatted as key-value pairs, separated by an equals sign.
     * </p>
     * Example file format:
     * <pre>
     * type = "stock"
     * symbol = "AAPL"
     * name = "Apple Inc."
     * quantity = "50"
     * price = "150.00"
     * bookValue = "7500.00"
     * </pre>
     * <p>
     * Investments are added to an {@code ArrayList} only when all required fields are provided.
     * </p>
     *
     * @param fileName the name of the file to read investments from
     * @return an {@code ArrayList} containing the investments read from the file
     */
    public static ArrayList<Investment> readInvestmentsFromFile(String fileName) {
        ArrayList<Investment> investments = new ArrayList<>();
    
        // open file
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) { 
            String investType = null, investSymbol = null, investName = null;
            int investQuantity = 0;
            double investPrice = 0.0, investBookValue = 0.0;
    
            // for each line, take the input and split into two at the = sign
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.isEmpty()) {
                    // Check if all fields are populated to create an investment object
                    if (investType != null && investSymbol != null && investName != null && investQuantity > 0 &&
                        investPrice > 0.0 && investBookValue >= 0.0) {
                        Investment investment = null;
                        if ("stock".equalsIgnoreCase(investType)) {
                            investment = new Stock(investSymbol, investName, investQuantity, investPrice, investBookValue);
                        } else if ("mutualfund".equalsIgnoreCase(investType)) {
                            investment = new MutualFund(investSymbol, investName, investQuantity, investPrice, investBookValue);
                        }
    
                        // Add investment to list and reset fields for the next investment
                        if (investment != null) {
                            investments.add(investment);
                        }
                            
                        // Reset fields for the next investment
                        investType = investSymbol = investName = null;
                        investQuantity = 0;
                        investPrice = investBookValue = 0.0;
                    }
                    continue;
                }
                String[] words = line.split("=", 2);
    
                if (words.length == 2) {
    
                    String key = words[0].trim().toLowerCase();
                    String value = words[1].trim().replaceAll("[\"\"]", "");
                    
                    // create the investment based on the key
                    switch (key) {
                        case "type":
                            investType = value;
                            break;
                        case "symbol":
                            investSymbol = value;
                            break;
                        case "name":
                            investName = value;
                            break;
                        case "quantity":
                            try {
                                investQuantity = Integer.parseInt(value);
                            } catch (NumberFormatException e) {
                                System.err.println("Invalid quantity format: " + value);
                            }
                            break;
                        case "price":
                            try {
                                investPrice = Double.parseDouble(value);
                            } catch (NumberFormatException e) {
                                System.err.println("Invalid price format: " + value);
                            }
                            break;
                        case "bookvalue":
                            try {
                                investBookValue = Double.parseDouble(value);
                            } catch (NumberFormatException e) {
                                System.err.println("Invalid book value format: " + value);
                            }
                            break;
                    }
                }
            }
    
            // After the loop, check if any pending investment (in case there's no empty line at the end)
            if (investType != null && investSymbol != null && investName != null && investQuantity > 0 &&
                investPrice > 0.0 && investBookValue >= 0.0) {
                System.out.println(investBookValue);
                Investment investment = null;
                if ("stock".equalsIgnoreCase(investType)) {
                    investment = new Stock(investSymbol, investName, investQuantity, investPrice, investBookValue);
                } else if ("mutualfund".equalsIgnoreCase(investType)) {
                    investment = new MutualFund(investSymbol, investName, investQuantity, investPrice, investBookValue);
                }
    
                // Add investment to list
                if (investment != null) {
                    investments.add(investment);
                }
            }
        } catch (FileNotFoundException e) {
            System.err.println("File does not exist. It will be created upon saving.");
        } catch (IOException e) {
            System.out.println("An error occurred while reading the file");
        } 
        return investments;
    }     
    
    /**
     * Saves a list of investments to a specified file. Each investment is formatted for easy reading,
     * and the file is created if it does not exist.
     * <p>
     * If the file does not exist, it will be created. Each investment will be saved in a formatted
     * manner that matches the structure expected by {@link #readInvestmentsFromFile(String)}.
     * </p>
     *
     * @param fileName    the name of the file to save investments to
     * @param investments the {@code ArrayList} of investments to save
     */
    public static void saveInvestmentsToFile(String fileName, ArrayList<Investment> investments) {
        File newFile = new File(fileName);
        
        try {
            // create new file if doesnt exist
            if (!newFile.exists() && !newFile.createNewFile()) {
                System.out.println("failed to create new file.");
                return;
            }

            // write to the file
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(newFile))) {

                // for ea investment, add it to file
                for (Investment i : investments) {
                    writer.write(i.toFileFormat());
                    writer.newLine();
                }
                
                System.out.println("Data saved successfully.");
            } catch (IOException e) {
                System.out.println(" an error occured while writing to file");
            }
        } catch (IOException e) {
            System.out.println(" an error occured while creating the file");
        }
    }
}
