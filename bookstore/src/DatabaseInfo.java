import java.sql.*;

public class DatabaseInfo {

    public static void main(String[] args) {
        String jdbcUrl = "jdbc:postgresql://localhost:5432/postgresAS2";
        String username = "postgres";
        String password = "sib75";

        try (Connection connection = DriverManager.getConnection(jdbcUrl, username, password)) {
            showTablesAndStructures(connection);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Display names and structures of all tables in the database
    private static void showTablesAndStructures(Connection connection) throws SQLException {

        DatabaseMetaData metaData = connection.getMetaData();
        ResultSet resultSet = metaData.getTables(null, null, "%", new String[]{"TABLE"});

        System.out.println("-------------------------------------");
        System.out.println("| Names and Structures of the Table |");
        System.out.println("-------------------------------------");

        while (resultSet.next()) {
            String table_name = resultSet.getString("TABLE_NAME");
            System.out.println("Table: " + table_name.toUpperCase());

            // Show columns for each table
            showColumns(connection, table_name);

            // Show primary key information
            showPK(connection, table_name);

            // Show foreign key information
            showFK(connection, table_name);

            System.out.println();
        }
    }

    // Show column details (type) of a table
    private static void showColumns(Connection connection, String table_name) throws SQLException {
        DatabaseMetaData metaData = connection.getMetaData();
        ResultSet resultSet = metaData.getColumns(null, null, table_name, null);

        System.out.println("------------");
        System.out.println("| Columns: |");
        System.out.println("------------");


        while (resultSet.next()) {
            String columnName = resultSet.getString("COLUMN_NAME");
            String dataType = resultSet.getString("TYPE_NAME");

            System.out.println(" - " + columnName + " (" + dataType +  ")");
        }
    }

    // Show primary key(s) of a table
    private static void showPK(Connection connection, String table_name) throws SQLException {
        DatabaseMetaData metaData = connection.getMetaData();
        ResultSet resultSet = metaData.getPrimaryKeys(null, null, table_name);

        System.out.println("Primary Key:");

        while (resultSet.next()) {
            String columnsOfPK = resultSet.getString("COLUMN_NAME");
            System.out.println(" - " + columnsOfPK);
        }
    }

    // Show foreign key(s) of a table
    private static void showFK(Connection connection, String table_name) throws SQLException {
        DatabaseMetaData metaData = connection.getMetaData();
        ResultSet resultSet = metaData.getImportedKeys(null, null, table_name);

        System.out.println("Foreign Keys:");

        while (resultSet.next()) {
            String fkName = resultSet.getString("FK_NAME");
            String fkColumnName = resultSet.getString("FKCOLUMN_NAME");
            String pkTableName = resultSet.getString("PKTABLE_NAME");
            String pkColumnName = resultSet.getString("PKCOLUMN_NAME");

            if((fkName == null) && (fkColumnName == null)){
                System.out.println("No Foreign Key for this Table");
            }else{
                System.out.println(" - " + fkName + ": " + fkColumnName +
                    " References " + pkTableName + "." + pkColumnName);
            }

        }
    }


}
