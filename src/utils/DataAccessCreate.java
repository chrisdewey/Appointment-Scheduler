package utils;

import models.Appointment;
import models.Customer;

import java.sql.*;

/**
 * Contains the methods for Creating new Appointments and Customers and adding them to the Database.
 * @author Christian Dewey
 */
public class DataAccessCreate {

    /**
     * Concatenates a SQL INSERT statement to insert the new appointment record into the MySQL Database.
     * @param a the newly created appointment to insert into the database
     */
    public static void createNewAppointment(Appointment a) {
        try {
            Connection connection = DBConnection.getConnection();
            String insertStatement = "INSERT INTO appointments (Title, Description, Location, Type, Start, End," +
                    " Created_By, Last_Update, Last_Updated_By, Customer_ID, User_ID, Contact_ID) " + //Create_Date, taken out from start of line.
                    "VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";

            DBQuery.setPreparedStatement(connection, insertStatement);
            PreparedStatement ps = DBQuery.getPreparedStatement();

            Timestamp startTime = TimeUtility.ConvertToUTCTimestamp(a.getStartTime());
            Timestamp endTime = TimeUtility.ConvertToUTCTimestamp(a.getEndTime());

            ps.setString(1, a.getTitle());
            ps.setString(2, a.getDescription());
            ps.setString(3, a.getLocation());
            ps.setString(4, a.getType());
            ps.setTimestamp(5, startTime);
            ps.setTimestamp(6, endTime);
            ps.setString(7, a.getCreatedBy());
            ps.setTimestamp(8, a.getLastUpdate());
            ps.setString(9, a.getLastUpdatedBy());
            ps.setString(10, Integer.toString(a.getCustomerID()));
            ps.setString(11, Integer.toString(a.getUserID()));
            ps.setString(12, Integer.toString(a.getContactID()));

            ps.execute();

            //confirm rows affected
            if(ps.getUpdateCount() > 0) {
                System.out.println(ps.getUpdateCount() + " rows affected.");
            } else {
                System.out.println("No Change!");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Concatenates a SQL INSERT statement to insert the new customer record into the MySQL Database.
     * @param c the newly created customer record to insert into the database
     */
    public static void createNewCustomer(Customer c) {
        try {
            Connection connection = DBConnection.getConnection();
            String insertStatement = "INSERT INTO customers (Customer_Name, Address, Postal_Code, Phone," +
                    " Created_By, Last_Update, Last_Updated_By, Division_ID) " + //Create_Date, taken out from start of line.
                    "VALUES (?,?,?,?,?,?,?,?)";

            DBQuery.setPreparedStatement(connection, insertStatement);
            PreparedStatement ps = DBQuery.getPreparedStatement();

            ps.setString(1, c.getName());
            ps.setString(2, c.getAddress());
            ps.setString(3, c.getPostalCode());
            ps.setString(4, c.getPhone());
            ps.setString(5, c.getCreatedBy());
            ps.setTimestamp(6, c.getLastUpdate());
            ps.setString(7, c.getLastUpdatedBy());
            ps.setString(8, Integer.toString(c.getFirstLvlDivisionID()));

            ps.execute();

            //confirm rows affected
            if(ps.getUpdateCount() > 0) {
                System.out.println(ps.getUpdateCount() + " rows affected.");
            } else {
                System.out.println("No Change!");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}