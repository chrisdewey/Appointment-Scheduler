package controllers;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import models.Appointment;
import utils.DataAccessRead;

import java.net.URL;
import java.time.LocalDate;
import java.time.Month;
import java.time.ZonedDateTime;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

/**
 * Controller class for the Appointment Report window appointments_report_view.fxml.
 * The total number of upcoming appointments, grouped by the Type of the Appointment, will be displayed for the
 * selected month. If the 'Also Show Past Appointments' CheckBox is selected, then appointments whose Start Time
 * has already passed will also be counted.
 * @author Christian Dewey
 */
public class AppointmentReportController implements Initializable {
    @FXML private TableView<Map.Entry<String, Long>> typeTable;
    @FXML private TableColumn<Map.Entry<String, Long>, String> typeColumn;
    @FXML private TableColumn<Map.Entry<String, Long>, Long> typeTotalColumn;

    @FXML private ComboBox<Month> monthComboBox;
    @FXML private CheckBox pastAppointmentCheckBox;

    private final Month thisMonth = LocalDate.now().getMonth();

    /**
     * Within the selected month, all upcoming appointments are counted and grouped by their Type. If the 'Also Show
     * Past Appointments' CheckBox is selected, then appointments whose Start Time have already passed will also be
     * counted.
     * @param month the selected month to filter the list by
     */
    private void typeMap(Month month) {
        ObservableList<Appointment> appointmentsList;

        if(pastAppointmentCheckBox.isSelected()) {
            appointmentsList = FXCollections.observableArrayList(
                    DataAccessRead.getAppointmentList()
                            .stream()
                            .filter(t -> t.getStartTime().getMonth() == month)
                            .collect(Collectors.toList()));
        } else {
            appointmentsList = FXCollections.observableArrayList(
                    DataAccessRead.getAppointmentList()
                            .stream()
                            .filter(t -> t.getStartTime().isAfter(ZonedDateTime.now()) && t.getStartTime().getMonth() == month)
                            .collect(Collectors.toList()));
        }

        Map<String, Long> appointmentsMap = appointmentsList.stream()
                .collect(Collectors.groupingBy(Appointment::getType, Collectors.counting()));

        ObservableList<Map.Entry<String, Long>> items = FXCollections.observableArrayList(appointmentsMap.entrySet());

        typeTable.setItems(items);

        typeColumn.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue().getKey()));
        typeTotalColumn.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue().getValue()));

        typeTable.refresh();
    }

    /**
     * When a month is selected from the monthComboBox, the typeMap() method is called and the selected month is
     * passed to it.
     */
    @FXML
    private void monthSelect(ActionEvent actionEvent) {
        Month selection = monthComboBox.getSelectionModel().getSelectedItem();
        typeMap(selection);

        typeTable.refresh();
    }

    /**
     * When the 'Also Show Past Appointments' CheckBox is toggled, the monthSelect() method is called again to
     * update the TableView.
     */
    @FXML
    private void pastAppointmentCheckBox(ActionEvent event) {
        monthSelect(new ActionEvent());
    }

    /**
     * Exits the window.
     */
    @FXML
    private void exitButton(ActionEvent actionEvent) {
        ((Node) (actionEvent.getSource())).getScene().getWindow().hide();
    }

    /**
     * Initializes the window and adds the months to the monthComboBox. Sets the selected month to the current month.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        typeTable.setPlaceholder(new Label("There are no appointments for the selected month."));

        monthComboBox.getItems().addAll(
                Month.valueOf("JANUARY"),
                Month.valueOf("FEBRUARY"),
                Month.valueOf("MARCH"),
                Month.valueOf("APRIL"),
                Month.valueOf("MAY"),
                Month.valueOf("JUNE"),
                Month.valueOf("JULY"),
                Month.valueOf("AUGUST"),
                Month.valueOf("SEPTEMBER"),
                Month.valueOf("OCTOBER"),
                Month.valueOf("NOVEMBER"),
                Month.valueOf("DECEMBER"));
        monthComboBox.setVisibleRowCount(12);

        monthComboBox.setValue(thisMonth);
        monthSelect(new ActionEvent());
    }
}