package main.java.com.ardiuser.finance.database;

import main.java.com.ardiuser.finance.model.User;
import java.sql.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class UserDAO {
    // Method to add a new user to the database
    public boolean addUser(User user) {
        String sql = "INSERT INTO users (username, password, email) VALUES (?, ?, ?)";
        
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            
            statement.setString(1, user.getUsername());
            statement.setString(2, user.getPassword());
            statement.setString(3, user.getEmail());

            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0;  // Returns true if the user was added successfully

        } catch (SQLException e) {
            System.out.println("Error adding user: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // Method to retrieve a user by their ID
    public User getUserById(int userId) {
        String sql = "SELECT * FROM users WHERE user_id = ?";
        
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            
            statement.setInt(1, userId);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                String username = resultSet.getString("username");
                String password = resultSet.getString("password");
                String email = resultSet.getString("email");

                // Return a new User object with the data
                return new User(userId, username, password, email);
            } else {
                System.out.println("User not found.");
                return null;  // User not found
            }

        } catch (SQLException e) {
            System.out.println("Error retrieving user: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    // Method to delete a user by their ID
    public boolean deleteUser(int userId) {
        String sql = "DELETE FROM users WHERE user_id = ?";
        
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            
            statement.setInt(1, userId);

            int rowsAffected = statement.executeUpdate();
            
            return rowsAffected > 0;  // Returns true if the deletion was successful

        } catch (SQLException e) {
            System.out.println("Error deleting user: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    public User loginUser(String username, String password) {
        String sql = "SELECT * FROM users WHERE username = ? AND password = ?"; // Password check, but use hashed passwords in production
        User user = null;

        try (Connection connection = DBConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)) {

            // Set parameters for the query (username and password)
            statement.setString(1, username);
            statement.setString(2, password);

             // Execute the query
            ResultSet resultSet = statement.executeQuery();

            // If the user is found, populate the user object
            if (resultSet.next()) {
                int userId = resultSet.getInt("user_id");
                String userEmail = resultSet.getString("email");
                user = new User(userId, username, password, userEmail);  // Returning the user object (password is not hashed here for simplicity)
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

         return user;
    }
    
}
