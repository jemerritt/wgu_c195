package model;


import helper.AppointmentQuery;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.SQLException;

/**Model for Appointment List.
 *
 * @author Joseph Merritt
 * */
public class AppointmentList {

    private static ObservableList<Appointment> allAppointments = FXCollections.observableArrayList();

    /**
     * @param appointment adds the appointment to allAppointments list.
     * */
    public static void addAppointment(Appointment appointment){
        allAppointments.add(appointment);
    }

    /**Removes selected appointment from allAppointments list.
     * @param selectedAppointment the appointment to remove from allAppointments list
     * @return true if selectedAppointment is not null or false if null
     * */
    public static boolean deleteAppointment(Appointment selectedAppointment) throws SQLException {
        if (selectedAppointment != null){
            AppointmentQuery.deleteAppointment(selectedAppointment.getId());
            allAppointments.remove(selectedAppointment);
            return true;
        }
        else
            return false;
    }

    /**Search for appointment by id.
     * @param appointmentId the appointmentId to find in allAppointments list
     * @return the appointment from allAppointments list with corresponding id or null if appointmentId does not exist
     * */
    public static Appointment lookupAppointment(int appointmentId) {
        for(Appointment p : allAppointments){
            if(p.getId() == appointmentId){
                return p;
            }
        }
        return null;
    }

    /**Updates selected appointment in allAppointments list.
     * @param index the index for updated appointment
     * @param selectedAppointment the appointment to update
     * */
    public static void updateAppointment(int index, Appointment selectedAppointment){
        allAppointments.set(index,selectedAppointment);
    }

    /**Returns list of all appointments.
     * @return the list of all appointments.
     * */
    public static ObservableList<Appointment> getAllAppointments() throws SQLException {

        clearAllAppointment();
        AppointmentQuery.selectAllAppointments();

        return allAppointments;
    }

    /**Clears allAppointments list.*/
    public static void clearAllAppointment(){allAppointments.clear();}

}
