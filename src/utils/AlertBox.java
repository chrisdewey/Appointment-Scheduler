package utils;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import models.Appointment;
import models.Customer;

import java.time.ZonedDateTime;
import java.util.function.Predicate;

/**
 * Contains methods used for displaying AlertBoxes to the user in different scenarios.
 * @author Christian Dewey
 */
public class AlertBox {

    /**
     * Displays a dialogue box showing the user the next appointment that starts within 15 minutes
     * of user login. If there is no appointment within 15 minutes, it displays that information to the user instead.
     * @param appointment that begins within 15 minutes of user login
     */
    public static void appointmentSoonAlert(Appointment appointment) {
        Alert alert;
        if (appointment != null) {
            alert = new Alert(Alert.AlertType.NONE,
                    "The following appointment begins soon:\n" +
                            "ID: " + appointment.getId() + "\n" +
                            "Title: " + appointment.getTitle() + "\n" +
                            "Type: " + appointment.getType() + "\n" +
                            "Contact ID: " + appointment.getContactID() + "\n" +
                            "Customer ID: " + appointment.getCustomerID(),
                    ButtonType.OK);
        } else {
            alert = new Alert(Alert.AlertType.NONE,
                    "There are no appointments starting in the next 15 minutes.",
                    ButtonType.OK);
        }
        alert.setTitle("Appointment Alert");
        alert.showAndWait();
    }

    /**
     * Displays a dialogue box to confirm if the user wants to exit the program.
     * @return true if user selects yes, false if no
     */
    public static boolean exitBox() {
        Alert alert = new Alert(Alert.AlertType.NONE,
                "Are you sure you want to exit?",
                ButtonType.YES,
                ButtonType.NO);
        alert.setTitle("Exit");

        alert.showAndWait();

        return alert.getResult() == ButtonType.YES;
    }

    /**
     * Confirms with the user if they want to delete the selected appointment/customer record.
     * @param removeOrCancel whether the message should use the wording remove or cancel
     * @param appointmentOrCustomer whether the message should use the wording appointment or customer
     * @return true if user selects yes, false if no
     */
    public static boolean deleteConfirmation(String removeOrCancel, String appointmentOrCustomer) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
                "Are you sure you want to " + removeOrCancel.toLowerCase() + " this " + appointmentOrCustomer + "?",
                ButtonType.YES,
                ButtonType.NO);
        alert.setHeaderText(removeOrCancel + " this " + appointmentOrCustomer + "?");

        alert.showAndWait();
        return alert.getResult() == ButtonType.YES;
    }

    /**
     * Confirms with  the user they want to save the appointment/customer record.
     * @param appointmentOrCustomer whether the message should use the wording appointment or customer
     * @return true if user selects yes, false if no
     */
    public static boolean saveConfirmation(String appointmentOrCustomer) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
                "Are you sure you want to save this " + appointmentOrCustomer + "?",
                ButtonType.YES,
                ButtonType.NO);
        alert.setHeaderText("Save");

        alert.showAndWait();
        return alert.getResult() == ButtonType.YES;
    }

    /**
     * Displays an error message if either a the Customer ID or User ID fields
     * are left blank/null, or if a non-number is used.
     */
    public static void numberFormatError() {
        Alert alert = new Alert(Alert.AlertType.ERROR,
                "Customer ID and User ID must be numbers.");
        alert.setTitle("Error");
        alert.setHeaderText("Cannot save appointment.");

        alert.showAndWait();
    }

    /**
     * Displays an alert box informing the user that the customer cannot be deleted until it no longer has associated
     * appointments.
     */
    public static void associatedAppointment() {
        Alert alert = new Alert(Alert.AlertType.ERROR,
                "This customer is associated with one or more appointment.\n" +
                        "These appointments must be deleted before the customer can be removed.");
        alert.setTitle("Error");
        alert.setHeaderText("Cannot delete customer.");

        alert.showAndWait();
    }

    /**
     * Displays an alert box if an appointment record has an invalid input by checking if:
     * the title, description, location, or type fields are left null;
     * the end time is before the start time;
     * the customerID, userID or ContactID do not have existing associated records.
     * @param title name of the appointment
     * @param description description of the appointment
     * @param location of the appointment
     * @param type of the appointment
     * @param startTimeZDT start time of the appointment
     * @param endTimeZDT end time of the appointment
     * @param customerID associated customer ID
     * @param userID associated user ID
     * @param contactID associated contact ID
     * @return true if the format is valid, false if it is not
     */
    public static boolean fieldCheckAppointment(String title,
                                                String description,
                                                String location,
                                                String type,
                                                ZonedDateTime startTimeZDT,
                                                ZonedDateTime endTimeZDT,
                                                int customerID,
                                                int userID,
                                                int contactID) {
        String errorMsg = "";
        long differenceOfZDTs = startTimeZDT.compareTo(endTimeZDT);
        boolean isValid = true;
        if (title == null || title.isBlank()) {
            errorMsg = "Appointment must have a title.";
            isValid = false;
        } else if (description == null || description.isBlank()) {
            errorMsg = "Appointment must have a description.";
            isValid = false;
        } else if (location == null || location.isBlank()) {
            errorMsg = "Appointment must have a location.";
            isValid = false;
        } else if (type == null || type.isBlank()) {
            errorMsg = "Appointment must have a type.";
            isValid = false;
        } else if (differenceOfZDTs > 0) {
            errorMsg = "Appointment Start Time must be before its End Time,\n" + "and within business hours.";
            isValid = false;
        } else if (DataAccessRead.getCustomerList().stream().noneMatch(i -> i.getId() == customerID)) {
            errorMsg = "You must enter a valid and existing Customer ID.";
            isValid = false;
        } else if (DataAccessRead.getUserList().stream().noneMatch(i -> i.getId() == userID)) {
            errorMsg = "You must enter a valid and existing User ID.";
            isValid = false;
        } else if (DataAccessRead.getContactList().stream().noneMatch(i -> i.getId() == contactID)) {
            errorMsg = "You must enter a valid Contact.";
            isValid = false;
        }

        if (!isValid) {
            Alert alert = new Alert(Alert.AlertType.ERROR, errorMsg);
            alert.setTitle("Error");
            alert.setHeaderText("Input Error");

            alert.showAndWait();
        }

        return isValid;
    }

    /**
     * Displays an alert box if a customer record has an invalid input by checking if:
     * the name, address, postal code, or phone fields are left null,
     * or if the country and division are not selected.
     * @param name name of the customer
     * @param address of the customer
     * @param postalCode of the customer
     * @param phone of the customer
     * @param divisionID of the customer
     * @param country of the customer
     * @return true if the format is valid, false if it is not
     */
    public static boolean fieldCheckCustomer(String name,
                                                String address,
                                                String postalCode,
                                                String phone,
                                                Integer divisionID,
                                                String country) {
        String errorMsg = "";
        boolean isValid = true;
        if (name == null || name.isBlank()) {
            errorMsg = "Customer must have a Name.";
            isValid = false;
        } else if (address == null || address.isBlank()) {
            errorMsg = "Customer must have a Address.";
            isValid = false;
        } else if (postalCode == null || postalCode.isBlank()) {
            errorMsg = "Customer must have a Postal Code.";
            isValid = false;
        } else if (phone == null || phone.isBlank()) {
            errorMsg = "Customer must have a Phone Number.";
            isValid = false;
        } else if (country == null || country.isBlank() || divisionID == null) {
            errorMsg = "You must select a valid Country and Region/Division.";
            isValid = false;
        }

        if (!isValid) {
            Alert alert = new Alert(Alert.AlertType.ERROR, errorMsg);
            alert.setTitle("Error");
            alert.setHeaderText("Input Error");

            alert.showAndWait();
        }

        return isValid;
    }

    /**
     * There are two <strong>Lambda</strong> expressions in this method used in together. First the associated list is
     * filtered by the given conditions, then loops through the list again and returns true if there are no
     * Appointments that match the given described in the 'Predicate<> condition' expressing.
     * @param appointmentId ID of the appointment. Used to exclude this appointment from 'condition' predicate
     *                      when checking for scheduled conflicts in cases of updating an appointment rather than
     *                      creating a new one.
     * @param startTimeZDT start time of the appointment to be made
     * @param endTimeZDT end time of the appointment to be made
     * @param customerID id of the customers appointment to be scheduled
     * @return true if the appointment does not conflict with an existing appointments start and end time
     * */
    public static boolean NoScheduleConflict(int appointmentId, ZonedDateTime startTimeZDT, ZonedDateTime endTimeZDT, int customerID) {
        boolean notConflicting;

        Predicate<Appointment> condition = a -> {
            ZonedDateTime start = a.getStartTime();
            ZonedDateTime end = a.getEndTime();
            int customer = a.getCustomerID();

            if(customer == customerID) {
                return (!startTimeZDT.isBefore(start) || !endTimeZDT.isBefore(start)) && (!startTimeZDT.isAfter(end) || !endTimeZDT.isAfter(end));
            } else { return false; }
        };

        notConflicting = DataAccessRead.getAppointmentList().stream()
                .filter(a -> a.getId() != appointmentId)
                .noneMatch(condition);

        if (!notConflicting) {
            Alert alert = new Alert(Alert.AlertType.ERROR,
                    "Appointment time conflicts with an existing appointment for this customer.");
            alert.setTitle("Error");
            alert.setHeaderText("Scheduling Error");

            alert.showAndWait();
        }

        return notConflicting;
    }

    /**
     * @param startTimeZDT start time of the appointment to be made
     * @param endTimeZDT end time of the appointment to be made
     * @return true if the appointment is within business hours
     * */
    public static boolean CheckBusinessHours(ZonedDateTime startTimeZDT, ZonedDateTime endTimeZDT) {
        boolean notConflicting;

        notConflicting = TimeUtility.businessHoursCheck(startTimeZDT, endTimeZDT);

        if (!notConflicting) {
            Alert alert = new Alert(Alert.AlertType.ERROR,
                    "Appointment time must be within business hours.");
            alert.setTitle("Error");
            alert.setHeaderText("Scheduling Error");

            alert.showAndWait();
        }

        return notConflicting;
    }


    /**
     * Displays an error message if there is no selected appointment or customer when there should be.
     * @param appointmentOrCustomer whether the message should use the wording appointment or customer
     */
    public static void selectionErrorTest(String appointmentOrCustomer) {
        Alert alert = new Alert(Alert.AlertType.NONE,
                "You must select a " + appointmentOrCustomer + " record to modify.",
                ButtonType.OK);
        alert.setTitle("Selection Error");
        alert.showAndWait();
    }

    /**
     * Confirms that a customer record was successfully deleted.
     * @param customer the customer being removed
     */
    public static void deleteCustomerSuccess(Customer customer) {
        Alert alert = new Alert(Alert.AlertType.NONE,
                "The customer was successfully removed.\n" +
                "Id: " + customer.getId() + "\n" +
                "Name: " + customer.getName(),
                ButtonType.OK);
        alert.setTitle("Success");
        alert.showAndWait();
    }

    /**
     * Confirms that an appointment was successfully deleted.
     * @param appointment the appointment being removed
     */
    public static void deleteAppointmentSuccess(Appointment appointment) {
        Alert alert = new Alert(Alert.AlertType.NONE,
                "The appointment was successfully canceled.\n" +
                "Id: " + appointment.getId() + "\n" +
                "Type: " + appointment.getType(),
                ButtonType.OK);
        alert.setTitle("Success");
        alert.showAndWait();
    }
}