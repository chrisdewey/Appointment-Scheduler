package controllers;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import models.Country;
import models.Customer;
import models.FirstLvlDivision;
import utils.DataAccessRead;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

/**
 * Controller class for the Customer Report window customers_report_view.fxml.
 * Displays a report showing how many customers are located within each country, and if a country is selected the
 * window will display the number of customers who are located in each First-Level-Division within that country.
 * @author Christian Dewey
 */
public class CustomerReportController implements Initializable {
    @FXML private TableView<Map.Entry<String, Long>> countryTable;
    @FXML private TableColumn<Map.Entry<String, Long>, String> countryColumn;
    @FXML private TableColumn<Map.Entry<String, Long>, Long> numCountryColumn;

    @FXML private TableView<Map.Entry<String, Long>> divisionTable;
    @FXML private TableColumn<Map.Entry<String, Long>, String> divisionColumn;
    @FXML private TableColumn<Map.Entry<String, Long>, Long> numDivColumn;

    /**
     * Displays the count of all customers, grouped by each First-Level-Division within the selected country.
     * @param countryId the Country ID for the displayed First-Level-Divisions to match
     */
    private void populateDivisionTable(Integer countryId) {
        Map<Integer, Long> customerDivisionIdMap = DataAccessRead.getCustomerList().stream()
                .collect(Collectors.groupingBy(Customer::getFirstLvlDivisionID, Collectors.counting()));

        Map<String, Long> customerDivisionMap = new HashMap<>();

        for(Map.Entry<Integer, Long> entry : customerDivisionIdMap.entrySet()) {

            if (DataAccessRead.getDivisionList().stream().anyMatch(x -> x.getId() == entry.getKey()
                    && x.getCountryID() == countryId)) {

                String divisionName = Objects.requireNonNull(DataAccessRead.getDivisionList().stream()
                        .filter(d -> d.getId() == entry.getKey())
                        .findFirst().orElse(null)).getDivision();

                customerDivisionMap.put(divisionName, entry.getValue());
                customerDivisionIdMap.remove(entry);
            }
        }

        ObservableList<Map.Entry<String, Long>> divisionNumbers = FXCollections.observableArrayList(customerDivisionMap.entrySet());

        divisionTable.setItems(divisionNumbers);

        divisionColumn.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue().getKey()));
        numDivColumn.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue().getValue()));

        divisionTable.refresh();
    }

    /**
     * Exits the window.
     */
    @FXML
    private void exitButton(ActionEvent actionEvent) {
        ((Node) (actionEvent.getSource())).getScene().getWindow().hide();
    }

    /**
     * Displays the count of all customers, grouped by each Country in the countryTable. If a country is selected, the
     * selected countries ID is passed to populateDivisionTable(), where it will display the count in the divisionTable
     * grouped by the countries First-Level-Divisions.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        Map<Integer, Long> customerDivisionIdMap = DataAccessRead.getCustomerList().stream()
                .collect(Collectors.groupingBy(Customer::getFirstLvlDivisionID, Collectors.counting()));

        Map<String, Long> customerDivMap = new HashMap<>();

        for (Country c : DataAccessRead.getCountryList()) {

            long customerCount = 0L;

            for (Map.Entry<Integer, Long> entry : customerDivisionIdMap.entrySet()) {

                ObservableList<FirstLvlDivision> filteredList = FXCollections.observableList(DataAccessRead.getDivisionList()
                        .stream()
                        .filter(d -> d.getId() == entry.getKey()
                                && d.getCountryID() == c.getId())
                        .collect(Collectors.toList()));

                if (!filteredList.isEmpty()) {
                    customerCount = customerCount + entry.getValue();
                }
            }

            customerDivMap.put(c.getCountry(), customerCount);
        }

        ObservableList<Map.Entry<String, Long>> customerNumbers = FXCollections.observableArrayList(customerDivMap.entrySet());

        countryTable.setItems(customerNumbers);

        countryColumn.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue().getKey()));
        numCountryColumn.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue().getValue()));

        divisionTable.refresh();

        countryTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                String country = countryTable.getSelectionModel().getSelectedItem().getKey();
                int id = Objects.requireNonNull(DataAccessRead.getCountryList()
                        .stream()
                        .filter(c -> c.getCountry().equals(country))
                        .findFirst()
                        .orElse(null))
                        .getId();
                populateDivisionTable(id);

            }
        });
    }
}