package ePortfolio;

import java.util.Scanner;
import javax.swing.*;
import java.awt.*;
import ePortfolio.PortfolioFileReader;
import java.util.ArrayList;
import java.util.HashSet;

/**
 * The {@code App} class serves as the entry point for the Portfolio management application.
 * It initializes the {@code Portfolio} and manages user interactions, including buying, selling,
 * updating, viewing gain, searching investments, and quitting the application.
 * <p>
 * Usage:
 * <p>
 * {@code java ePortfolio.App}
 */
public class App {

    /**
     * The portfolio instance representing the user's investment portfolio.
     */
    private Portfolio portfolio;

    /**
     * The name of the file used for loading and saving portfolio data.
     */
    private String fileName;

    /**
     * The main frame of the GUI application.
     */
    private JFrame frame;

    // Constants for frame size, fonts, and colours
    private static final int FRAME_WIDTH = 800;
    private static final int FRAME_HEIGHT = 600;

    private static final Font TITLE_FONT = new Font("SansSerif", Font.BOLD, 14);
    private static final Font LABEL_FONT = new Font("SansSerif", Font.PLAIN, 14);
    private static final Font INPUT_FONT = new Font("SansSerif", Font.PLAIN, 14);
    private static final Color BACKGROUND_COLOR = new Color(240, 240, 240);
    private static final Color FOREGROUND_COLOR = new Color(60, 63, 65);

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

        // Set filename as a variable
        String fileName = args[0];

        // Create portfolio object
        Portfolio portfolio = new Portfolio();

        // Read file and populate investment arraylist
        portfolio.setInvestments(PortfolioFileReader.readInvestmentsFromFile(fileName));
        
        // Launch the GUI with the portfolio
        SwingUtilities.invokeLater(() -> new App(portfolio, fileName)); 
    }

    /**
     * Constructor to initialize the ePortfolio GUI.
     *
     * @param portfolio The portfolio to manage.
     * @param fileName  The filename for loading and saving portfolio data.
     */
    public App(Portfolio portfolio, String fileName) {
        this.portfolio = portfolio;
        this.fileName = fileName;

        // Main frame
        frame = new JFrame("ePortfolio");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(FRAME_WIDTH, FRAME_HEIGHT);
        frame.setLayout(new BorderLayout());

        // Create the menu bar
        JMenuBar menuBar = new JMenuBar();
        menuBar.setBackground(new Color(60, 63, 65));
        menuBar.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        JMenu commands = new JMenu("Commands");
        commands.setFont(TITLE_FONT);
        commands.setForeground(Color.WHITE);
        menuBar.add(commands);

        frame.setJMenuBar(menuBar);

        // Create menu items
        JMenuItem buyInvestment = createMenuItem("Buy an Investment");
        JMenuItem sellInvestment = createMenuItem("Sell an Investment");
        JMenuItem updateInvestment = createMenuItem("Update Investments");
        JMenuItem getGain = createMenuItem("Get Gain on Investments");
        JMenuItem search = createMenuItem("Search Investments");
        JMenuItem exit = createMenuItem("Exit and Save");

        // Add menu items to the commands menu
        commands.add(buyInvestment);
        commands.add(sellInvestment);
        commands.add(updateInvestment);
        commands.add(getGain);
        commands.add(search);
        commands.addSeparator();
        commands.add(exit);

        // Add action listeners for menu items
        buyInvestment.addActionListener(e -> showBuyInvestment());
        sellInvestment.addActionListener(e -> showSellInvestment());
        updateInvestment.addActionListener(e -> showUpdateInvestments());
        getGain.addActionListener(e -> showGetTotalGain());
        search.addActionListener(e -> showSearchInvestments());
        exit.addActionListener(e -> {
            PortfolioFileReader.saveInvestmentsToFile(fileName, portfolio.getInvestments());
            System.exit(0);
        });

        // Show the welcome screen
        showWelcome();

        // Make the frame visible
        frame.setVisible(true);
    }

    /**
     * Helper method to create styled menu items.
     *
     * @param text The text to display for the menu item.
     * @return The styled {@code JMenuItem} object.
     */
    private JMenuItem createMenuItem(String text) {
        JMenuItem menuItem = new JMenuItem(text);
        menuItem.setFont(LABEL_FONT);
        menuItem.setBackground(Color.WHITE);
        menuItem.setForeground(FOREGROUND_COLOR);
        menuItem.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        return menuItem;
    }

    /**
     * Displays the welcome screen.
     */
    private void showWelcome() {
        JPanel welcomePanel = new JPanel();
        welcomePanel.setLayout(new BoxLayout(welcomePanel, BoxLayout.Y_AXIS));
        welcomePanel.setBackground(BACKGROUND_COLOR);

        welcomePanel.add(Box.createRigidArea(new Dimension(0, 40)));

        JLabel welcomeMessage = new JLabel("Welcome to ePortfolio");
        welcomeMessage.setFont(new Font("SansSerif", Font.BOLD, 24));
        welcomeMessage.setForeground(FOREGROUND_COLOR);
        welcomeMessage.setAlignmentX(Component.CENTER_ALIGNMENT);
        welcomePanel.add(welcomeMessage);

        welcomePanel.add(Box.createRigidArea(new Dimension(0, 20)));

        JTextArea instructions = new JTextArea(
                "Choose a command from the Commands menu to buy or sell an investment, update prices for all investments, "
                + "get gain for the portfolio, search for relevant investments, or quit the program."
        );
        instructions.setFont(INPUT_FONT);
        instructions.setLineWrap(true);
        instructions.setWrapStyleWord(true);
        instructions.setEditable(false);
        instructions.setBackground(BACKGROUND_COLOR);
        instructions.setAlignmentX(Component.CENTER_ALIGNMENT);
        instructions.setMargin(new Insets(20, 20, 20, 20));
        welcomePanel.add(instructions);

        frame.getContentPane().removeAll();
        frame.add(welcomePanel, BorderLayout.CENTER);
        frame.revalidate();
        frame.repaint();
    }

    /**
     * Displays the "Buy Investment" screen.
     * Allows the user to input details for buying an investment and validates the input fields.
     */
    public void showBuyInvestment() {
        // Input Panel
        JPanel inputPanel = new JPanel(new GridBagLayout());
        inputPanel.setBackground(BACKGROUND_COLOR);
        inputPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                "Buying an investment",
                0,
                0,
                TITLE_FONT,
                FOREGROUND_COLOR
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        // "Type" Dropdown Field
        JLabel typeLabel = new JLabel("Type:");
        typeLabel.setFont(LABEL_FONT);

        JComboBox<String> typeDropdown = new JComboBox<>(new String[]{"Stock", "Mutual Fund"});
        typeDropdown.setFont(INPUT_FONT);

        gbc.gridx = 0;
        gbc.gridy = 0;
        inputPanel.add(typeLabel, gbc);

        gbc.gridx = 1;
        inputPanel.add(typeDropdown, gbc);

        // Other Input Fields
        String[] labels = {"Symbol:", "Name:", "Quantity:", "Price:"};
        JTextField[] textFields = new JTextField[labels.length];

        for (int i = 0; i < labels.length; i++) {
            JLabel label = new JLabel(labels[i]);
            label.setFont(LABEL_FONT);

            JTextField textField = new JTextField(15);
            textField.setFont(INPUT_FONT);
            textFields[i] = textField;

            gbc.gridx = 0;
            gbc.gridy = i + 1;
            inputPanel.add(label, gbc);

            gbc.gridx = 1;
            inputPanel.add(textField, gbc);
        }

        // Button and Message Panels
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.setBackground(BACKGROUND_COLOR);

        JButton resetButton = new JButton("Reset");
        JButton buyButton = new JButton("Buy");

        resetButton.setFont(LABEL_FONT);
        buyButton.setFont(LABEL_FONT);

        resetButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        buyButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        buttonPanel.add(Box.createVerticalGlue());
        buttonPanel.add(resetButton);
        buttonPanel.add(Box.createRigidArea(new Dimension(0, 10))); // Space
        buttonPanel.add(buyButton);
        buttonPanel.add(Box.createVerticalGlue());

        JPanel messagePanel = createMessagePanel();
        JTextArea messageArea = (JTextArea) ((JScrollPane) messagePanel.getComponent(0)).getViewport().getView();

        // Add Action Listener for Reset Button
        resetButton.addActionListener(e -> {
            typeDropdown.setSelectedIndex(0); // Reset dropdown
            for (JTextField textField : textFields) {
                textField.setText(""); // Clear all text fields
            }
            messageArea.setText(""); // Clear messages
        });

        // Add Action Listener for Buy Button
        buyButton.addActionListener(e -> {
            try {
                String type = (String) typeDropdown.getSelectedItem();
                String symbol = textFields[0].getText().trim().toLowerCase();
                if (symbol.contains(" ")) {
                    throw new IllegalArgumentException("The symbol feld cannot contain any spaces.");
                }

                String name = textFields[1].getText().trim().toLowerCase();    
                String quantityStr = textFields[2].getText().trim();
                String priceStr = textFields[3].getText().trim();

                if (symbol.isEmpty() || name.isEmpty() || quantityStr.isEmpty() || priceStr.isEmpty()) {
                    throw new IllegalArgumentException("All fields must be filled out.");
                }

                int quantity = Integer.parseInt(quantityStr);
                if (quantity <= 0) {
                    throw new IllegalArgumentException("Quantity must be positive and non-zero.");
                }

                double price = Double.parseDouble(priceStr);
                if (price <= 0) {
                    throw new IllegalArgumentException("Price must be positive and non-zero.");
                }

                String message = portfolio.buy(type, symbol, name, price, quantity);
                messageArea.setText(message);
            } catch (NumberFormatException ex) {
                messageArea.setText("Quantity and price must be valid numbers.");
            } catch (IllegalArgumentException ex) {
                messageArea.setText(ex.getMessage());
            }
        });

        // Combine Panels
        JPanel buyPanel = createMainPanel(inputPanel, buttonPanel, messagePanel);

        // Update Frame
        frame.getContentPane().removeAll();
        frame.add(buyPanel, BorderLayout.CENTER);
        frame.revalidate();
        frame.repaint();
    }

    /**
     * Displays the "Sell Investment" screen.
     * Allows the user to input details for selling an investment and validates the input fields.
     */
    public void showSellInvestment() {
        // Input Panel
        JPanel inputPanel = new JPanel(new GridBagLayout());
        inputPanel.setBackground(BACKGROUND_COLOR);
        inputPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                "Selling an investment",
                0,
                0,
                TITLE_FONT,
                FOREGROUND_COLOR
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        // Input Fields
        String[] labels = {"Symbol:", "Quantity:", "Price:"};
        JTextField[] textFields = new JTextField[labels.length];

        for (int i = 0; i < labels.length; i++) {
            JLabel label = new JLabel(labels[i]);
            label.setFont(LABEL_FONT);

            JTextField textField = new JTextField(15);
            textField.setFont(INPUT_FONT);
            textFields[i] = textField; // Store the text fields for later access

            gbc.gridx = 0;
            gbc.gridy = i;
            inputPanel.add(label, gbc);

            gbc.gridx = 1;
            inputPanel.add(textField, gbc);
        }

        // Button Panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.setBackground(BACKGROUND_COLOR);

        JButton resetButton = new JButton("Reset");
        JButton sellButton = new JButton("Sell");

        resetButton.setFont(LABEL_FONT);
        sellButton.setFont(LABEL_FONT);

        resetButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        sellButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        buttonPanel.add(Box.createVerticalGlue());
        buttonPanel.add(resetButton);
        buttonPanel.add(Box.createRigidArea(new Dimension(0, 10))); // Space
        buttonPanel.add(sellButton);
        buttonPanel.add(Box.createVerticalGlue());

        // Message Panel
        JPanel messagePanel = createMessagePanel();
        JTextArea messageArea = (JTextArea) ((JScrollPane) messagePanel.getComponent(0)).getViewport().getView();

        // Add Action Listener for Reset Button
        resetButton.addActionListener(e -> {
            for (JTextField textField : textFields) {
                textField.setText(""); // Clear all text fields
            }
            messageArea.setText(""); // Clear messages
        });

        // Add Action Listener for Sell Button
        sellButton.addActionListener(e -> {
            // Retrieve input values
            try {
                String symbol = textFields[0].getText().trim().toLowerCase();
                if (symbol.contains(" ")) {
                    throw new IllegalArgumentException("The symbol feld cannot contain any spaces.");
                }

                String quantityStr = textFields[1].getText().trim();
                String priceStr = textFields[2].getText().trim();

                if (symbol.isEmpty() || quantityStr.isEmpty() || priceStr.isEmpty()) {
                    throw new IllegalArgumentException("All fields must be filled.");
                }

                int quantity = Integer.parseInt(quantityStr);
                if (quantity <= 0) {
                    throw new IllegalArgumentException("Quantity must be be a positive, non-zero, non-decimal integer");
                }

                double price = Double.parseDouble(priceStr);
                if (price <= 0) {
                    throw new IllegalArgumentException("Price must be a positive, non-zero value");
                }

                String message = portfolio.sell(symbol, price, quantity);
                messageArea.setText(message);
            } catch (NumberFormatException ex) {
                messageArea.setText("Quantity and price must be valid numbers.");
            } catch (IllegalArgumentException ex) {
                messageArea.setText(ex.getMessage());
            }
        });

        // Combine Panels
        JPanel sellPanel = createMainPanel(inputPanel, buttonPanel, messagePanel);

        // Update Frame
        frame.getContentPane().removeAll();
        frame.add(sellPanel, BorderLayout.CENTER);
        frame.revalidate();
        frame.repaint();
    }

    /**
     * Displays the "Update Investments" screen.
     * Allows the user to view and update the price of investments in their portfolio.
     */
    public void showUpdateInvestments() {
        // Retrieve the list of investments
        ArrayList<Investment> investments = portfolio.getInvestments();
        if (investments.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "No investments to update.", "Information", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        // Current index to keep track of which investment we're on
        final int[] currentIndex = {0};

        // Input Panel
        JPanel inputPanel = new JPanel(new GridBagLayout());
        inputPanel.setBackground(BACKGROUND_COLOR);
        inputPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                "Updating investments",
                0,
                0,
                TITLE_FONT,
                FOREGROUND_COLOR
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        // Labels and TextFields
        JLabel symbolLabel = new JLabel("Symbol:");
        symbolLabel.setFont(LABEL_FONT);
        JTextField symbolField = new JTextField(15);
        symbolField.setFont(INPUT_FONT);
        symbolField.setEditable(false);

        gbc.gridx = 0;
        gbc.gridy = 0;
        inputPanel.add(symbolLabel, gbc);

        gbc.gridx = 1;
        inputPanel.add(symbolField, gbc);

        JLabel nameLabel = new JLabel("Name:");
        nameLabel.setFont(LABEL_FONT);
        // Use JTextArea for the name field
        JTextArea nameArea = new JTextArea(3, 15);
        nameArea.setFont(INPUT_FONT);
        nameArea.setEditable(false);
        nameArea.setLineWrap(true);
        nameArea.setWrapStyleWord(true);

        JScrollPane nameScrollPane = new JScrollPane(nameArea);
        nameScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        nameScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        gbc.gridx = 0;
        gbc.gridy = 1;
        inputPanel.add(nameLabel, gbc);

        gbc.gridx = 1;
        inputPanel.add(nameScrollPane, gbc);

        JLabel priceLabel = new JLabel("Price:");
        priceLabel.setFont(LABEL_FONT);
        JTextField priceField = new JTextField(15);
        priceField.setFont(INPUT_FONT);

        gbc.gridx = 0;
        gbc.gridy = 2;
        inputPanel.add(priceLabel, gbc);

        gbc.gridx = 1;
        inputPanel.add(priceField, gbc);

        // Button Panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.setBackground(BACKGROUND_COLOR);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JButton prevButton = new JButton("Prev");
        JButton nextButton = new JButton("Next");
        JButton saveButton = new JButton("Save");

        prevButton.setFont(LABEL_FONT);
        nextButton.setFont(LABEL_FONT);
        saveButton.setFont(LABEL_FONT);

        prevButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        nextButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        saveButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        buttonPanel.add(Box.createVerticalGlue());
        buttonPanel.add(prevButton);
        buttonPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        buttonPanel.add(nextButton);
        buttonPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        buttonPanel.add(saveButton);
        buttonPanel.add(Box.createVerticalGlue());

        // Message Panel
        JPanel messagePanel = createMessagePanel();
        JTextArea messageArea = (JTextArea) ((JScrollPane) messagePanel.getComponent(0)).getViewport().getView();

        // Initial Display
        updateDisplay(currentIndex[0], investments, symbolField, nameArea, priceField, prevButton, nextButton);

        // Add Action Listener for Prev Button
        prevButton.addActionListener(e -> {
            if (currentIndex[0] > 0) {
                currentIndex[0]--;
                updateDisplay(currentIndex[0], investments, symbolField, nameArea, priceField, prevButton, nextButton);
            }
        });

        // Add Action Listener for Next Button
        nextButton.addActionListener(e -> {
            if (currentIndex[0] < investments.size() - 1) {
                currentIndex[0]++;
                updateDisplay(currentIndex[0], investments, symbolField, nameArea, priceField, prevButton, nextButton);
            }
        });

        // Add Action Listener for Save Button
        saveButton.addActionListener(e -> {
            String newPriceStr = priceField.getText().trim();

            // Check if price field is empty
            if (newPriceStr.isEmpty()) {
                throw new IllegalArgumentException("Price must have a value inputted in order to update");
            }

            try {
                double newPrice = Double.parseDouble(newPriceStr);
                if (newPrice <= 0) {
                    throw new IllegalArgumentException("Price must be a positive number.");
                }

                // Update the investment's price
                Investment currentInvestment = investments.get(currentIndex[0]);
                currentInvestment.setPrice(newPrice);

                // Display the updated investment
                messageArea.setText("Price updated for investment:\n" + currentInvestment.toString());
            } catch (NumberFormatException ex) {
                messageArea.setText("Invalid price entered. Please enter a valid number.");
            } catch (IllegalArgumentException ex) {
                messageArea.setText(ex.getMessage());
            }
        });

        // Combine Panels
        JPanel updatePanel = createMainPanel(inputPanel, buttonPanel, messagePanel);

        // Update Frame
        frame.getContentPane().removeAll();
        frame.add(updatePanel, BorderLayout.CENTER);
        frame.revalidate();
        frame.repaint();
    }

    /**
     * Helper method for showUpdateInvestments.
     * Updates the display fields for the current investment during the update process.
     *
     * @param index        The index of the current investment being displayed.
     * @param investments  The list of investments in the portfolio.
     * @param symbolField  The text field for displaying the symbol of the investment.
     * @param nameArea     The text area for displaying the name of the investment.
     * @param priceField   The text field for updating the price of the investment.
     * @param prevButton   The button to navigate to the previous investment.
     * @param nextButton   The button to navigate to the next investment.
     */
    private void updateDisplay(int index, ArrayList<Investment> investments, JTextField symbolField, JTextArea nameArea, JTextField priceField, JButton prevButton, JButton nextButton) {
        Investment currentInvestment = investments.get(index);
        symbolField.setText(currentInvestment.getSymbol());
        nameArea.setText(currentInvestment.getName());
        priceField.setText(String.format("%.2f", currentInvestment.getPrice())); // Display current price

        // Enable/disable buttons based on current index
        prevButton.setEnabled(index > 0);
        nextButton.setEnabled(index < investments.size() - 1);
    }

    /**
     * Displays the "Search Investments" screen.
     * Allows the user to search for investments by symbol, keywords, and price range.
     */
    public void showSearchInvestments() {
        // Input Panel
        JPanel inputPanel = new JPanel(new GridBagLayout());
        inputPanel.setBackground(BACKGROUND_COLOR);
        inputPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                "Searching investments",
                0,
                0,
                TITLE_FONT,
                FOREGROUND_COLOR
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        // Input Fields
        String[] labels = {"Symbol:", "Name keywords:", "Low price:", "High price:"};
        JTextField[] textFields = new JTextField[labels.length];

        for (int i = 0; i < labels.length; i++) {
            JLabel label = new JLabel(labels[i]);
            label.setFont(LABEL_FONT);

            JTextField textField = new JTextField(15);
            textField.setFont(INPUT_FONT);
            textFields[i] = textField; // Store the text fields for later retrieval

            gbc.gridx = 0;
            gbc.gridy = i;
            inputPanel.add(label, gbc);

            gbc.gridx = 1;
            inputPanel.add(textField, gbc);
        }

        // Button Panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.setBackground(BACKGROUND_COLOR);

        JButton resetButton = new JButton("Reset");
        JButton searchButton = new JButton("Search");

        resetButton.setFont(LABEL_FONT);
        searchButton.setFont(LABEL_FONT);

        resetButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        searchButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        buttonPanel.add(Box.createVerticalGlue());
        buttonPanel.add(resetButton);
        buttonPanel.add(Box.createRigidArea(new Dimension(0, 10))); // Space
        buttonPanel.add(searchButton);
        buttonPanel.add(Box.createVerticalGlue());

        // Results Panel
        JPanel resultsPanel = new JPanel(new BorderLayout());
        resultsPanel.setBackground(BACKGROUND_COLOR);
        resultsPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                "Search results",
                0,
                0,
                TITLE_FONT,
                FOREGROUND_COLOR
        ));

        JTextArea resultsArea = new JTextArea();
        resultsArea.setFont(INPUT_FONT);
        resultsArea.setLineWrap(true);
        resultsArea.setWrapStyleWord(true);
        resultsArea.setEditable(false);

        JScrollPane scrollPane = new JScrollPane(resultsArea);
        resultsPanel.add(scrollPane, BorderLayout.CENTER);

        // Add Action Listener for Reset Button
        resetButton.addActionListener(e -> {
            for (JTextField textField : textFields) {
                textField.setText(""); // Clear all input fields
            }
            resultsArea.setText(""); // Clear the results area
        });

        // Add Action Listener for Search Button
        searchButton.addActionListener(e -> {
            // Get the user inputs from the text fields
            String symbol = textFields[0].getText().trim();
            String nameKeywords = textFields[1].getText().trim();
            String lowPriceStr = textFields[2].getText().trim();
            String highPriceStr = textFields[3].getText().trim();

            // Initialize price range with defaults
            double lowPrice = 0.0;
            double highPrice = 99999.0;

            try {
                // Parse lowPrice if not empty
                if (!lowPriceStr.isEmpty()) {
                    lowPrice = Double.parseDouble(lowPriceStr);
                }

                // Parse highPrice if not empty
                if (!highPriceStr.isEmpty()) {
                    highPrice = Double.parseDouble(highPriceStr);
                }

                // Validation: Ensure lowPrice <= highPrice
                if (lowPrice > highPrice) {
                    throw new IllegalArgumentException("Error: Low price cannot be greater than high price.");
                }

                // Perform the search using the Portfolio class
                ArrayList<String> results = portfolio.search(symbol, nameKeywords, lowPrice, highPrice);

                // Display the results in the results area
                if (results == null || results.isEmpty()) {
                    throw new IllegalArgumentException("No matching investments found.");
                } else {
                    resultsArea.setText(""); // Clear previous results
                    for (String result : results) {
                        resultsArea.append(result + "\n");
                    }
                }

            } catch (NumberFormatException ex) {
                // Handle invalid price inputs
                resultsArea.setText("Invalid price entered. Please enter valid numeric values for prices.");
            } catch (IllegalArgumentException ex) {
                resultsArea.setText(ex.getMessage());
            } catch (Exception ex) {
                // Handle any unexpected errors
                resultsArea.setText("An unexpected error occurred: " + ex.getMessage());
            }
        });

        // Combine Panels
        JPanel searchPanel = createMainPanel(inputPanel, buttonPanel, resultsPanel);

        // Update Frame
        frame.getContentPane().removeAll();
        frame.add(searchPanel, BorderLayout.CENTER);
        frame.revalidate();
        frame.repaint();
    }

    /**
     * Displays the "Get Gain on Investments" screen.
     * Shows the total gain and individual gains of each investment in the portfolio.
     */
    public void showGetTotalGain() {
        // Declare totalGainField as a local variable
        JTextField totalGainField = new JTextField(15);
        
        // Create Total Gain Panel, passing totalGainField as a parameter
        JPanel totalGainPanel = createTotalGainPanel(totalGainField);

        // Get the individual gains from the portfolio
        ArrayList<Double> individualGain = portfolio.getGain();
        double totalGain = 0.0;

        // Create a StringBuilder to build the individual gains string
        StringBuilder individualGainsBuilder = new StringBuilder();

        // Get the list of investments
        ArrayList<Investment> investments = portfolio.getInvestments();

        // Loop through the investments and their gains
        for (int i = 0; i < individualGain.size(); i++) {
            double gain = individualGain.get(i);
            totalGain += gain;

            Investment inv = investments.get(i);
            String investmentType = inv instanceof Stock ? "Stock" : "Mutual Fund";

            individualGainsBuilder.append(String.format("Gain for %s %s (%s): %.2f%n",
                    investmentType, inv.getName(), inv.getSymbol(), gain));
        }

        // Convert the StringBuilder to a String
        String individualGains = individualGainsBuilder.toString();

        // Set the total gain in the text field
        totalGainField.setText(String.format("%.2f", totalGain));

        // Create Individual Gains Panel
        JPanel individualGainsPanel = createMessagePanel();
        individualGainsPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                "Individual gains",
                0,
                0,
                TITLE_FONT,
                FOREGROUND_COLOR
        ));

        // Set the individual gains in the message area
        JTextArea messageArea = (JTextArea) ((JScrollPane) individualGainsPanel.getComponent(0)).getViewport().getView();
        messageArea.setText(individualGains);

        // Combine Panels
        JPanel gainPanel = new JPanel(new BorderLayout());
        gainPanel.add(totalGainPanel, BorderLayout.NORTH);
        gainPanel.add(individualGainsPanel, BorderLayout.CENTER);

        // Update Frame
        frame.getContentPane().removeAll();
        frame.add(gainPanel, BorderLayout.CENTER);
        frame.revalidate();
        frame.repaint();
    }

    /**
     * Helper function for getting the total gain on investments.
     * Creates the panel for displaying the total gain.
     *
     * @param totalGainField The text field for displaying the total gain.
     * @return The panel containing the total gain display.
     */
    private JPanel createTotalGainPanel(JTextField totalGainField) {
        JPanel totalGainPanel = new JPanel(new GridBagLayout());
        totalGainPanel.setBackground(BACKGROUND_COLOR);
        totalGainPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                "Getting total gain",
                0,
                0,
                TITLE_FONT,
                FOREGROUND_COLOR
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        JLabel totalGainLabel = new JLabel("Total gain:");
        totalGainLabel.setFont(LABEL_FONT);

        // Initialize totalGainField here
        totalGainField.setFont(INPUT_FONT);
        totalGainField.setEditable(false); // Make it non-editable

        gbc.gridx = 0;
        gbc.gridy = 0;
        totalGainPanel.add(totalGainLabel, gbc);

        gbc.gridx = 1;
        totalGainPanel.add(totalGainField, gbc);

        return totalGainPanel;
    }

    /**
     * Helper function to create the main panel layout by combining input, button, and bottom panels.
     *
     * @param inputPanel  The panel for user inputs.
     * @param buttonPanel The panel for control buttons.
     * @param bottomPanel The panel for messages or additional UI elements.
     * @return The combined main panel.
     */
    private JPanel createMainPanel(JPanel inputPanel, JPanel buttonPanel, JPanel bottomPanel) {
        inputPanel.setPreferredSize(new Dimension(frame.getWidth() * 2 / 3, frame.getHeight()));
        buttonPanel.setPreferredSize(new Dimension(frame.getWidth() / 3, frame.getHeight()));
        bottomPanel.setPreferredSize(new Dimension(frame.getWidth(), frame.getHeight() / 3));

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(inputPanel, BorderLayout.CENTER);
        topPanel.add(buttonPanel, BorderLayout.EAST);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(topPanel, BorderLayout.CENTER);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        return mainPanel;
    }

    /**
     * Helper function to create an input panel with labeled text fields.
     *
     * @param title  The title of the input panel.
     * @param labels The labels for the input fields.
     * @return The input panel with labeled text fields.
     */
    private JPanel createInputPanel(String title, String[] labels) {
        JPanel inputPanel = new JPanel(new GridBagLayout());
        inputPanel.setBackground(BACKGROUND_COLOR);
        inputPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                title,
                0,
                0,
                TITLE_FONT,
                FOREGROUND_COLOR
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        for (int i = 0; i < labels.length; i++) {
            JLabel label = new JLabel(labels[i]);
            label.setFont(LABEL_FONT);
            JTextField textField = new JTextField(15);
            textField.setFont(INPUT_FONT);

            gbc.gridx = 0;
            gbc.gridy = i;
            inputPanel.add(label, gbc);

            gbc.gridx = 1;
            inputPanel.add(textField, gbc);
        }

        return inputPanel;
    }

    /**
     * Helper function to create a button panel with specified button labels.
     *
     * @param buttonLabels The labels for the buttons in the panel.
     * @return The button panel with buttons.
     */
    private JPanel createButtonPanel(String[] buttonLabels) {
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.setBackground(BACKGROUND_COLOR);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        for (String label : buttonLabels) {
            JButton button = new JButton(label);
            button.setFont(LABEL_FONT);
            button.setAlignmentX(Component.CENTER_ALIGNMENT);

            buttonPanel.add(Box.createVerticalGlue());
            buttonPanel.add(button);
            buttonPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        }
        buttonPanel.add(Box.createVerticalGlue());

        return buttonPanel;
    }

    /**
     * Helper function to create a message panel for displaying messages or search results.
     *
     * @return The message panel.
     */
    private JPanel createMessagePanel() {
        JPanel messagePanel = new JPanel(new BorderLayout());
        messagePanel.setBackground(BACKGROUND_COLOR);
        messagePanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                "Messages",
                0,
                0,
                TITLE_FONT,
                FOREGROUND_COLOR
        ));

        JTextArea messageArea = new JTextArea();
        messageArea.setFont(INPUT_FONT);
        messageArea.setLineWrap(true);
        messageArea.setWrapStyleWord(true);
        messageArea.setEditable(false);

        JScrollPane scrollPane = new JScrollPane(messageArea);
        messagePanel.add(scrollPane, BorderLayout.CENTER);

        return messagePanel;
    }
}
