# ePortfolio Investment Management System

## 1. General Problem

The **ePortfolio Investment Management System** is designed to manage a user's investment portfolio, allowing the user to track, buy, sell, and update stocks and mutual funds. The program provides functionality for calculating total portfolio gains, searching investments based on various criteria, and updating investment prices. This solution aims to simulate a simplified portfolio management tool with core features necessary to manage a basic portfolio, focusing on both stocks and mutual funds.

## 2. Assumptions and Limitations

- **Assumptions:**
    - The user is familiar with basic investment concepts like buying, selling, and gains calculation.
    - Users will input reasonable values such as non-negative prices, valid symbols, and quantities for investments.
    - The program assumes that when selling investments, the user has enough shares to cover the sale.
    - The program handles commands case-insensitively and accepts partial matches for most commands (e.g., `q` for `quit`).
    
- **Limitations:**
    - This system does not account for advanced investment types or features like dividends, splits, or detailed transaction history.
    - The system operates on a command-line interface, which limits the usability for non-technical users.
    - There is no persistent storage; once the program terminates, all investment data is lost.

## 3. User Guide (Building and Running the Program)

### Prerequisites:
- You need Java installed (JDK version 8 or above) to compile and run the program.

### Steps to Compile and Run:
1. Download all the source files in the `ePortfolio` package (including `Portfolio.java`, `Stock.java`, `MutualFund.java`, and `Investment.java`).
2. Open a terminal or command prompt.
3. Navigate to the directory where the files are located.
4. Compile the program by running the command:
    ```
    javac ePortfolio/*.java
    ```
5. Run the program by typing:
    ```
    java ePortfolio.App <filename.txt>

6. The program will create a new file called whatever you input. Additionally, I have provided you with a file loaded with investments if you'd like to use it. It must be located in the same directory as `ePortfolio` (but not inside ePortfolio).  
    ```
    java ePortfolio.App cdebloisPortfolio.txt

### Commands Available:
- **buy:** Buy a stock or mutual fund.
- **sell:** Sell a stock or mutual fund.
- **update:** Update the prices of all investments.
- **getGain:** Calculate the total gain of the portfolio.
- **search:** Search for investments based on symbol, keywords, or price range.
- **quit:** Exit the program.

### Example Session:
> **Program Output:**
>
> `Enter a command (buy, sell, update, getGain, search, quit):`  
>
> **User Input:**
>
> `buy`
>
> **Program Output:**
>
> `Please enter the kind of investment you are buying (stock or mutualfund):`
>
> **User Input:**
>
> `stock`
>
> **Program Output:**
>
> `Please enter the symbol of the investment:`
>
> **User Input:**
>
> `AAPL`
>
> **Program Output:**
>
> `Enter the price of the AAPL:`
>
> **User Input:**
>
> `150.00`
>
> **Program Output:**
>
> `Enter how many stocks you want to purchase:`
>
> **User Input:**
>
> `10`
>
> **Program Output:**
>
> `Successfully purchased 10 stocks of AAPL.`


## 4. Test Plan

This test plan outlines the scenarios and inputs used to test the functionality of the **Portfolio** program, ensuring correct behavior and proper handling of inputs.

### 4.1. **Functionality**

This test plan covers the primary conditions for testing ePortfolio Investment Manage Application.

#### 4.1.1. **Buy Command Functionality**

- Allows for reasonable differences from "stock" or "mutualfund" when asked to enter the kind of investment you are buying
- Allows for any non-negative, non-zero double to be entered for price. Will not allow negative values or strings, or zero.
- Allows for any non-negative integers to be entered for quantity. Will not allow zero, a string, or a negative integer.
- Updating existing investment quantities if you are purchasing a pre-existing investment.
- Adds new investments to the portfolio if the symbol is unique.

#### 4.1.2. **Sell Command Functionality**

- Allows for case insensitive and some white space on the outsides when entering of a symbol. If symbol is not familiar, you will not beable to sell.
- Allows for any positive integer to be inputted for price. It will not allow a negative integer, a zero, or a string.
- Allows for any positive integer to be inputted for quantity. If the value is negative, zero, or a string, you will not beable to sell.
- If the quantity you want to sell is greater then the quantity you own, you will not beable to sell.
- Updates the quantity of the investment.
- Updates the price of the investment.
- Removes the investment if you sold all of the quantity

#### 4.1.3. **Update Command Functionality**

- For every investment owned, it will ask you to enter an updated price.
- You will be asked to enter a valid price if the value you enter is negative, or a string.

#### 4.1.5. **Get Gain Command Functionality**

- This function simply computes the amount of loss or gain you have based on your book value and current worth, including fees.
- It will output total portfolio gain and individual loss or gain for each stock

#### 4.1.6. **Quit Command Functionality**

- This command will shut down the program

## 5. Possible Improvements

If given more time, the following improvements could be made:
- **Persistent Storage:** Implement file-based storage or a database to save investment data between program runs.
- **GUI Interface:** Build a graphical user interface to make the program more user-friendly.
- **Advanced Investment Types:** Add support for more complex investments such as bonds or options.
- **Transaction History:** Provide a detailed transaction history for each investment.
- **Error Handling:** Improve error handling and user feedback for more robust input validation.

## 6. Updates November 11th, 2024

### 6.1 File-Based Storagess
Implemented file-based storage to save investment data between program runs.
### 6.2 Search function 
The search function has been upgraded to make use of hashmaps for faster searching and keyword lookup.
### 6.3 Bug Fixes
#### 6.3.1 Buy and sell investments
The buy and sell functions no longer the price and quantity values to be 0.
#### 6.3.2 Search investments
1. The search function no longers accepts word level matches (ex; when searching for Can, it is no longer matches with Canada)
2. The search function no longer returns outputs every investment when you input a single price.
3. The search function now works with the greater then (80-)
#### 6.3.3 Command Loop
The program used to be not be defensive enough, it would accept "selling" and "buying" when it should be rejected. 
#### 6.3.4 Other Quality of Life Upgrades
- Reduced the two arraylists into one.

### 6.2 Improvements after Update
- **GUI Interface:** Build a graphical user interface to make the program more user-friendly.
- **Advanced Investment Types:** Add support for more complex investments such as bonds or options.
- **Transaction History:** Provide a detailed transaction history for each investment.