package utils;

import models.Appointment;
import models.Customer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Contains the methods for Deleting the Customer or Appointment from the Database.
 * @author Christian Dewey
 */
public class DataAccessDelete {

    /**
     * Sends a DELETE statement to the MySQL Database to remove the selected appointment data, then
     *  passes the appointment to DataAccessRead.removeAppointmentFromList() to be removed from the Observable List.
     * @param a the appointment to be deleted
     */
    public static void deleteAppointment(Appointment a) {
        if (AlertBox.deleteConfirmation("Cancel", "Appointment")) {
            try {
                Connection connection = DBConnection.getConnection();
                String deleteStatement = "DELETE FROM appointments WHERE Appointment_ID = ?";

                DBQuery.setPreparedStatement(connection, deleteStatement);
                PreparedStatement ps = DBQuery.getPreparedStatement();

                ps.setString(1, String.valueOf(a.getId()));

                ps.execute();

                //confirm rows affected
                if (ps.getUpdateCount() > 0) {
                    System.out.println(ps.getUpdateCount() + " rows affected.");
                    DataAccessRead.removeAppointmentFromList(a);
                    AlertBox.deleteAppointmentSuccess(a);
                } else {
                    System.out.println("No Change!");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * @return whether the customer is associated with any existing appointments.
     * @param c the customer record to check
     */
    private static boolean checkAppointments(Customer c) {

        boolean foundInList = DataAccessRead.getAppointmentList().stream().anyMatch(a -> a.getCustomerID() == c.getId());

        if (foundInList) {
            AlertBox.associatedAppointment();
            return false;
        } else return true;
    }

    /**
     * If the customer is not associated with any existing appointments, then sends a DELETE statement to the MySQL
     * Database to remove the selected customer data, then passes the customer to
     * DataAccessRead.removeCustomerFromList() to be removed from the associated Observable List.
     * @param c the appointment to be deleted
     */
    public static void deleteCustomer(Customer c) {
        if (AlertBox.deleteConfirmation("Remove", "Customer") && checkAppointments(c)) {
            try {
                Connection connection = DBConnection.getConnection();
                String deleteStatement = "DELETE FROM customers WHERE Customer_ID = ?";

                DBQuery.setPreparedStatement(connection, deleteStatement);
                PreparedStatement ps = DBQuery.getPreparedStatement();

                ps.setString(1, String.valueOf(c.getId()));

                ps.execute();

                //confirm rows affected
                if (ps.getUpdateCount() > 0) {
                    System.out.println(ps.getUpdateCount() + " rows affected.");
                    DataAccessRead.removeCustomerFromList(c);
                    AlertBox.deleteCustomerSuccess(c);
                } else {
                    System.out.println("No Change!");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

}