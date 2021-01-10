package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Contains the methods for connecting to the Database.
 * @author Christian Dewey
 */
public class DBConnection {

    //JDBC url parts
    private static final String protocol = "jdbc:";
    private static final String vendorName = "mysql:";
    private static final String ipAddress = ""; //TODO input IP Address for the Database here

    //JDBC url concatenation
    private static final String jdbcURL = protocol + vendorName + ipAddress;

    //Driver interface reference
    private static final String MYSQLJDBCDriver = "com.mysql.cj.jdbc.Driver";

    //Database connection username and password //TODO input Database Username and Password here for connection
    private static final String username = "";
    private static final String password = "";

    private static Connection connection = null;

    /**
     * Connects to the MySQL Database and stores the connection to be accessed by the getConnection() method
     */
    public static void startConnection() {
        try {
            Class.forName(MYSQLJDBCDriver);
            connection = DriverManager.getConnection(jdbcURL, username, password);
            System.out.println("Connection Successful.");
        }
        catch(ClassNotFoundException e) {
            System.out.println("ClassNotFoundException Error: " + e.getMessage());
        }
        catch(SQLException e) {
            System.out.println("SQLException Error: " + e.getMessage());
        }
    }

    /**
     * returns the connection to the database, which has already been opened by startConnection()
     * @return connection the connection to the Database
     */
    public static Connection getConnection() {
        return connection;
    }

    /**
     * Closes the connection to the Database
     */
    public static void closeConnection() {
        try {
            connection.close();
            System.out.println("Connection Closed.");
        }
        catch (SQLException e) {
            System.out.println("SQLException Error: " + e.getMessage());
        }
    }
}