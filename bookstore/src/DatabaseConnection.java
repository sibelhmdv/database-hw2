import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

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

            insertOperationAuthors(connection);
            insertOperationBooks(connection);

            if (connection != null && !connection.isClosed()) {
                System.out.println("Connected to the database!");
            } else {
                System.out.println("Failed to connect to the database.");
            }
            // Now you can perform database operations using the 'connection' object

            // Close the connection when done
            connection.close();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            System.err.println("Exception" + e.getMessage());
        }
    }

    
    private static void insertOperationBooks(Connection connection) {
        try {
            // Insert a new book
            String insertQuery = "INSERT INTO books (bookid, title, genre, authorid, stockquantity, price) " + 
            "VALUES (?,?,?,?,?,?)";

            try (PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
                preparedStatement.setInt(1, 1);
                preparedStatement.setString(2, "Beauty and the Beast"); 
                preparedStatement.setString(3, "Romance");
                preparedStatement.setInt(4, 1); 
                preparedStatement.setInt(5, 3);
                preparedStatement.setDouble(6, 20.2);

                int rowsInserted = preparedStatement.executeUpdate();
                System.out.println(rowsInserted + " row(s) inserted.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void insertOperationAuthors(Connection connection) {
        try {
            // Insert a new book
            String insertQuery = "INSERT INTO authors (authorid, firstname, lastname) " + 
            "VALUES (?,?,?)";

            try (PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
                preparedStatement.setInt(1, 1);
                preparedStatement.setString(2, "Gabrielle-Suzanne"); 
                preparedStatement.setString(3, "Barbot de Villeneuve");

                int rowsInserted = preparedStatement.executeUpdate();
                System.out.println(rowsInserted + " row(s) inserted.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}
