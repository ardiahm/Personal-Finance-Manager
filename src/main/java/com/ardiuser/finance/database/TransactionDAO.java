package main.java.com.ardiuser.finance.database;

import main.java.com.ardiuser.finance.model.Transaction;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class TransactionDAO {
    
    public void addTransaction(Transaction transaction) throws SQLException {
        String sql = "INSERT INTO transactions (amount, description, date, category, user_id) VALUES (?, ?, ?, ?, ?)";

        try (Connection connection = DBConnection.getConnection(); 
        PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setBigDecimal(1, transaction.getAmount());
            statement.setString(2, transaction.getDescription());
            statement.setDate(3, new java.sql.Date(transaction.getDate().getTime()));  // Convert java.util.Date to java.sql.Date
            statement.setString(4, transaction.getCategory());
            statement.setInt(5, transaction.getUserId());
            
            // Execute the statement
            statement.executeUpdate();
        }
    }

    public boolean updateTransaction(Transaction transaction) throws SQLException {
        String sql = "UPDATE transactions SET amount = ?, description = ?, date = ?, category = ? WHERE id = ?";
        
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            
                connection.setAutoCommit(true);

            // Set the parameters for the PreparedStatement
            statement.setBigDecimal(1, transaction.getAmount());
            statement.setString(2, transaction.getDescription());
            statement.setDate(3, new java.sql.Date(transaction.getDate().getTime()));
            statement.setString(4, transaction.getCategory());
            statement.setInt(5, transaction.getId());
            
            // Execute the update statement
            int rowsAffected = statement.executeUpdate();
            System.out.println("Rows affected: " + rowsAffected);  // Debugging line
            return rowsAffected > 0;
        }
    }

    public boolean deleteTransaction(int transactionId) throws SQLException {
        String sql = "DELETE FROM transactions WHERE id = ?";
        
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            
            // specifically select the inputted transaction ID
            statement.setInt(1, transactionId);
            int rowsAffected = statement.executeUpdate();
            
            return rowsAffected > 0; // returns true if deletion is successful (> 1)
        }
    }

    public Transaction getTransactionById(int id) throws SQLException {
        String sql = "SELECT * FROM transactions WHERE id = ?";
        Transaction transaction = null;
        
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            
            // Set the transaction ID as a parameter
            statement.setInt(1, id);
            
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    BigDecimal amount = resultSet.getBigDecimal("amount");
                    String description = resultSet.getString("description");
                    Date date = resultSet.getDate("date");
                    String category = resultSet.getString("category");
                    int userId = resultSet.getInt("user_id");
                    // Create a new Transaction object
                    transaction = new Transaction(id, amount, description, date, category, userId);
                }
            }
        }
            return transaction;
    }

    public List<Transaction> getAllTransactions(int userId) throws SQLException {
        List<Transaction> transactions = new ArrayList<>(); // Initialize the list to hold the transactions
        String sql = "SELECT * FROM transactions WHERE user_id = ?";
    
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
    
            // Set the userId parameter to filter transactions by user
            statement.setInt(1, userId);
    
            try (ResultSet resultSet = statement.executeQuery()) {
                // Loop through the result set and create Transaction objects
                while (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    BigDecimal amount = resultSet.getBigDecimal("amount");
                    String description = resultSet.getString("description");
                    Date date = resultSet.getDate("date");
                    String category = resultSet.getString("category");
    
                    // Create a new Transaction object and add it to the list
                    Transaction transaction = new Transaction(id, amount, description, date, category, userId);
                    transactions.add(transaction);
                }
            }
        }
        return transactions; // Return the populated list of transactions
    }
    

    public BigDecimal getTotalBalance(int userId) throws SQLException {
        BigDecimal totalBalance = BigDecimal.ZERO;
        String sql = "SELECT SUM(amount) FROM transactions WHERE user_id = ? ";
    
        try (Connection conn = DBConnection.getConnection();
        PreparedStatement statement = conn.prepareStatement(sql)){
            statement.setInt(1, userId);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                totalBalance = resultSet.getBigDecimal(1);
                if (totalBalance == null) {
                    totalBalance = BigDecimal.ZERO;
                }
                }}
        catch (SQLException e) {
                System.out.println("Error retrieving total balance: " + e.getMessage());
                }
        return totalBalance;
    }

}
