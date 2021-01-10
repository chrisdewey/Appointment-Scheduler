package models;

/**
 * Represents a First-Level-Division.
 * @author Christian Dewey
 */
public class FirstLvlDivision {
    private final int id;
    private final String division;
    private final int countryID;

    /**
     * Creates a First-Level-Division.
     * @param id the First-Level-Division ID
     * @param division the name of the First-Level-Division
     * @param countryID ID of the country the First-Level-Division is in
     */
    public FirstLvlDivision(int id, String division, int countryID) {
        this.id = id;
        this.division = division;
        this.countryID = countryID;
    }

    /**
     * Gets the First-Level-Division ID.
     */
    public int getId() {
        return id;
    }

    /**
     * Gets the First-Level-Divisions name.
     */
    public String getDivision() {
        return division;
    }

    /**
     * Gets the First-Level-Division associated country ID.
     */
    public int getCountryID() {
        return countryID;
    }
}