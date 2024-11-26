package ePortfolio;

import java.util.Scanner;
import javax.swing.*;
import java.awt.*;
import ePortfolio.PortfolioFileReader;

/**
 * The {@code App} class serves as the entry point for the Portfolio management application.
 * It initializes the {@code Portfolio} and manages user interactions, including buying, selling,
 * updating, viewing gain, searching investments, and quitting the application.
 * <p>
 * Usage:
 * <pre> 
 * {@code java PortfolioApp <filename>}
 * </pre>
 */
public class App {

    // Attribute
    private Portfolio portfolio;
    private JFrame frame;

    /**
     * Main method to run the Portfolio management program. Initializes a portfolio,
     * loads investments from a specified file, and provides a command interface for
     * users to manage their investments.
     *
     * @param args command line arguments where args[0] is expected to be the filename
     *             for loading and saving portfolio data.
     */
    public static void main(String[] args) {

        // Check if filename was provided
        if (args.length < 1) {
            System.out.println("To use the program: java Portfolio <filename>");
            return;
        }

        // Scanner object to scan
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter 1 to use the GUI, enter 2 to use command line: ");
        int gui = scanner.nextInt();
        scanner.nextLine();

        // Set filename as a variable
        String fileName = args[0];

        // Create portfolio object
        Portfolio portfolio = new Portfolio();

        // Read file and populate investment arraylist
        portfolio.setInvestments(PortfolioFileReader.readInvestmentsFromFile(fileName));

        // Boolean variable for looping
        boolean loop = true;
        
        if (gui == 2) {
            while (loop) {
                
                // Prompt user to enter command
                System.out.println("Enter a command (buy, sell, update, getGain, search, quit): ");
                
                // Call method to take input and return corresponding number
                int command = App.commandLoop(scanner);

                // Do appropriate action to portfolio
                switch (command) {
                    case 1: // Buy action
                    portfolio.buy(scanner);
                    break;
                    case 2: // Sell action
                        portfolio.sell(scanner);
                        break;
                    case 3: // Update action
                        portfolio.update(scanner);
                        break;
                    case 4: // GetGain action
                        portfolio.getGain();
                        break;
                    case 5: // Search action
                        portfolio.updateKeywordIndex();
                        portfolio.search(scanner);
                        break;
                    case 6: // Quit action
                        System.out.println("Quitting program...");
                        loop = false;
                        break;
                    case 7: // Invalid command action
                        System.out.println("Invalid command. Please try again");
                        break;
                }
            }

            // Save investments to file
            PortfolioFileReader.saveInvestmentsToFile(fileName, portfolio.getInvestments());

            // Close scanner object
            scanner.close();
        } else if (gui == 1) {
            boolean fileLoaded = false;

            // Launch the GUI with the portfolio
            SwingUtilities.invokeLater(() -> new App(portfolio)); 
        }
    }

    /**
     * Prompts the user for a command and returns an integer corresponding
     * to the selected command for easier handling within the application.
     *
     * @param scanner the {@code Scanner} object to read user input
     * @return an integer representing the command entered by the user:
     *         <ul>
     *             <li>1 for buy</li>
     *             <li>2 for sell</li>
     *             <li>3 for update</li>
     *             <li>4 for getGain</li>
     *             <li>5 for search</li>
     *             <li>6 for quit</li>
     *             <li>7 for invalid command</li>
     *         </ul>
     */
    private static int commandLoop(Scanner scanner) {
        
        // Initialize string to store input
        String command = null;

        // Take user input
        command = scanner.nextLine();

        // Remove whitespace and capitals from input to handle robust input
        command = command.toLowerCase().replaceAll("\\s", "");

        // Check for partial matches / matches
        if (command.startsWith("b")) {
            return 1;
        } else if (command.startsWith("sel")) {
            return 2;
        } else if (command.startsWith("u")) {
            return 3;
        } else if (command.startsWith("g")) {
            return 4;
        } else if (command.startsWith("sea")) {
            return 5;
        } else if (command.startsWith("q")) {
            return 6;
        } else {
            return 7;
        }
    }

    public App(Portfolio portfolio) {
        this.portfolio = portfolio;

        // Main frame
        frame = new JFrame("ePortfolio");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLayout(new BorderLayout());

        // Menu Bar
        JMenuBar menuBar = new JMenuBar();
        JMenu commands = new JMenu("Commands");

        // Add items to the commands menu
        JMenuItem buyInvestment = new JMenuItem("Buy an investment");
        JMenuItem sellInvestment = new JMenuItem("Sell an investment");
        JMenuItem updateInvestment = new JMenuItem("Update investments");
        JMenuItem getGain = new JMenuItem("Get gain on investments");
        JMenuItem search = new JMenuItem("Search investments");
        JMenuItem exit = new JMenuItem("Exit");

        // Add menu items to the commands menu
        commands.add(buyInvestment);
        commands.add(sellInvestment);
        commands.add(updateInvestment);
        commands.add(getGain);
        commands.add(search);
        commands.addSeparator();
        commands.add(exit);

        // Action listener for buyInvestment item

        // Action listener for sellInvestment item 

        // Action listener for updateInvestment item

        // Action listener for getGain item

        // Action listener for search item

        // Action listener for exit item
        exit.addActionListener(e -> System.exit(0));
    }
}
