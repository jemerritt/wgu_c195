package model;
/**
 *
 * @author Joseph Merritt
 */
public class Customer {
    private int id;
    private String name;
    private String phone;
    private String address;
    private String postal;
    private int division;

    /**Constructor for customer class.*/
    public Customer(int id, String name, String phone, String address, String postal, int division) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.address = address;
        this.postal = postal;
        this.division = division;
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
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the phone
     */
    public String getPhone() {
        return phone;
    }

    /**
     * @param phone the phone to set
     */
    public void setPrice(String phone) {
        this.phone = phone;
    }

    /**
     * @return the address
     */
    public String getAddress() {
        return address;
    }

    /**
     * @param address the address to set
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * @return the postal
     */
    public String getPostal() {
        return postal;
    }

    /**
     * @param postal the postal to set
     */
    public void setPostal(String postal) {
        this.postal = postal;
    }

    /**
     * @return the division
     */
    public int getDivision() {
        return division;
    }

    /**
     * @param division the division to set
     */
    public void setDivision(int division) {
        this.division = division;
    }

}