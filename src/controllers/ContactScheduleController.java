package controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;
import models.Appointment;
import models.Contact;
import utils.DataAccessRead;
import utils.TimeUtility;

import java.net.URL;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Controller class for the Contact Schedule Report window contact_schedule_report_view.fxml.
 * The user can select each Contact and it will display their Scheduled Appointments. The appointments
 * can be filtered by week, month, all appointments, and whether past appointments will be displayed or not.
 * @author Christian Dewey
 */
public class ContactScheduleController implements Initializable {
    @FXML ComboBox<Contact> contactComboBox;
    @FXML private RadioButton weekRadioBtn;
    @FXML private RadioButton monthRadioBtn;
    @FXML private RadioButton allRadioBtn;
    @FXML private CheckBox pastAppointmentCheckBox;

    @FXML TableView<Appointment> contactScheduleTableView;

    @FXML private TableColumn<Appointment, Integer> apptIDColumn;
    @FXML private TableColumn<Appointment, String> titleColumn;
    @FXML private TableColumn<Appointment, String> descrColumn;
    @FXML private TableColumn<Appointment, String> locationColumn;
    @FXML private TableColumn<Appointment, String> typeColumn;
    @FXML private TableColumn<Appointment, ZonedDateTime> startColumn;
    @FXML private TableColumn<Appointment, ZonedDateTime> endColumn;
    @FXML private TableColumn<Appointment, Integer> custIDColumn;

    private ObservableList<Appointment> filteredAppointmentList = DataAccessRead.getAppointmentList();
    private boolean showPastAppointments = false;

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
    private void exitButton(ActionEvent actionEvent) {
        ((Node)(actionEvent.getSource())).getScene().getWindow().hide();
    }

    /**
     * Used to toggle whether the table view will also show appointments whose Start Time has already passed within
     * the given week, month, or all appointments.
     */
    @FXML
    private void pastAppointmentCheckBox(ActionEvent actionEvent) {
        showPastAppointments = pastAppointmentCheckBox.isSelected();

        if (!contactComboBox.getSelectionModel().isEmpty()) {
            contactSelect(actionEvent);
            if (weekRadioBtn.isSelected()) { weekRadioBtnPressed(new ActionEvent()); }
            if (monthRadioBtn.isSelected()) { monthRadioBtnPressed(new ActionEvent()); }
            if (allRadioBtn.isSelected()) { allRadioBtnPressed(new ActionEvent()); }
        }
    }

    /**
     * When a contact is selected from the contactComboBox, the Table View is filtered to show only appointments
     * related to that Contact.
     */
    @FXML
    private void contactSelect(ActionEvent actionEvent) {
        filteredAppointmentList = FXCollections.observableList(DataAccessRead.getAppointmentList().stream()
                .filter(a -> a.getContactID() == contactComboBox.getValue().getId())
                .collect(Collectors.toList()));

        contactScheduleTableView.setItems(filteredAppointmentList);

        if (!showPastAppointments) {
            contactScheduleTableView.setItems(FXCollections.observableList(filteredAppointmentList.stream()
                    .filter(a -> !a.getStartTime().isBefore(ZonedDateTime.now()))
                    .collect(Collectors.toList())));
        }

        contactScheduleTableView.refresh();
    }

    /**
     * Shows all upcoming appointments in the given week for the selected Contact. If the 'Also Show Past Appointments'
     * CheckBox is selected, then the Table View will also show appointments whose Start Time has already passed in the
     * given week.
     */
    @FXML
    private void weekRadioBtnPressed(ActionEvent actionEvent) {
        monthRadioBtn.setSelected(false);
        allRadioBtn.setSelected(false);
        contactScheduleTableView.setPlaceholder(new Label("Contact has no scheduled appointments this week."));

        Predicate<Appointment> condition = a -> {
            ZonedDateTime from = a.getStartTime();
            ZonedDateTime now = ZonedDateTime.now();

            String inputWeekDate = from.format( DateTimeFormatter.ISO_WEEK_DATE);
            String currentWeekDate = now.format( DateTimeFormatter.ISO_WEEK_DATE);

            String inputWeek = inputWeekDate.substring( 0 , 8 ) ;
            String currentWeek = currentWeekDate.substring( 0 , 8 ) ;

            return inputWeek.equalsIgnoreCase( currentWeek ) && from.isAfter(now);
        };

        Predicate<Appointment> conditionAll = a -> {
            ZonedDateTime from = a.getStartTime();
            ZonedDateTime now = ZonedDateTime.now();

            String inputWeekDate = from.format( DateTimeFormatter.ISO_WEEK_DATE);
            String currentWeekDate = now.format( DateTimeFormatter.ISO_WEEK_DATE);

            String inputWeek = inputWeekDate.substring( 0 , 8 ) ;
            String currentWeek = currentWeekDate.substring( 0 , 8 ) ;

            return inputWeek.equalsIgnoreCase( currentWeek );
        };

        if (!showPastAppointments) {
            contactScheduleTableView.setItems(FXCollections.observableList(filteredAppointmentList.stream()
                    .filter(condition)
                    .collect(Collectors.toList())));
        } else {
            contactScheduleTableView.setItems(FXCollections.observableList(filteredAppointmentList.stream()
                    .filter(conditionAll)
                    .collect(Collectors.toList())));
        }

        contactScheduleTableView.refresh();
    }

    /**
     * Shows all upcoming appointments in the given month for the selected Contact. If the 'Also Show Past Appointments'
     * CheckBox is selected, then the Table View will also show appointments whose Start Time has already passed in the
     * given month.
     */
    @FXML
    private void monthRadioBtnPressed(ActionEvent actionEvent) {
        weekRadioBtn.setSelected(false);
        allRadioBtn.setSelected(false);
        contactScheduleTableView.setPlaceholder(new Label("Contact has no scheduled appointments this month."));

        Predicate<Appointment> condition = a -> {
            ZonedDateTime from = a.getStartTime();
            ZonedDateTime now = ZonedDateTime.now();

            return from.getMonth() == now.getMonth() && from.isAfter(now);
        };

        Predicate<Appointment> conditionAll = a -> {
            ZonedDateTime from = a.getStartTime();
            ZonedDateTime now = ZonedDateTime.now();

            return from.getMonth() == now.getMonth();
        };

        if (!showPastAppointments) {
            contactScheduleTableView.setItems(FXCollections.observableList(filteredAppointmentList.stream()
                    .filter(condition)
                    .collect(Collectors.toList())));
        } else {
            contactScheduleTableView.setItems(FXCollections.observableList(filteredAppointmentList.stream()
                    .filter(conditionAll)
                    .collect(Collectors.toList())));
        }

        contactScheduleTableView.refresh();
    }

    /**
     * Shows all upcoming appointments for the selected Contact. If the 'Also Show Past Appointments'
     * CheckBox is selected, then the Table View will also show appointments whose Start Time has already passed.
     */
    @FXML
    private void allRadioBtnPressed(ActionEvent actionEvent) {
        weekRadioBtn.setSelected(false);
        monthRadioBtn.setSelected(false);
        contactScheduleTableView.setPlaceholder(new Label("Contact has no appointments scheduled."));

        if (!showPastAppointments) {
            contactScheduleTableView.setItems(FXCollections.observableList(filteredAppointmentList.stream()
                    .filter(a -> !a.getStartTime().isBefore(ZonedDateTime.now()))
                    .collect(Collectors.toList())));
        } else {
            contactScheduleTableView.setItems(filteredAppointmentList);
        }

        contactScheduleTableView.refresh();
    }

    /**
     * Initializes the window and format of the TableView and ComboBox.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        contactComboBox.setItems(DataAccessRead.getContactList());
        contactComboBox.setCellFactory(comboBoxFactory);
        contactComboBox.setButtonCell(comboBoxFactory.call(null));

        weekRadioBtn.setSelected(false);
        monthRadioBtn.setSelected(false);
        allRadioBtn.setSelected(true);

        contactScheduleTableView.setPlaceholder(new Label("No scheduled appointments for the selected contact."));

        //contactScheduleTableView.setItems(DataAccessRead.getAppointmentList());
        contactScheduleTableView.getSortOrder().add(startColumn);

        // set up columns in the TableViews
        apptIDColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        descrColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        locationColumn.setCellValueFactory(new PropertyValueFactory<>("location"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        startColumn.setCellValueFactory(new PropertyValueFactory<>("startTime"));

        startColumn.setCellValueFactory(new PropertyValueFactory<>("startTime"));
        startColumn.setCellFactory(TimeUtility.getDateFactory());

        endColumn.setCellValueFactory(new PropertyValueFactory<>("endTime"));
        endColumn.setCellFactory(TimeUtility.getDateFactory());

        custIDColumn.setCellValueFactory(new PropertyValueFactory<>("customerID"));
    }
}