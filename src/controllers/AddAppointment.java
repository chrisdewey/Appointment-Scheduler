package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.util.Callback;
import models.Appointment;
import models.Contact;
import utils.*;

import java.net.URL;
import java.sql.Timestamp;
import java.time.*;
import java.util.ResourceBundle;

import static java.time.LocalTime.now;

/**
 * Controller class for the Add Appointment window add_appointment_view.fxml.
 * Allows the user to create a new Appointment and add it to the Database, or update an existing one.
 * @author Christian Dewey
 */
public class AddAppointment implements Initializable {

    @FXML private Spinner<LocalTime> startTimeSpinner;
    @FXML private Spinner<LocalTime> endTimeSpinner;
    @FXML private ComboBox<Contact>  contactComboBox;
    @FXML private TextField appointmentIDField;
    @FXML private TextField titleField;
    @FXML private TextArea descriptionField;
    @FXML private TextField typeField;
    @FXML private TextField locationField;
    @FXML private TextField customerIDField;
    @FXML private TextField userIDField;
    @FXML private DatePicker datePicker;
    @FXML private Label localTimeLabel;

    private boolean isNewAppointment;
    private Appointment appointmentToUpdate;
    private String sessionUsername;

    /**
     * Callback method used to populate the contactComboBox with all Contacts, displaying their name.
     * When a Contact is selected from the ComboBox, the Contact is returned, rather than just the name,
     * allowing convenient access to the Contact object for use in other methods.
     */
    private final Callback<ListView<Contact>, ListCell<Contact>> comboBoxFactory = lv -> new ListCell<>() {
        @Override
        protected void updateItem(Contact item, boolean empty) {
            super.updateItem(item, empty);
            setText(empty ? "" : item.getName());
        }
    };

    /**
     * Exits the window.
     */
    @FXML
    public void cancelButton(ActionEvent actionEvent) {
        ((Node)(actionEvent.getSource())).getScene().getWindow().hide();
    }

    /**
     * When the Save Button is pressed, a new Appointment object is created and added to the appointmentList as well as
     * the Database. If 'isNewAppointment' is false, then the existing Appointment object is updated with the new
     * information instead of creating a new Appointment.
     */
    @FXML
    public void saveButton(ActionEvent actionEvent) {
        try {
            if (AlertBox.saveConfirmation("Appointment")) {

                int id = Integer.parseInt(appointmentIDField.getText());
                String title = titleField.getText();
                String description = descriptionField.getText();
                String location = locationField.getText();
                String type = typeField.getText();

                LocalTime startTime = LocalTime.parse(startTimeSpinner.getValue().toString());

                LocalDateTime startTimeLDT = LocalDateTime.of(datePicker.getValue(), startTime);
                ZonedDateTime startTimeZDT = ZonedDateTime.of(startTimeLDT, ZoneId.systemDefault());

                LocalTime endTime = LocalTime.parse(endTimeSpinner.getValue().toString());
                LocalDateTime endTimeLDT = LocalDateTime.of(datePicker.getValue(), endTime);

                /*
                If the Business Hours in LocalTime pass Midnight; and Midnight is between startTime and endTime,
                then adds a Day to the endTime LDT.
                */
                TimeUtility.businessHoursOverMidnight();
                if (TimeUtility.businessHoursOverMidnight() != null && endTime.isBefore(TimeUtility.businessHoursOverMidnight())) {
                    endTimeLDT = LocalDateTime.of(datePicker.getValue().plusDays(1), endTime);
                }

                ZonedDateTime endTimeZDT = ZonedDateTime.of(endTimeLDT, ZoneId.systemDefault());

                int customerID = Integer.parseInt(customerIDField.getText());
                int userID = Integer.parseInt(userIDField.getText());
                int contactID = 0;

                if (contactComboBox.getSelectionModel().getSelectedItem() != null) {
                    contactID = contactComboBox.getValue().getId();
                }

                if (AlertBox.fieldCheckAppointment(title, description, location, type, startTimeZDT, endTimeZDT, customerID, userID, contactID)
                        && AlertBox.NoScheduleConflict(id, startTimeZDT, endTimeZDT, customerID)
                        && AlertBox.CheckBusinessHours(startTimeZDT, endTimeZDT)) {

                    // if this is a NEW appointment, else it's an UPDATE statement.
                    if (isNewAppointment) {

                        Appointment newAppointment = new Appointment(
                                id,
                                title,
                                description,
                                location,
                                type,
                                startTimeZDT,
                                endTimeZDT,
                                LocalDateTime.now(),
                                sessionUsername,
                                Timestamp.valueOf(LocalDateTime.now()),
                                sessionUsername,
                                customerID,
                                userID,
                                contactID
                        );

                        DataAccessCreate.createNewAppointment(newAppointment);
                        DataAccessRead.addToAppointmentList(newAppointment);

                    } else {


                        appointmentToUpdate.setTitle(title);
                        appointmentToUpdate.setDescription(description);
                        appointmentToUpdate.setLocation(location);
                        appointmentToUpdate.setType(type);
                        appointmentToUpdate.setStartTime(startTimeZDT);
                        appointmentToUpdate.setEndTime(endTimeZDT);
                        appointmentToUpdate.setLastUpdate(Timestamp.valueOf(LocalDateTime.now()));
                        appointmentToUpdate.setContactID(contactID);
                        appointmentToUpdate.setCustomerID(customerID);
                        appointmentToUpdate.setUserID(userID);

                        DataAccessUpdate.updateAppointment(appointmentToUpdate);
                    }
                    ((Node) (actionEvent.getSource())).getScene().getWindow().hide();
                }
            }
        } catch (NumberFormatException e) { AlertBox.numberFormatError(); }
    }

    /**
     * Initializes the window and sets up the contactComboBox, the TimeSpinners and sets the localTimeLabel with
     * the business hours converted to LocalTime.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        startTimeSpinner.setValueFactory(TimeUtility.TimePicker(now()));
        startTimeSpinner.setEditable(true);

        endTimeSpinner.setValueFactory(TimeUtility.TimePicker(now()));
        endTimeSpinner.setEditable(true);

        localTimeLabel.setText(TimeUtility.businessHoursString());

        contactComboBox.setItems(DataAccessRead.getContactList());
        contactComboBox.setCellFactory(comboBoxFactory);
        contactComboBox.setButtonCell(comboBoxFactory.call(null));
    }

    /**
     * Used to decide whether the user is creating a new Appointment or updating an existing one. If it is an
     * existing Appointment to be updated, the text fields are populated with the Appointment information.
     * @param isNewAppointment Whether the user is creating a new Appointment, or updating an existing one.
     * @param appointmentToUpdate The Appointment object to be updated. If creating a new Appointment set this as null.
     * @param sessionUsername The username of the User updating/creating the Appointment.
     */
    public void initData(Boolean isNewAppointment, Appointment appointmentToUpdate, String sessionUsername) {
        this.isNewAppointment = isNewAppointment;
        this.appointmentToUpdate = appointmentToUpdate;
        this.sessionUsername = sessionUsername;

        // Load Data into fields if it's an update, else populate ID field only for new appointments.
        if(!isNewAppointment) {

            appointmentIDField.setText(Integer.toString(appointmentToUpdate.getId()));
            titleField.setText(appointmentToUpdate.getTitle());
            descriptionField.setText(appointmentToUpdate.getDescription());
            typeField.setText(appointmentToUpdate.getType());
            locationField.setText(appointmentToUpdate.getLocation());

            //contact
            contactComboBox.setValue(DataAccessRead.getContactList().stream()
                    .filter(Contact -> Contact.getId() == appointmentToUpdate.getContactID())
                    .findFirst().orElse(null));

            customerIDField.setText(Integer.toString(appointmentToUpdate.getCustomerID()));
            userIDField.setText(Integer.toString(appointmentToUpdate.getUserID()));

            //start time, end time, date
            startTimeSpinner.setValueFactory(TimeUtility.TimePicker(appointmentToUpdate.getStartTime().toLocalTime()));
            endTimeSpinner.setValueFactory(TimeUtility.TimePicker(appointmentToUpdate.getEndTime().toLocalTime()));
            datePicker.setValue(appointmentToUpdate.getStartTime().toLocalDate());

        } else {
            appointmentIDField.setText(Integer.toString(DataAccessRead.getLastAppointmentId() + 1));
        }
    }
}