package utils;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Contains the PreparedStatement methods for interacting with the Database.
 * @author Christian Dewey
 */
public class DBQuery {

    // statement reference
    private static PreparedStatement statement;

    /**
     * Creates Prepared Statement Object
     * @param connection the connection to the Database
     * @param sqlStatement the SQL PreparedStatement
     */
    public static void setPreparedStatement(Connection connection, String sqlStatement) throws SQLException {
        statement = connection.prepareStatement(sqlStatement);
    }

    /**
     * Returns the Prepared Statement Object to be used with .execute() and interact with the Database
     * @return statement the Prepared Statement Object
     */
    public static PreparedStatement getPreparedStatement() {
        return statement;
    }
}