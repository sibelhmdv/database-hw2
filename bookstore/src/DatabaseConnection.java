import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;

public class DatabaseConnection {
    public static void main(String[] args) {
        // Database connection parameters
        String url = "jdbc:postgresql://localhost:5432/postgresAS2";
        String user = "postgres";
        String password = "sib75";

        try {
            // Load the PostgreSQL JDBC driver
            Class.forName("org.postgresql.Driver");

            // Establish the database connection
            Connection connection = DriverManager.getConnection(url, user, password);


            //insertOperationAuthors(connection, 1); //no duplicate should have here

            //insertOperationBooks(connection, 1); //no duplicate should have here
            
            //deleteOperationBooks(connection, 1); 

            //deleteOperationAuthors(connection, 1);
            

            if (connection != null && !connection.isClosed()) {
                System.out.println("Connected to the database!");
            } else {
                System.out.println("Failed to connect to the database.");
            }

            // Close the connection when done
            connection.close();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            System.err.println("Exception" + e.getMessage());
        }
    }

    
    private static void insertOperationBooks(Connection connection,int authorid) {
        try {

            if (!authorExists(connection, authorid)) {
                System.out.println("Author with ID " + authorid + " does not exist. Please insert the author first.");
                return;
            }


            // Insert a new book
            String insertQuery = "INSERT INTO books (bookid, title, genre, authorid, stockquantity, price) " + 
            "VALUES (?,?,?,?,?,?)";

            try (PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
                preparedStatement.setInt(1, 1);
                preparedStatement.setString(2, "Beauty and the Beast"); 
                preparedStatement.setString(3, "Romance");
                preparedStatement.setInt(4, authorid); 
                preparedStatement.setInt(5, 3);
                preparedStatement.setDouble(6, 20.2);

                int rowsInserted = preparedStatement.executeUpdate();
                System.out.println(rowsInserted + " row(s) inserted in Books Table.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    private static void deleteOperationBooks(Connection connection, int authorid) {
        try {
            // Delete a book by bookid
            String deleteQuery = "DELETE FROM books WHERE authorid = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(deleteQuery)) {
                preparedStatement.setInt(1, authorid); // assigning value manually for now

                int rowsAffected = preparedStatement.executeUpdate();
                System.out.println(rowsAffected + " row(s)" + "with the Author ID of " + authorid + " deleted from Books.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void insertOperationAuthors(Connection connection, int authorid) {
        try {


            if(authorExists(connection, 0)) {
                System.out.println("Author with ID " + authorid + " already exists.");
                return;
            }

            // Insert new Authors
            String insertQuery = "INSERT INTO authors (authorid, firstname, lastname) " + 
            "VALUES (?,?,?)";

            try (PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
                preparedStatement.setInt(1, authorid);
                preparedStatement.setString(2, "Gabrielle-Suzanne"); 
                preparedStatement.setString(3, "Barbot de Villeneuve");

                int rowsInserted = preparedStatement.executeUpdate();
                System.out.println(rowsInserted + " row(s) inserted in Authors Table.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static boolean authorExists(Connection connection, int authorid) throws SQLException {
        // Check if the author with the given ID already exists
        String checkAuthorQuery = "SELECT COUNT(*) FROM authors WHERE authorid = ?";
        try (PreparedStatement checkAuthorStatement = connection.prepareStatement(checkAuthorQuery)) {
            checkAuthorStatement.setInt(1, authorid);
            try (ResultSet resultSet = checkAuthorStatement.executeQuery()) {
                if (resultSet.next()) {
                    int count = resultSet.getInt(1);
                    return count > 0;
                }
            }
        }
        return false;
    }

    private static void deleteOperationAuthors(Connection connection, int authorid) {
        try {

            // Delete related book first
            deleteOperationBooks(connection, authorid);

            String deleteQuery = "DELETE FROM authors WHERE authorid = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(deleteQuery)) {
                preparedStatement.setInt(1, authorid); // assigning value manually for now

                int rowsAffected = preparedStatement.executeUpdate();
                System.out.println(rowsAffected + " row(s) deleted from Authors.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
