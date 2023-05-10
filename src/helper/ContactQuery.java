package helper;

import model.*;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**ContactQuery class provides methods for interacting with Contacts table in database.
 *
 * @author Joseph Merritt
 * */
public abstract class ContactQuery {

    /**Query database for all Contacts and add to ContactList.*/
    public static void selectAllContacts() throws SQLException {
        String sql = "SELECT * FROM CONTACTS";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            int cId = rs.getInt("Contact_ID");
            for(Contact c : ContactList.getAllContacts()){
                if(cId == c.getId()){
                    rs.next();
                }
            }
            Contact contact = new Contact(cId, rs.getString("Contact_Name"),
                    rs.getString("Email"));
            ContactList.addContact(contact);
        }
    }

}
