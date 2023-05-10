package helper;

import model.*;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**CountryQuery class provides methods for interacting with database.
 *
 * @author Joseph Merritt
 * */
public abstract class CountryQuery {

    /**Query database for all Countries and add to CountriesList.*/
    public static void selectAllCountries() throws SQLException {
        CountryList.clearAllCountries();
        String sql = "SELECT * FROM COUNTRIES";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            Country c = new Country(rs.getInt("Country_ID"), rs.getString("Country"));
            CountryList.addCountry(c);
        }
    }

    /**Query database to determine Country based on Division ID.*/
    public static int selectCountryByDivision(int id) throws SQLException {
        String sql = "SELECT * FROM FIRST_LEVEL_DIVISIONS WHERE Division_ID = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setInt(1, id);
        ResultSet rs = ps.executeQuery();
        rs.next();

        return rs.getInt("Country_ID");
    }

}
