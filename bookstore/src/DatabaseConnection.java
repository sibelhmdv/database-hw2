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

            if (connection != null && !connection.isClosed()) {
                System.out.println("Connected to the database!");
            } else {
                System.out.println("Failed to connect to the database.");
            }


            Scanner scanner = new Scanner(System.in);
            System.out.print("Enter author ID: ");
            int authorid = scanner.nextInt();

            //insertOperationAuthors(connection, authorid); 

            //insertOperationBooks(connection, authorid); 
            
            //deleteOperationBooks(connection, authorid); 

            //deleteOperationAuthors(connection, authorid);
            
            //readAuthors(connection);
            
            //readBooks(connection, authorid); //check after fixing inserting book method

            
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

            
            Scanner scanner = new Scanner(System.in);
            System.out.print("Enter Book ID: ");
            int bookid = scanner.nextInt();

            // Check if the book with the provided id already exists
            if (bookExists(connection, bookid)) {
            System.out.println("Book with ID " + bookid + " already exists. Please choose a different Book ID.");
            return;
            }

            System.out.print("Enter Book Title: ");
            String title = scanner.next(); //nextline was returning empty line


            scanner.nextLine(); //prevention of empty string for the next line

            System.out.print("Enter Book's Stock Quantity: ");
            int quantity = scanner.nextInt();

            System.out.print("Enter Book's Genre: ");
            String genre = scanner.next();

            scanner.nextLine();

            System.out.print("Enter Book's Price (in 10.2 format): ");
            double price = scanner.nextDouble();


            // Insert a new book
            String insertQuery = "INSERT INTO books (bookid, title, genre, authorid, stockquantity, price) " + 
            "VALUES (?,?,?,?,?,?)";

            try (PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
                preparedStatement.setInt(1, bookid);
                preparedStatement.setString(2, title); 
                preparedStatement.setString(3, genre);
                preparedStatement.setInt(4, authorid); 
                preparedStatement.setInt(5, quantity);
                preparedStatement.setDouble(6, price);

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

            Scanner scanner = new Scanner(System.in);
            System.out.print("Enter Author Name: ");
            String firstname = scanner.nextLine();

            System.out.print("Enter Author's Lastname: ");
            String lastname = scanner.nextLine();

            // Insert new Author
            String insertQuery = "INSERT INTO authors (authorid, firstname, lastname) " + 
            "VALUES (?,?,?)";

            try (PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
                preparedStatement.setInt(1, authorid);
                preparedStatement.setString(2, firstname); 
                preparedStatement.setString(3, lastname);

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

    private static void readAuthors(Connection connection) {
        try {
            // Select and show all Authors
            String selectQuery = "SELECT * FROM authors";
            try (PreparedStatement preparedStatement = connection.prepareStatement(selectQuery);
                 ResultSet resultSet = preparedStatement.executeQuery()) {

                while (resultSet.next()) {
                    int authorId = resultSet.getInt("authorid");
                    String firstname = resultSet.getString("firstname");
                    String lastname = resultSet.getString("lastname");

                    System.out.println("Author ID: " + authorId + " | FirstName: " + firstname +
                             " | LastName: " + lastname);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void readBooks(Connection connection, int authorid) {
        try {
            // Select and show all books
            String selectQuery = "SELECT * FROM books";
            try (PreparedStatement preparedStatement = connection.prepareStatement(selectQuery);
                 ResultSet resultSet = preparedStatement.executeQuery()) {

                
                if(!resultSet.isBeforeFirst()) {
                    System.out.println("There is no any book in this database!");
                }else {
                    while (resultSet.next()) {
                    int bookid = resultSet.getInt("bookid");
                    String title = resultSet.getString("title");
                    String genre = resultSet.getString("genre");
                    authorid = resultSet.getInt("authorid");
                    int stockquantity = resultSet.getInt("stockquantity");
                    double price = resultSet.getDouble("price");
                    
                    System.out.println("Book ID: " + bookid + " | Title: " + title + " | Genre: " + genre +
                            " | Author ID: " + authorid + " | Stock Quantity: " + stockquantity + " | Price: " + price);
                    }
                }    
                
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static boolean bookExists(Connection connection, int bookid) throws SQLException {
        // Check if the bookid already exists
        String checkBookQuery = "SELECT COUNT(*) FROM books WHERE bookid = ?";
        try (PreparedStatement checkBookStatement = connection.prepareStatement(checkBookQuery)) {
            checkBookStatement.setInt(1, bookid);
            try (ResultSet resultSet = checkBookStatement.executeQuery()) {
                if (resultSet.next()) {
                    int count = resultSet.getInt(1);
                    return count > 0;
                }
            }
        }
        return false;
    }

}
