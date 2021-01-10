package models;

/**
 * Represents a user.
 * @author Christian Dewey
 */
public class User {
    private final int id;

    /**
     * Creates a User with the specified id.
     * @param id id of the user
     */
    public User(int id) {
        this.id = id;
    }

    /**
     * Gets the Users ID.
     */
    public int getId() {
        return id;
    }
}