package models;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;

/**
 * Represents an Appointment.
 * @author Christian Dewey
 */
public class Appointment {
    private int id;
    private String title;
    private String description;
    private String location;
    private String type;
    private ZonedDateTime startTime;
    private ZonedDateTime endTime;
    private LocalDateTime createDate;
    private String createdBy;
    private Timestamp lastUpdate;
    private String lastUpdatedBy;
    private int customerID;
    private int userID;
    private int contactID;

    /**
     * Creates an Appointment.
     * @param id the Appointment ID
     * @param title the title of the Appointment
     * @param description the description of the Appointment
     * @param location the location of the Appointment
     * @param type the Appointment type
     * @param startTime the start time of the Appointment
     * @param endTime the end time of the Appointment
     * @param createDate the date the Appointment was created
     * @param createdBy who the Appointment was originally created by
     * @param lastUpdate the date of the last update to the Appointment
     * @param lastUpdatedBy who the Appointment was last updated by
     * @param customerID the ID of the Customer associated with the Appointment
     * @param userID the ID of the User associated with the Appointment
     * @param contactID the ID of the Contact associated with the Appointment
     */
    public Appointment(int id,
                       String title,
                       String description,
                       String location,
                       String type,
                       ZonedDateTime startTime,
                       ZonedDateTime endTime,
                       LocalDateTime createDate,
                       String createdBy,
                       Timestamp lastUpdate,
                       String lastUpdatedBy,
                       int customerID,
                       int userID,
                       int contactID) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.location = location;
        this.type = type;
        this.startTime = startTime;
        this.endTime = endTime;
        this.createDate = createDate;
        this.createdBy = createdBy;
        this.lastUpdate = lastUpdate;
        this.lastUpdatedBy = lastUpdatedBy;
        this.customerID = customerID;
        this.userID = userID;
        this.contactID = contactID;
    }

    /**
     * Gets the Appointment ID
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the Appointment ID
     * @param id the ID of the appointment to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Gets the Appointment Title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the Appointment Title
     * @param title the Title of the appointment to set
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Gets the Appointment Description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the Appointment Description
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Gets the Appointment Location
     */
    public String getLocation() {
        return location;
    }

    /**
     * Sets the Appointment Location
     * @param location the location of the appointment to set
     */
    public void setLocation(String location) {
        this.location = location;
    }

    /**
     * Gets the Appointment Type
     */
    public String getType() {
        return type;
    }

    /**
     * Sets the Appointment Type
     * @param type the type of the appointment to set
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Gets the Appointment Start Time
     */
    public ZonedDateTime getStartTime() { return startTime; }

    /**
     * Sets the Appointment Start Time
     * @param startTime the start time of the appointment to set
     */
    public void setStartTime(ZonedDateTime startTime) {
        this.startTime = startTime;
    }

    /**
     * Gets the Appointment End Time
     */
    public ZonedDateTime getEndTime() {
        return endTime;
    }

    /**
     * Sets the Appointment End Time
     * @param endTime the end time of the appointment to set
     */
    public void setEndTime(ZonedDateTime endTime) { this.endTime = endTime; }

    /**
     * Gets the Appointment record Create Date.
     */
    public LocalDateTime getCreateDate() { return createDate; }

    /**
     * Sets the Appointment record Create Date.
     * @param createDate the date the Appointment was originally created
     */
    public void setCreateDate(LocalDateTime createDate) { this.createDate = createDate; }

    /**
     * Gets who the Appointment record was created by.
     */
    public String getCreatedBy() { return createdBy; }

    /**
     * Sets who the Appointment record was created by.
     * @param createdBy who the appointment was created by originally
     */
    public void setCreatedBy(String createdBy) { this.createdBy = createdBy; }

    /**
     * Gets the Appointment record Last Update Date.
     */
    public Timestamp getLastUpdate() { return lastUpdate; }

    /**
     * Sets the Appointment record Last Update Date.
     * @param lastUpdate the date of the most recent update to the appointment information
     */
    public void setLastUpdate(Timestamp lastUpdate) { this.lastUpdate = lastUpdate; }

    /**
     * Gets who the Appointment record was Last Updated By.
     */
    public String getLastUpdatedBy() { return lastUpdatedBy; }

    /**
     * Sets who the Appointment record was Last Updated By.
     * @param lastUpdatedBy  who the appointment information was most recently updated by
     */
    public void setLastUpdatedBy(String lastUpdatedBy) { this.lastUpdatedBy = lastUpdatedBy; }

    /**
     * Gets the associated Customer ID
     */
    public int getCustomerID() {
        return customerID;
    }

    /**
     * Sets the associated Customer ID
     * @param customerID the ID of the associated Customer to set
     */
    public void setCustomerID(int customerID) {
        this.customerID = customerID;
    }

    /**
     * Gets the associated User ID
     */
    public int getUserID() {
        return userID;
    }

    /**
     * Sets the associated User ID
     * @param userID the ID of the associated User to set
     */
    public void setUserID(int userID) {
        this.userID = userID;
    }

    /**
     * Gets the associated Contact ID
     */
    public int getContactID() {
        return contactID;
    }

    /**
     * Sets the associated Contact ID
     * @param contactID the ID of the associated Contact to set
     */
    public void setContactID(int contactID) {
        this.contactID = contactID;
    }
}