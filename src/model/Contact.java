package model;



/**
 *
 * @author Joseph Merritt
 */
public class Contact {
    private int id;
    private String name;
    private String email;

    /** Constructor for Appointment class.*/
    public Contact(int id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;

    }
    @Override
    public String toString() {return name;}

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the title to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * @param email the email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

}