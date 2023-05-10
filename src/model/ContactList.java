package model;


import helper.ContactQuery;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.SQLException;

/**Model for Contact List.
 *
 * @author Joseph Merritt
 * */
public class ContactList {

    private static ObservableList<Contact> allContacts = FXCollections.observableArrayList();

    /**
     * @param contact adds the contact to allContacts list.
     * */
    public static void addContact(Contact contact){
        allContacts.add(contact);
    }

    /**Query data for all contacts.
     * */
    public static void queryContacts() throws SQLException {
        ContactQuery.selectAllContacts();
    }

    /**Returns list of all contacts.
     * @return the list of all contacts.
     * */
    public static ObservableList<Contact> getAllContacts(){

        return allContacts;
    }

    /**Clears allContacts list.*/
    public static void clearAllContacts(){allContacts.clear();}

}
