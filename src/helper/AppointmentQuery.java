package helper;

import model.*;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

/**AppointmentQuery class provides methods for interacting with Appointments table in database.
 *
 * @author Joseph Merritt
 * */
public abstract class AppointmentQuery {

    /** Add appointment with following attributes.
     * @param title      The appointment title.
     * @param desc       The appointment description.
     * @param location   The appointment location.
     * @param start      The appointment start time.
     * @param end        The appointment end time.
     * @param customerId The appointment customer ID.
     * @param userId     The appointment User ID.
     * @param contactId  The appointment contact ID.
     *
     * @return rowsAffected The number of rows affected.
     */
    public static int insertAppointment(String title, String desc, String location, String type, Timestamp start, Timestamp end, int customerId, int userId, int contactId) throws SQLException {

        String sql = "INSERT INTO APPOINTMENTS (Title, Description, Location, Type, Start, End, Customer_ID, USER_ID, Contact_ID) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setString(1, title);
        ps.setString(2, desc);
        ps.setString(3, location);
        ps.setString(4, type);
        ps.setTimestamp(5, start);
        ps.setTimestamp(6, end);
        ps.setInt(7, customerId);
        ps.setInt(8, userId);
        ps.setInt(9, contactId);
        int rowsAffected = ps.executeUpdate();

        return rowsAffected;
    }

    /**Update selected appointment.
     * @param title         The appointment title.
     * @param desc          The appointment description.
     * @param location      The appointment location.
     * @param start         The appointment start time.
     * @param end           The appointment end time.
     * @param customerId    The appointment customer ID.
     * @param userId        The appointment User ID.
     * @param contactId     The appointment contact ID.
     * @param appointmentId The appointment ID.
     *
     * @return rowsAffected The number of rows affected.
     */
    public static int updateAppointment(String title, String desc, String location, String type, Timestamp start, Timestamp end, int customerId, int userId, int contactId, int appointmentId) throws SQLException {

        String sql = "UPDATE APPOINTMENTS SET Title=?, Description=?, Location=?, Type=?, Start=?, End=?, Customer_ID=?, USER_ID=?, Contact_ID=? WHERE Appointment_ID = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setString(1, title);
        ps.setString(2, desc);
        ps.setString(3, location);
        ps.setString(4, type);
        ps.setTimestamp(5, start);
        ps.setTimestamp(6, end);
        ps.setInt(7, customerId);
        ps.setInt(8, userId);
        ps.setInt(9, contactId);
        ps.setInt(10, appointmentId);
        int rowsAffected = ps.executeUpdate();

        return rowsAffected;
    }

    /** Delete the selected appointment from database.
     * @param id      The appointment ID.
     *
     * @return rowsAffected The number of rows affected.
     */
    public static int deleteAppointment(int id) throws SQLException {

        String sql = "DELETE FROM APPOINTMENTS WHERE Appointment_ID = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setInt(1, id);
        int rowsAffected = ps.executeUpdate();

        return rowsAffected;
    }

    /**Query database for all appointments and add to AppointmentList.*/
    public static void selectAllAppointments() throws SQLException {
        String sql = "SELECT * FROM APPOINTMENTS";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            LocalDateTime utcLDT = rs.getTimestamp("Start").toLocalDateTime();
            ZonedDateTime sZDT = utcLDT.atZone(ZoneId.of("UTC"));
            ZonedDateTime startConverted = sZDT.withZoneSameInstant(ZoneId.of(ZoneId.systemDefault().toString()));
            Timestamp startStamp = Timestamp.valueOf(startConverted.toLocalDateTime());
            LocalDateTime eutcLDT = rs.getTimestamp("End").toLocalDateTime();
            ZonedDateTime eZDT = eutcLDT.atZone(ZoneId.of("UTC"));
            ZonedDateTime endConverted = eZDT.withZoneSameInstant(ZoneId.of(ZoneId.systemDefault().toString()));
            Timestamp endStamp = Timestamp.valueOf(endConverted.toLocalDateTime());

            Appointment a = new Appointment(rs.getInt("Appointment_ID"), rs.getString("Title"),
                    rs.getString("Description"), rs.getString("Location"),
                    rs.getString("Type"), startStamp, endStamp,
                    rs.getInt("Customer_ID"), rs.getInt("User_ID"),
                    rs.getInt("Contact_ID"));
            AppointmentList.addAppointment(a);
        }
    }
}
