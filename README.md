ePortfolio Investment Management System

1. General Problem

The ePortfolio Investment Management System is designed to help users manage their investment portfolios, focusing on stocks and mutual funds. This system allows users to track investments, buy, sell, update prices, calculate portfolio gains, and search investments based on different criteria. The system simulates a simplified portfolio management tool, making it easier to understand the core functionalities of managing basic investments.

2. Assumptions and Limitations

Assumptions:

The user is familiar with basic investment concepts like buying, selling, and calculating gains.

Users will input reasonable values, such as non-negative prices, valid symbols, and quantities.

The program enforces constraints on selling too much of an asset and ensures valid inputs.

Limitations:

This system does not support advanced investment features like dividends, splits, or detailed transaction history.

The program operates via a graphical user interface (GUI).

Persistent storage is implemented using a file-based storage solution, which may be limited compared to databases.

3. User Guide

3.1. Prerequisites:

Java (JDK version 8 or above) is required to compile and run the program.

3.2. Steps to Compile and Run:

Download all source files in the ePortfolio package, including Portfolio.java, Stock.java, MutualFund.java, Investment.java, PortfolioFileReader.java, and App.java.

Open a terminal or command prompt.

Navigate to the directory where the files are located.

Compile the program by running:

javac ePortfolio/*.java

Run the program in command line mode or GUI mode:

Command-line mode:

java ePortfolio.App <filename.txt>

Example:

java ePortfolio.App cdebloisPortfolio.txt

The program will create a new file to store investment data if the given filename does not exist.

3.3. Commands Available in the Drop-Down Menu (GUI):

Buy an Investment: Buy a stock or mutual fund.

Sell an Investment: Sell a stock or mutual fund.

Update Investments: Update the prices of all investments.

Get Gain on Investments: Calculate the total gain of the portfolio.

Search Investments: Search for investments based on symbol, keywords, or price range.

Exit and Save: Exit the program and save data.

4. Test Plan

The test plan covers the main functionalities of the ePortfolio Investment Management System to ensure it behaves correctly under different conditions.

4.1. Functionality

4.1.1. Buy Command

Accepts "stock" or "mutual fund" as valid input types in the drop-down menu.

If the investment already exists, it validates that the name inputted matches the name already in the portfolio. It will not allow the purchase if it does not match.

Allows positive, non-zero values for quantity and price.

Updates the quantity if an existing investment is purchased again.

Adds new investments to the portfolio if the symbol is unique.

4.1.2. Sell Command

Accepts symbols in a case-insensitive manner.

Symbols cannot have spaces in them.

Allows positive integers for quantity.

Prevents selling more units than currently owned.

Updates or removes the investment from the portfolio after a sale.

4.1.3. Update Command

Asks for updated prices for every investment.

Previous and next buttons are disabled when at the start or end of the portfolio, respectively.

Accepts positive values for price.

4.1.4. Get Gain Command

Computes individual gain or loss for each investment.

Displays the total portfolio gain.

4.1.5. Search Command

Searches based on symbol, keywords in investment names, and a price range.

Uses a hashmap to index investment keywords for faster searches.

4.1.6. Exit Command

Exits and saves the program safely.

5. Possible Improvements

Advanced Persistent Storage: Implementing a database solution instead of file-based storage to handle larger portfolios more effectively.

Detailed Transaction History: Adding a complete transaction history for each investment, allowing users to view their past transactions.

Enhanced Error Handling: Improving the robustness of the system with better user input validation and detailed error messages.

GUI Enhancements: Expanding the GUI to provide more visual elements, such as graphs for portfolio performance and summary statistics.

Advanced Investment Types: Including additional investment types, such as bonds or options, to provide a more complete portfolio management experience.

6. Updates

6.1. November 11th, 2024

6.1.1. File-Based Storage

Implemented file-based storage to save investment data between program runs, ensuring data persistence.

6.1.2. Improved Search Functionality

The search function now utilizes hashmaps for faster searching and keyword lookup, making searches more efficient.

6.1.3. Bug Fixes and Quality Improvements

Buy and Sell Commands: The buy and sell functions no longer allow zero values for price and quantity.

Search Function: Fixed issues where partial matches would return incorrect results. Enhanced the search to only return exact keyword matches.

Command Loop: Improved defensiveness by only allowing valid commands (e.g., rejecting inputs like "selling" instead of "sell").

Code Refactoring: Reduced redundant ArrayLists to improve efficiency and maintainability.

6.2. November 27th, 2024

6.2.1. GUI: Added a User Interface that Supports Each Command

Implemented a graphical user interface that allows users to access all commands via drop-down menus and buttons.

6.2.2. Additional Error Checking

Enhanced error checking to provide more robust validation for user inputs.

This README outlines the core functionality, usage, and possible enhancements of the ePortfolio Investment Management System. The program provides a solid foundation for portfolio management and can be further expanded to meet the needs of more sophisticated users.

