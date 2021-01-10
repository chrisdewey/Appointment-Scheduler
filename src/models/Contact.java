package models;

/**
 * Represents a Contact.
 * @author Christian Dewey
 */
public class Contact {
    private final int id;
    private final String name;
    private final String email;

    /**
     * Creates a Contact.
     * @param id the Contacts ID
     * @param name the name of the Contact
     * @param email the Contacts Email Address
     */
    public Contact(int id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }

    /**
     * Gets the Contacts ID
     */
    public int getId() {
        return id;
    }

    /**
     * Gets the Contacts Name
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the Contacts Email Address
     */
    public String getEmail() {
        return email;
    }
}