package model;


import helper.UserQuery;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.SQLException;

/**Model for User List.
 *
 * @author Joseph Merritt
 * */
public class UserList {

    private static ObservableList<User> allUsers = FXCollections.observableArrayList();

    public static void addUser(User user){
        allUsers.add(user);
    }

    /**Returns list of all users.
     * @return the list of all users.
     * */
    public static ObservableList<User> getAllUsers() throws SQLException {

        clearAllUser();
        UserQuery.selectAllUsers();

        return allUsers;
    }

    /**Clears allUsers list.*/
    public static void clearAllUser(){allUsers.clear();}

}
