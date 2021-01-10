package controllers;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import models.Appointment;
import models.Customer;
import utils.AlertBox;
import utils.DataAccessDelete;
import utils.DataAccessRead;
import utils.TimeUtility;

import java.io.IOException;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Controller class for the Main Application Screen using main_view.fxml.
 * @author Christian Dewey
 */
public class MainController {
    @FXML private Label userLabel;
    @FXML private RadioButton weekRadioBtn;
    @FXML private RadioButton monthRadioBtn;
    @FXML private RadioButton allRadioBtn;

    @FXML private TableView<Appointment> aptTableView;
    @FXML private TableColumn<Appointment, Integer> apptIDColumn;
    @FXML private TableColumn<Appointment, String> titleColumn;
    @FXML private TableColumn<Appointment, String> descrColumn;
    @FXML private TableColumn<Appointment, String> locationColumn;
    @FXML private TableColumn<Appointment, Integer> contactColumn;
    @FXML private TableColumn<Appointment, String> typeColumn;
    @FXML private TableColumn<Appointment, ZonedDateTime> startColumn;
    @FXML private TableColumn<Appointment, ZonedDateTime> endColumn;
    @FXML private TableColumn<Appointment, Integer> custIDColumn;

    @FXML private TableView<Customer> custTableView;
    @FXML private TableColumn<Customer, String> customerIDColumn;
    @FXML private TableColumn<Customer, String> custNameColumn;
    @FXML private TableColumn<Customer, String> addressColumn;
    @FXML private TableColumn<Customer, String> postalColumn;
    @FXML private TableColumn<Customer, String> phoneColumn;
    @FXML private TableColumn<Customer, Integer> custLocationColumn;

    private String sessionUsername;

    /**
     * When the 'View by Week' button is pressed, filters the observable list to show on the aptTableView only
     * the appointments which occur this Week, excluding past appointments.
     */
    @FXML
    private void weekRadioBtnPressed(ActionEvent actionEvent) {
        monthRadioBtn.setSelected(false);
        allRadioBtn.setSelected(false);
        aptTableView.setPlaceholder(new Label("No scheduled appointments for the next 7 days."));

        Predicate<Appointment> condition = a -> {
            ZonedDateTime from = a.getStartTime();
            ZonedDateTime now = ZonedDateTime.now();

            String inputWeekDate = from.format( DateTimeFormatter.ISO_WEEK_DATE);
            String currentWeekDate = now.format( DateTimeFormatter.ISO_WEEK_DATE);

            String inputWeek = inputWeekDate.substring( 0 , 8 ) ;
            String currentWeek = currentWeekDate.substring( 0 , 8 ) ;

            return inputWeek.equalsIgnoreCase( currentWeek ) && from.isAfter(now);
        };
        aptTableView.setItems(FXCollections.observableList(DataAccessRead.getAppointmentList().stream()
                .filter(condition)
                .collect(Collectors.toList())));
        aptTableView.refresh();
        aptTableView.getSortOrder().add(startColumn);
    }

    /**
     * When the 'View by Month' button is pressed, filters the observable list to show on the aptTableView only
     * the appointments which occur this Month, excluding past appointments.
     */
    @FXML
    private void monthRadioBtnPressed(ActionEvent actionEvent) {
        weekRadioBtn.setSelected(false);
        allRadioBtn.setSelected(false);
        aptTableView.setPlaceholder(new Label("No scheduled appointments this month."));

        Predicate<Appointment> condition = a -> {
            ZonedDateTime from = a.getStartTime();
            ZonedDateTime now = ZonedDateTime.now();

            return from.getMonth() == now.getMonth() && from.isAfter(now);
        };
        aptTableView.setItems(FXCollections.observableList(DataAccessRead.getAppointmentList().stream()
                .filter(condition)
                .collect(Collectors.toList())));
        aptTableView.refresh();
        aptTableView.getSortOrder().add(startColumn);
    }
    /**
     * When the 'View All' button is pressed, the observable list is shown on the aptTableView
     * without any filters.
     */
    @FXML
    private void allRadioBtnPressed() {
        weekRadioBtn.setSelected(false);
        monthRadioBtn.setSelected(false);
        aptTableView.setPlaceholder(new Label("No appointments scheduled."));

        aptTableView.setItems(DataAccessRead.getAppointmentList());
        aptTableView.refresh();
        aptTableView.getSortOrder().add(startColumn);
    }

    /**
     * Opens the Contact Schedules window.
     */
    @FXML
    private void contactScheduleReport() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/contact_schedule_report_view.fxml"));

        Stage primaryStage = new Stage();
        Scene scene = new Scene(new StackPane());
        scene.setRoot(loader.load());
        primaryStage.initModality(Modality.APPLICATION_MODAL);
        primaryStage.initStyle(StageStyle.DECORATED);

        primaryStage.setTitle("Contact Schedules");
        primaryStage.setScene(scene);

        primaryStage.showAndWait();
    }

    /**
     * Opens the Appointment Report window showing the number of upcoming appointments by Type and Month
     */
    @FXML
    private void appointmentByCustomerReport() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/appointments_report_view.fxml"));

        Stage primaryStage = new Stage();
        Scene scene = new Scene(new StackPane());
        scene.setRoot(loader.load());
        primaryStage.initModality(Modality.APPLICATION_MODAL);
        primaryStage.initStyle(StageStyle.DECORATED);

        primaryStage.setTitle("Appointments By Customer");
        primaryStage.setScene(scene);

        primaryStage.showAndWait();
    }

    /**
     * Opens the Customer Report window showing how many customers are located in each Country and Division.
     */
    @FXML
    private void customerReport() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/customers_report_view.fxml"));

        Stage primaryStage = new Stage();
        Scene scene = new Scene(new StackPane());
        scene.setRoot(loader.load());
        primaryStage.initModality(Modality.APPLICATION_MODAL);
        primaryStage.initStyle(StageStyle.DECORATED);

        primaryStage.setTitle("Appointments By Customer");
        primaryStage.setScene(scene);

        primaryStage.showAndWait();
    }

    /**
     * Opens the Add Appointment window.
     * Also passes isNewAppointment = 'true' to the AddAppointment Controller, which
     * informs that the appointment will be a new one and not an existing one to be updated.
     */
    @FXML
    private void createAppointmentButton() throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/add_appointment_view.fxml"));

        Stage primaryStage = new Stage();
        Scene scene = new Scene(new StackPane());
        scene.setRoot(loader.load());
        primaryStage.initModality(Modality.APPLICATION_MODAL);
        primaryStage.initStyle(StageStyle.DECORATED);

        primaryStage.setTitle("Add Appointment");
        primaryStage.setScene(scene);

        controllers.AddAppointment controller = loader.getController();
        controller.initData(true, null, sessionUsername);

        primaryStage.showAndWait();

        if (weekRadioBtn.isSelected()) {
            aptTableView.refresh();
            weekRadioBtnPressed(new ActionEvent());
        } else if (monthRadioBtn.isSelected()) {
            aptTableView.refresh();
            monthRadioBtnPressed(new ActionEvent());
        } else {
            aptTableView.refresh();
        }

        aptTableView.getSortOrder().add(startColumn);
    }

    /**
     * Opens the Add Appointment window.
     * Also passes isNewAppointment = 'false' with the selected Appointment to the AddAppointment Controller, which
     * allows the controller to update the existing appointment, rather than create a new one.
     */
    @FXML
    private void updateAppointmentButton() throws IOException {

        Appointment selectedAppointment = aptTableView.getSelectionModel().getSelectedItem();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/add_appointment_view.fxml"));

        Stage primaryStage = new Stage();
        Scene scene = new Scene(new StackPane());
        scene.setRoot(loader.load());
        primaryStage.initModality(Modality.APPLICATION_MODAL);
        primaryStage.initStyle(StageStyle.DECORATED);

        primaryStage.setTitle("Update Appointment Information");
        primaryStage.setScene(scene);

        controllers.AddAppointment controller = loader.getController();
        controller.initData(false, selectedAppointment, sessionUsername);

        primaryStage.showAndWait();
        aptTableView.refresh();
        aptTableView.getSortOrder().add(startColumn);
    }

    /**
     * Deletes the selected appointment from the database, the observable list, and the aptTableView.
     */
    @FXML
    private void deleteAppointmentButton() {
        if (aptTableView.getSelectionModel().getSelectedItem() == null) {
            AlertBox.selectionErrorTest("Appointment");
        } else {
            Appointment selectedAppointment = aptTableView.getSelectionModel().getSelectedItem();
            DataAccessDelete.deleteAppointment(selectedAppointment);

            if (weekRadioBtn.isSelected()) {
                aptTableView.refresh();
                weekRadioBtnPressed(new ActionEvent());
            } else if (monthRadioBtn.isSelected()) {
                aptTableView.refresh();
                monthRadioBtnPressed(new ActionEvent());
            } else {
                aptTableView.refresh();
            }
        }
    }

    /**
     * Opens the Add Customer window.
     * Also passes isNewCustomer = 'true' to the AddCustomer Controller, which
     * informs that the Customer will be a new one and not an existing one to be updated.
     */
    @FXML
    private void createCustomerButton() throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/add_customer_view.fxml"));

        Stage primaryStage = new Stage();
        Scene scene = new Scene(new StackPane());
        scene.setRoot(loader.load());
        primaryStage.initModality(Modality.APPLICATION_MODAL);
        primaryStage.initStyle(StageStyle.DECORATED);

        primaryStage.setTitle("Add Customer");
        primaryStage.setScene(scene);

        controllers.AddCustomer controller = loader.getController();
        controller.initData(true, null, sessionUsername);

        primaryStage.showAndWait();
        custTableView.refresh();
    }

    /**
     * Opens the Add Customer window.
     * Also passes isNewCustomer = 'false' with the selected Customer to the AddCustomer Controller, which
     * allows the controller to update the existing Customer, rather than create a new one.
     */
    @FXML
    private void updateCustomerButton() throws IOException {

        Customer selectedCustomer = custTableView.getSelectionModel().getSelectedItem();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/add_customer_view.fxml"));

        Stage primaryStage = new Stage();
        Scene scene = new Scene(new StackPane());
        scene.setRoot(loader.load());
        primaryStage.initModality(Modality.APPLICATION_MODAL);
        primaryStage.initStyle(StageStyle.DECORATED);

        primaryStage.setTitle("Update Customer Information");
        primaryStage.setScene(scene);

        controllers.AddCustomer controller = loader.getController();
        controller.initData(false, selectedCustomer, sessionUsername);

        primaryStage.showAndWait();
        custTableView.refresh();
    }

    /**
     * Deletes the selected Customer from the database, the observable list, and the custTableView.
     */
    @FXML
    private void deleteCustomerButton() {
        if (custTableView.getSelectionModel().getSelectedItem() == null) {
            AlertBox.selectionErrorTest("Customer");
        } else {
            Customer selectedCustomer = custTableView.getSelectionModel().getSelectedItem();
            DataAccessDelete.deleteCustomer(selectedCustomer);

            aptTableView.refresh();
        }
    }

    /**
     * On user login, checks whether there is an appointment within the next 15 minutes. If there is, displays
     * the appointment information. If there is no upcoming appointment it informs the user.
     */
    private void upcomingAppointmentCheck() {
        Appointment appointment = DataAccessRead.getAppointmentList()
                .stream()
                .filter(a -> a.getStartTime().isAfter(ZonedDateTime.now()) &&
                        a.getStartTime().isBefore(ZonedDateTime.now().plusMinutes(15)))
                .findFirst()
                .orElse(null);

        AlertBox.appointmentSoonAlert(appointment);
    }

    /**
     * Exits the application
     */
    @FXML
    private void exitButton() { if(AlertBox.exitBox()) Platform.exit(); }

    /**
     * Calls the 'fetch data' methods in DataAccessRead to fetch all needed data from the database.
     * Loads the tableviews and populates them with their associated data.
     * 'weekRadioBtnPressed' is called so the user sees the 'Week Appointment View' first.
     */
    public void initialize() {
        aptTableView.setPlaceholder(new Label("No scheduled appointments for the next 7 days."));
        custTableView.setPlaceholder(new Label("No customers have been added."));

        // set up columns in the TableViews
        apptIDColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        descrColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        locationColumn.setCellValueFactory(new PropertyValueFactory<>("location"));
        contactColumn.setCellValueFactory(new PropertyValueFactory<>("contactID"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        startColumn.setCellValueFactory(new PropertyValueFactory<>("startTime"));

        startColumn.setCellValueFactory(new PropertyValueFactory<>("startTime"));
        startColumn.setCellFactory(TimeUtility.getDateFactory());

        endColumn.setCellValueFactory(new PropertyValueFactory<>("endTime"));
        endColumn.setCellFactory(TimeUtility.getDateFactory());

        custIDColumn.setCellValueFactory(new PropertyValueFactory<>("customerID"));

        customerIDColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        custNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        addressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));
        postalColumn.setCellValueFactory(new PropertyValueFactory<>("postalCode"));
        phoneColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));
        custLocationColumn.setCellValueFactory(new PropertyValueFactory<>("location"));

        // Fetch data from DB
        DataAccessRead.fetchContactData();
        DataAccessRead.fetchDivisionData();
        DataAccessRead.fetchCountryData();
        DataAccessRead.fetchUserData();

        // Load the data onto the TableViews
        DataAccessRead.fetchAppointmentData();
        aptTableView.setItems(DataAccessRead.getAppointmentList());
        aptTableView.getSortOrder().add(startColumn);

        DataAccessRead.fetchCustomerData();
        custTableView.setItems(DataAccessRead.getCustomerList());

        weekRadioBtnPressed(new ActionEvent());
    }

    /**
     * Receives the username from the login screen upon successful login to be displayed on the Main Screen UI.
     */
    public void initSession(String sessionUsername) {
        userLabel.setText("Current User: " + sessionUsername);
        this.sessionUsername = sessionUsername;

        upcomingAppointmentCheck();
    }
}