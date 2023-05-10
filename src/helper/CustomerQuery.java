package helper;

import model.*;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**CustomerQuery class provides methods for interacting with Customers table in database.
 *
 * @author Joseph Merritt
 * */
public abstract class CustomerQuery {

    /** Add customer with following attributes.
     * @param name      The customer name.
     * @param phone     The customer phone number.
     * @param address   The customer address.
     * @param postal    The customer postal code.
     * @param division  The customer division ID.
     *
     * @return rowsAffected The number of rows affected.
     */
    public static int  insertCustomer(String name, String phone, String address, String postal, int division) throws SQLException {

        String sql = "INSERT INTO CUSTOMERS (Customer_Name, Phone, Address, Postal_Code, Division_ID) VALUES(?, ?, ?, ?, ?)";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setString(1, name);
        ps.setString(2, phone);
        ps.setString(3, address);
        ps.setString(4, postal);
        ps.setInt(5, division);
        int rowsAffected = ps.executeUpdate();

        return rowsAffected;
    }

    /** Update selected customer.
     * @param name      The customer name.
     * @param phone     The customer phone number.
     * @param address   The customer address.
     * @param postal    The customer postal code.
     * @param division  The customer division ID.
     * @param id        The customer ID.
     *
     * @return rowsAffected The number of rows affected.
     */
    public static int updateCustomer(String name, String phone, String address, String postal, int division, int id) throws SQLException {

        String sql = "UPDATE CUSTOMERS SET Customer_Name=?, Phone=?, Address=?, Postal_Code=?, Division_ID=? WHERE Customer_ID = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setString(1, name);
        ps.setString(2, phone);
        ps.setString(3, address);
        ps.setString(4, postal);
        ps.setInt(5, division);
        ps.setInt(6, id);
        int rowsAffected = ps.executeUpdate();

        return rowsAffected;
    }

    /** Delete the selected customer from database.
     * @param id      The customer ID.
     *
     * @return rowsAffected The number of rows affected.
     */
    public static int deleteCustomer(int id) throws SQLException {

        for(Appointment a : AppointmentList.getAllAppointments()){
            if (a.getCustomerId() == id){
                AppointmentQuery.deleteAppointment(a.getId());
            }
        }

        String sql = "DELETE FROM CUSTOMERS WHERE Customer_ID = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setInt(1, id);
        int rowsAffected = ps.executeUpdate();

        return rowsAffected;
    }

    /**Query database for all customers and add to CustomerList.*/
    public static void selectAllCustomers() throws SQLException {
        String sql = "SELECT * FROM CUSTOMERS";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            Customer c = new Customer(rs.getInt("Customer_ID"), rs.getString("Customer_Name"),
                    rs.getString("Phone"), rs.getString("Address"),
                    rs.getString("Postal_Code"), rs.getInt("Division_ID"));
            CustomerList.addCustomer(c);
        }
    }
}
