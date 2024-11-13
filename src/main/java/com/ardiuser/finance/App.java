package main.java.com.ardiuser.finance;

import main.java.com.ardiuser.finance.model.Transaction;
import main.java.com.ardiuser.finance.model.User;
import main.java.com.ardiuser.finance.database.TransactionDAO;
import main.java.com.ardiuser.finance.database.UserDAO;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Scanner;


public class App {
    public static void main(String[] args) throws SQLException {
        // initialize scanner for user input
        Scanner in = new Scanner(System.in);

        // greet, then prompt user to either log in to existing account or create one
        System.out.println("Hello, Personal Finance Manager Here");
        System.out.println("Do you wish to 'log in' or 'create user'?");
        String log_or_create = in.nextLine(); // gather input
        
        User user = firstPrompt(log_or_create); // pass input to the first prompt
        
        if (user != null) { // check if user is not null
        secondPrompt(user); // pass verified user to the second prompt
        }
        in.close();
        
    }
    public static User firstPrompt(String log_or_create) throws SQLException {
        // if they choose to log in:
        UserDAO userDAO = new UserDAO();
        @SuppressWarnings("resource")
        Scanner in = new Scanner(System.in);
        User user = null;

        if (log_or_create.equalsIgnoreCase("log in")) {
            // prompt user to input username and password
            System.out.println("You entered 'log in'...");
            System.out.print("Enter username: ");
            String username = in.nextLine().trim();
            System.out.print("Enter password: ");
            String password = in.nextLine();
            
            // create user object using provided inputs
            user = userDAO.loginUser(username, password);

            // if user is found, display user's information
            if (user != null) {
                System.out.println("Login successful!");
                System.out.println("User ID: " + user.getUserId());
                System.out.println("Username: " + user.getUsername());
                System.out.println("Email: " + user.getEmail());
    
                // add functionality once logged in from here
                // -->
                // call second prompt function
                secondPrompt(user); // (asks user to view statement or make transaction)
            } 
            else { // if login fails, inform user to press run again.
                
                System.out.println("Invalid username or password. Press run to try again.");
            }
            
        
        } 
        else if  (log_or_create.equalsIgnoreCase("create user")) { // if user selects 'create user':
        // prompt user to input username, password, and email
        System.out.println("You selected 'create user'...");
        System.out.print("Enter username: ");
        String username = in.nextLine();
        System.out.print("Enter password: ");
        String password = in.nextLine();
        System.out.print("Enter email: ");
        String email = in.nextLine();

        // create newUser object
        User newUser = new User(0, username, password, email);  // ID is 0 since it will be auto-generated

        // Pass the newUser object to UserDAO to save it to the database
        boolean isAdded = userDAO.addUser(newUser);

        if (isAdded) { // if added succesfully:
            System.out.println("User registered successfully!");
        } else { // if not:
            System.out.println("Registration failed.");
        }
       
        }
        else {
            System.out.println("Invalid input, please press run to try again.");
        }
        return user;
    }

    public static void secondPrompt(User user) throws SQLException {
        @SuppressWarnings("resource")
        // initialize scanner for user input, and estbalish connection with transactionDAO 
        Scanner in = new Scanner(System.in);
        TransactionDAO transactionDAO = new TransactionDAO();

        // initiialize variables outside of loop so they can be rereferenced
        boolean logged_out = false;
        BigDecimal amount;
        String dateString;
        Date date;
        String category;

        // repeatedly loop until user logs out
        while (!logged_out){ 
            System.out.println("\nWould you like to:");
            System.out.println("A - All Transactions");
            System.out.println("V - View Specific Transaction");
            System.out.println("M - Make Transaction");
            System.out.println("U - Update Transaction");
            System.out.println("D - Delete Transaction");
            System.out.println("T - Total Balance");
            System.out.println("L - Log Out");

            // gather user input, trimming white space
            String userInput = in.nextLine().trim();

            // if user selects A
            if (userInput.equalsIgnoreCase("A")){ 
                // print new line, then communicate with transactionDAO and getAllTransactions method
                System.out.println();
                List<Transaction> transactions = transactionDAO.getAllTransactions(user.getUserId());
                
                // print each transaction in the transactions list
                for (Transaction t : transactions) {
                System.out.println(t);
                }
            }

            // if user selects V
            else if (userInput.equalsIgnoreCase("V")){ 
                // prompt to enter transaction ID, and gather input
                System.out.print("Enter the Transaction ID: ");
                int transactionId = in.nextInt();
                in.nextLine(); 

                // communicate with transactionDAO and getTransactionById method
                Transaction transaction = transactionDAO.getTransactionById(transactionId);
                
                // check if transaction actually exists
                if (transaction != null) {
                    // if it does, print details
                    System.out.println("Transaction Details: ");
                    System.out.println(transaction);
                }
                else {
                    // if it doesn't, print error message
                    System.out.println("Transaction not found.");
                }
            }   
            
            // if user selects M
            else if (userInput.equalsIgnoreCase("M")) {
                // prompt to enter details and gather inputs
                System.out.print("Enter the amount: ");
                amount = in.nextBigDecimal();
                in.nextLine();

                System.out.print("Enter the description: ");
                String description = in.nextLine();
            
                System.out.print("Enter the date (yyyy-mm-dd): ");
                dateString = in.nextLine();
                date = java.sql.Date.valueOf(dateString);
            
                System.out.print("Enter the category: ");
                category = in.nextLine();

                // create a new transaction object with the data inputted above
                Transaction transaction = new Transaction(0, amount, description, date, category, user.getUserId());
                
                try {
                    // communicate with transactionDAO and addTransaction method
                    transactionDAO.addTransaction(transaction);
                    System.out.println("Transaction added succesfully!");
                }
                catch (Exception e) {
                    // if unsuccessful, print error message
                    System.out.println("Error adding transaction " + e.getMessage());
                } 
            }

            else if (userInput.equalsIgnoreCase("U")) {
                System.out.print("Enter the ID of the transaction you wish to update: ");
                int transactionId = in.nextInt();
                in.nextLine();

                Transaction transaction = transactionDAO.getTransactionById(transactionId);
                if (transaction != null) {
                    // update amount prompt and gather data
                    System.out.print("Enter a new amount (current: " + transaction.getAmount() + "): ");
                    BigDecimal newAmount = in.nextBigDecimal();
                    in.nextLine();
                    
                    // update description prompt and gather data
                    System.out.print("Enter a new description (current: " + transaction.getDescription() + "): ");
                    String newDescription = in.nextLine();

                    // update date prompt and gather data
                    System.out.print("Enter a new date (yyyy-mm-dd) (current: " + transaction.getDate() + "): ");
                    String newDateString = in.nextLine();
                    Date newDate = java.sql.Date.valueOf(newDateString);

                    // update category prompt and gather data
                    System.out.print("Enter a new category (current: " + transaction.getCategory() + "): ");
                    String newCategory = in.nextLine();

                    // set parameters for transaction 
                    transaction.setAmount(newAmount);
                    transaction.setDescription(newDescription);
                    transaction.setDate(newDate);
                    transaction.setCategory(newCategory);

                    boolean updated = transactionDAO.updateTransaction(transaction);
                    
                    // check if update transaction was successfull 
                    if (updated) {
                        System.out.println("Transaction updated succesffully");
                    }
                    else {
                        System.out.println("Transaction update failed.");
                    }
                }
                else {
                    System.out.println("Transaction not found.");
                }
            }
                
            // if user selects D
            else if (userInput.equalsIgnoreCase("D")) {
                // prompt to enter specific transaction ID and gather input
                System.out.println("Enter the ID of the transaction you wish to delete: ");
                int transactionId = in.nextInt();
                in.nextLine();

                // communicate with transactionDAO and deleteTransaction method
                boolean deleted = transactionDAO.deleteTransaction(transactionId);
                
                
                if (deleted) {
                    // if DAO returns a true/successful deletion, print message below. 
                    System.out.println("Transaction deleted succesfully.");
                }
                else {
                    // if DAO returns a false/failed deletion, print message below.
                    System.out.println("Transaction not found or could not be deleted.");
                }
            }

            // if user selects T
            else if (userInput.equalsIgnoreCase("T")){
                // communicate with transacationDAO and getTotalBalance method (which handles calculating total balance)
                BigDecimal totalBalance = transactionDAO.getTotalBalance(user.getUserId());
                System.out.println("Your total balance is: " + totalBalance);
            }  

            // if user selects L
            else if (userInput.equalsIgnoreCase("L")) {
                // print goodbye message
                System.out.println("Okay see you!");
                logged_out = true; // set logged_out to true --> break loop.
            }

            // if user selects none of the above, their input was invalid. 
            else {
                System.out.println("Invalid input entered. Try again");
            }
        }
    }
}