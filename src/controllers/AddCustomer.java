package controllers;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.util.Callback;

import models.*;
import utils.AlertBox;
import utils.DataAccessCreate;
import utils.DataAccessRead;
import utils.DataAccessUpdate;

import java.net.URL;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

/**
 * Controller class for the Add Customer window add_customer_view.fxml.
 * Allows the user to create a new Customer and add it to the Database, or update an existing one.
 * @author Christian Dewey
 */
public class AddCustomer implements Initializable {
    @FXML TextField customerIDField;
    @FXML TextField customerNameField;
    @FXML TextField addressField;
    @FXML TextField postalCodeField;
    @FXML TextField phoneField;
    @FXML ComboBox<Country> countryComboBox;
    @FXML ComboBox<FirstLvlDivision> divisionComboBox;

    private boolean isNewCustomer;
    private Customer customerToUpdate;
    private String sessionUsername;

    /**
     * Callback method used to populate the countryComboBox with all Countries, displaying their name.
     * When a Country is selected from the ComboBox, the Country is returned, rather than just the name,
     * allowing convenient access to the Country object for use in other methods.
     */
    private final Callback<ListView<Country>, ListCell<Country>> countryBoxFactory = lv -> new ListCell<>() {
        @Override
        protected void updateItem(Country item, boolean empty) {
            super.updateItem(item, empty);
            setText(empty ? "" : item.getCountry());
        }
    };

    /**
     * Callback method used to populate the divisionComboBox with all First-Level-Divisions, displaying their name.
     * When a First-Level-Divisions is selected from the ComboBox, the First-Level-Divisions is returned, rather than
     * just the name, allowing convenient access to the First-Level-Divisions object for use in other methods.
     */
    private final Callback<ListView<FirstLvlDivision>, ListCell<FirstLvlDivision>> divisionBoxFactory = lv -> new ListCell<>() {
        @Override
        protected void updateItem(FirstLvlDivision item, boolean empty) {
            super.updateItem(item, empty);
            setText(empty ? "" : item.getDivision());
        }
    };

    /**
     * When a Country is selected in the countryComboBox, the divisionComboBox is enabled and populated with all
     * First-Level-Divisions associated with that country.
     */
    @FXML
    private void countryComboBoxAction(ActionEvent actionEvent) {
        divisionComboBox.setDisable(false);
        divisionComboBox.setItems(FXCollections.observableList(DataAccessRead.getDivisionList().stream()
                .filter(div -> div.getCountryID() == countryComboBox.getValue().getId())
                .collect(Collectors.toList())));
    }

    /**
     * Closes the window.
     */
    @FXML
    public void cancelButton(ActionEvent actionEvent) {
        ((Node)(actionEvent.getSource())).getScene().getWindow().hide();
    }

    /**
     * When the Save Button is pressed, a new Customer object is created and added to the customerList as well as
     * the Database. If 'isNewCustomer' is false, then the existing Customer object is updated with the new
     * information instead of creating a new Customer.
     */
    @FXML
    public void saveButton(ActionEvent actionEvent) {
        if (AlertBox.saveConfirmation("Customer")) {

            String name = customerNameField.getText();
            String address = addressField.getText();
            String postalCode = postalCodeField.getText();
            String phone = phoneField.getText();

            int divisionID = 0;
            if (divisionComboBox.getSelectionModel().getSelectedItem() != null) {
                divisionID = divisionComboBox.getValue().getId();
            }

            String country = "";
            if (divisionComboBox.getSelectionModel().getSelectedItem() != null) {
                country = countryComboBox.getValue().getCountry();
            }

            if (AlertBox.fieldCheckCustomer(name, address, postalCode, phone, divisionID, country)) {


                // if this is a NEW customer, else it's an UPDATE statement
                if (isNewCustomer) {

                    Customer newCustomer = new Customer(
                            Integer.parseInt(customerIDField.getText()),
                            name,
                            address,
                            postalCode,
                            phone,
                            LocalDateTime.now(),
                            sessionUsername,
                            Timestamp.valueOf(LocalDateTime.now()),
                            sessionUsername,
                            divisionID,
                            country
                    );

                    DataAccessCreate.createNewCustomer(newCustomer);
                    DataAccessRead.addToCustomerList(newCustomer);

                } else {
                    customerToUpdate.setName(name);
                    customerToUpdate.setAddress(address);
                    customerToUpdate.setPostalCode(postalCode);
                    customerToUpdate.setPhone(phone);
                    customerToUpdate.setLastUpdate(Timestamp.valueOf(LocalDateTime.now()));
                    customerToUpdate.setFirstLvlDivisionID(divisionID);

                    String location = divisionComboBox.getValue().getDivision() + ", " + country;
                    customerToUpdate.setLocation(location);

                    DataAccessUpdate.updateCustomer(customerToUpdate);
                    //                    try {
//                        DataAccessUpdate.updateCustomer(customerToUpdate);
//                        ((Node) (actionEvent.getSource())).getScene().getWindow().hide();
//
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
                }
                ((Node) (actionEvent.getSource())).getScene().getWindow().hide();
            }
        }
    }

    /**
     * Initializes the window and sets up ComboBoxes.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        divisionComboBox.setItems(DataAccessRead.getDivisionList());
        divisionComboBox.setCellFactory(divisionBoxFactory);
        divisionComboBox.setButtonCell(divisionBoxFactory.call(null));

        countryComboBox.setItems(DataAccessRead.getCountryList());
        countryComboBox.setCellFactory(countryBoxFactory);
        countryComboBox.setButtonCell(countryBoxFactory.call(null));
    }

    /**
     * Used to decide whether the user is creating a new Customer or updating an existing one. If it is an
     * existing customer to be updated, the text fields are populated with the Customers information.
     * @param isNewCustomer Whether the user is creating a new Customer, or updating an existing one.
     * @param customerToUpdate The Customer object to be updated. If creating a new Customer set this as null.
     * @param sessionUsername The username of the User updating/creating the Customer.
     */
    public void initData(Boolean isNewCustomer, Customer customerToUpdate, String sessionUsername) {
        this.isNewCustomer = isNewCustomer;
        this.customerToUpdate = customerToUpdate;
        this.sessionUsername = sessionUsername;

        // Load Data into fields if it's an update (isNewCustomer = true), else only populate ID field for new appointments.
        if(!isNewCustomer) {
            customerIDField.setText(Integer.toString(customerToUpdate.getId()));
            customerNameField.setText(customerToUpdate.getName());
            addressField.setText(customerToUpdate.getAddress());
            postalCodeField.setText(customerToUpdate.getPostalCode());
            phoneField.setText(customerToUpdate.getPhone());

            divisionComboBox.setValue(DataAccessRead.getDivisionList().stream()
                    .filter(div -> div.getId() == customerToUpdate.getFirstLvlDivisionID())
                    .findFirst().orElse(null));
            divisionComboBox.setDisable(false);

            countryComboBox.setValue(DataAccessRead.getCountryList().stream()
                    .filter(country -> country.getId() == divisionComboBox.getValue().getCountryID())
                    .findFirst().orElse(null));

        } else {
            customerIDField.setText(Integer.toString(DataAccessRead.getLastCustomerId() + 1));
        }
    }
}