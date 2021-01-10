package models;

import java.sql.Timestamp;
import java.time.LocalDateTime;

/**
 * Represents a Customer.
 * @author Christian Dewey
 */
public class Customer {
    private int id;
    private String name;
    private String address;
    private String postalCode;
    private String phone;
    private LocalDateTime createDate;
    private String createdBy;
    private Timestamp lastUpdate;
    private String lastUpdatedBy;
    private int firstLvlDivisionID;
    private String location;

    /**
     * Creates a Customer.
     * @param id the customer ID
     * @param name the name of the customer
     * @param address the address of the customer
     * @param postalCode the postal code of the customer
     * @param phone the customers phone number
     * @param createDate the date the customer was created
     * @param createdBy who the customer record was originally created by
     * @param lastUpdate the date of the last customer record update
     * @param lastUpdatedBy who the customer record was last updated by
     * @param firstLvlDivisionID the ID of the First-Level-Division the customer is in
     * @param location String of the location of the customer, including First-Level-Division and Country
     */
    public Customer(int id,
                    String name,
                    String address,
                    String postalCode,
                    String phone,
                    LocalDateTime createDate,
                    String createdBy,
                    Timestamp lastUpdate,
                    String lastUpdatedBy,
                    int firstLvlDivisionID,
                    String location) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.postalCode = postalCode;
        this.phone = phone;
        this.createDate = createDate;
        this.createdBy = createdBy;
        this.lastUpdate = lastUpdate;
        this.lastUpdatedBy = lastUpdatedBy;
        this.firstLvlDivisionID = firstLvlDivisionID;
        this.location = location;
        //this.firstLvlDivisionID = firstLvlDivisionID;
    }

    /**
     * Gets the Customer ID.
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the Customer ID.
     * @param id the ID to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Gets the Customer Name.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the Customer Name.
     * @param name the Name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the Customers Address.
     */
    public String getAddress() {
        return address;
    }

    /**
     * Sets the Customers Address.
     * @param address the Address to set
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * Gets the Customers Postal Code.
     */
    public String getPostalCode() {
        return postalCode;
    }

    /**
     * Sets the Customers Postal Code.
     * @param postalCode the Postal Code to set
     */
    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    /**
     * Gets the Customers Phone Number.
     */
    public String getPhone() {
        return phone;
    }

    /**
     * Sets the Customers Postal Code.
     * @param phone the Phone Number to set
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * Gets the Customer record Create Date.
     */
    public LocalDateTime getCreateDate() { return createDate; }

    /**
     * Sets the Customer record Create Date.
     * @param createDate the Create Date to Set
     */
    public void setCreateDate(LocalDateTime createDate) { this.createDate = createDate; }

    /**
     * Gets who the Customer record was created by.
     */
    public String getCreatedBy() { return createdBy; }

    /**
     * Sets who the Customer record was created by.
     * @param createdBy who the record was created by
     */
    public void setCreatedBy(String createdBy) { this.createdBy = createdBy; }

    /**
     * Gets the Customer record Last Update Date.
     */
    public Timestamp getLastUpdate() { return lastUpdate; }

    /**
     * Sets the Customer record Last Update Date.
     * @param lastUpdate the date of the Last Update to set
     */
    public void setLastUpdate(Timestamp lastUpdate) { this.lastUpdate = lastUpdate; }

    /**
     * Gets who the Customer record was Last Updated By.
     */
    public String getLastUpdatedBy() { return lastUpdatedBy; }

    /**
     * Sets who the Customer record was Last Updated By.
     * @param lastUpdatedBy who the Last Update was performed by
     */
    public void setLastUpdatedBy(String lastUpdatedBy) { this.lastUpdatedBy = lastUpdatedBy; }

    /**
     * Gets the Customers First-Level-Division ID.
     */
    public int getFirstLvlDivisionID() {
        return firstLvlDivisionID;
    }

    /**
     * Sets the Customers First-Level-Division ID.
     * @param firstLvlDivisionID the ID of the First-Level-Division to set
     */
    public void setFirstLvlDivisionID(int firstLvlDivisionID) {
        this.firstLvlDivisionID = firstLvlDivisionID;
    }

    /**
     * Sets the Customers Location.
     */
    public String getLocation() { return location; }

    /**
     * Gets the Customers Location.
     * @param location the Customers Location to set
     */
    public void setLocation(String location) { this.location = location; }
}