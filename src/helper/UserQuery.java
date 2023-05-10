package helper;

import model.*;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**UserQuery class provides methods for interacting with User table in database.
 *
 * @author Joseph Merritt
 * */
public abstract class UserQuery {

    /**Query database for all users and add to UserList.*/
    public static void selectAllUsers() throws SQLException {
        String sql = "SELECT * FROM USERS";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            User u = new User(rs.getInt("User_ID"), rs.getString("User_Name"),
                    rs.getString("Password"));
            UserList.addUser(u);
        }
    }

}
