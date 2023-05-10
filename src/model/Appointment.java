package model;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

/** Appointment class holds data for appointments.
 *
 * @author Joseph Merritt
 */
public class Appointment {
    private int id;
    private String title;
    private String desc;
    private String location;

    private String type;
    private Timestamp start;
    private Timestamp end;
    private int customerId;
    private int userId;
    private int contactId;

    /** Constructor for Appointment class.*/
    public Appointment(int id, String title, String desc, String location, String type, Timestamp start, Timestamp end, int customerId, int userId, int contactId){
        this.id = id;
        this.title = title;
        this.desc = desc;
        this.location = location;
        this.contactId = contactId;
        this.type = type;
        this.start = start;
        this.end = end;
        this.customerId = customerId;
        this.userId = userId;
    }

    /**
     * @return the id.
     */
    public int getId() {
        return id;
    }

    /**
     * @param id the id to set.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return the title.
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param title the title to set.
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @return the description.
     */
    public String getDesc() {
        return desc;
    }

    /**
     * @param desc the description to set.
     */
    public void setDesc(String desc) {
        this.desc = desc;
    }

    /**
     * @return the location.
     */
    public String getLocation() {
        return location;
    }

    /**
     * @param location the location to set.
     */
    public void setLocation(String location) {
        this.location = location;
    }

    /**
     * @return the type.
     */
    public String getType() {
        return type;
    }

    /**
     * @param type the type to set.
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * @return the start.
     */
    public Timestamp getStart() {
        return start;
    }

    /**
     * @param start the start time to set.
     */
    public void setStart(Timestamp start) {
        this.start = start;
    }

    /**
     * @return the end.
     */
    public Timestamp getEnd() {
        return end;
    }

    /**
     * @param end the end time to set.
     */
    public void setEnd(Timestamp end) {
        this.end = end;
    }

    /**
     * @return the customer ID.
     */
    public int getCustomerId() {
        return customerId;
    }

    /**
     * @param customerId the customer ID to set.
     */
    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    /**
     * @return the user ID.
     */
    public int getUserId() {
        return userId;
    }

    /**
     * @param userId the user ID to set.
     */
    public void setUserId(int userId) {
        this.userId = userId;
    }

    /**
     * @return the contact ID to return.
     */
    public int getContactId() {
        return contactId;
    }

    /**
     * @param contactId the contact to set.
     */
    public void setContactId(int contactId) {
        this.contactId = contactId;
    }

    /**
     * @return the month of the appointment.
     * */
    public String getMonth(){
        LocalDateTime ldt = start.toLocalDateTime();

        return ldt.getMonth() + " " + ldt.getYear();
    }

    public String getContactName(){
        for(Contact c : ContactList.getAllContacts()){
            if(contactId == c.getId()){
                return c.getName();
            }
        }
        return null;
    }
}