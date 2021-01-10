package utils;

import models.Appointment;
import models.Customer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;

/**
 * Contains the methods for updating rows in the Database.
 * @author Christian Dewey
 */
public class DataAccessUpdate {

    /**
     * Using PreparedStatement, an UPDATE Statement is sent to the Database to update any information about the
     *  appointment the user changes, except the Appointment ID. The appointment is then removed from the
     *  ObservableList 'appointmentList'
     * @param a the appointment to be updated
     */
    public static void updateAppointment(Appointment a) {
        try {
            Connection connection = DBConnection.getConnection();
            String insertStatement = "UPDATE appointments SET Title=?, Description=?, Location=?, Type=?, Start=?," +
                    "End=?, Last_Update=?, Last_Updated_By=?, Customer_ID=?, User_ID=?, Contact_ID=?" +
                    "WHERE Appointment_ID=?";

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
            ps.setTimestamp(7, a.getLastUpdate());
            ps.setString(8, a.getLastUpdatedBy());
            ps.setString(9, Integer.toString(a.getCustomerID()));
            ps.setString(10, Integer.toString(a.getUserID()));
            ps.setString(11, Integer.toString(a.getContactID()));

            ps.setString(12, Integer.toString(a.getId()));

            ps.execute();

            //confirm rows affected
            if(ps.getUpdateCount() > 0) {
                System.out.println(ps.getUpdateCount() + " rows affected.");
                DataAccessRead.updateAppointment(a);
            } else {
                System.out.println("No Change!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Using PreparedStatement, an UPDATE Statement is sent to the Database to update any information about the
     *  customer record the user changes, except the Customer ID. The customer is then removed from the
     *  ObservableList 'customerList'
     * @param c the customer record to be updated
     */
    public static void updateCustomer(Customer c) {
        try {
            Connection connection = DBConnection.getConnection();
            String insertStatement = "UPDATE customers SET Customer_Name=?, Address=?, Postal_Code=?, Phone=?, " +
                    "Last_Update=?, Last_Updated_By=?, Division_ID=?" +
                    "WHERE Customer_ID=?";

            DBQuery.setPreparedStatement(connection, insertStatement);
            PreparedStatement ps = DBQuery.getPreparedStatement();


            ps.setString(1, c.getName());
            ps.setString(2, c.getAddress());
            ps.setString(3, c.getPostalCode());
            ps.setString(4, c.getPhone());
            ps.setTimestamp(5, c.getLastUpdate());
            ps.setString(6, c.getLastUpdatedBy());
            ps.setString(7, Integer.toString(c.getFirstLvlDivisionID()));

            ps.setString(8, Integer.toString(c.getId()));

            ps.execute();

            //confirm rows affected
            if(ps.getUpdateCount() > 0) {
                System.out.println(ps.getUpdateCount() + " rows affected.");
                DataAccessRead.updateCustomer(c);
            } else {
                System.out.println("No Change!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}