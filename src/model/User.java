package model;

/**
 *
 * @author Joseph Merritt
 */
public class User {
    private int id;
    private String userName;
    private String password;


    public User(int id, String userName, String password) {
        this.id = id;
        this.userName = userName;
        this.password = password;

    }

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
     * @return the username
     */
    public String getUserName() {
        return userName;
    }

    /**
     * @param userName the username to set
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

}