package models;

/**
 * Represents a Country.
 * @author Christian Dewey
 */
public class Country {
    private final int id;
    private final String country;

    /**
     * Creates a Country.
     * @param id the Country ID
     * @param country the name of the Country
     */
    public Country(int id, String country) {
        this.id = id;
        this.country = country;
    }

    /**
     * Gets the Country ID
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the Country ID
     */
    public String getCountry() {
        return country;
    }
}