package helper;

import model.*;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**DivisionQuery class provides methods for interacting with First_Level_Divisions table in database.
 *
 * @author Joseph Merritt
 * */
public abstract class DivisionQuery {

    /** Query database for division with the associated ID.
     * @param id      The division id.
     */
    public static void selectRelatedDivisions(int id) throws SQLException {
        DivisionList.clearAllDivisions();
        String sql = "SELECT * FROM FIRST_LEVEL_DIVISIONS WHERE Country_ID = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setInt(1, id);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            Division d = new Division(rs.getInt("Division_ID"), rs.getString("Division"));
            DivisionList.addDivision(d);
        }
    }

}
