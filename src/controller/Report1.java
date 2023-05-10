package controller;

import helper.JDBC;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import model.Appointment;
import model.AppointmentList;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.*;

/**Report1 Class provides control for the Report1 screen.
 *
 * @author Joseph Merritt
 * */
public class Report1 implements Initializable {

    public ImageView exitButton;
    public ImageView minButton;
    public Label appointmentSearchError;
    public Label cLabel;
    public Label aLabel;
    public TextArea typeArea;
    public TextArea monthArea;
    Image exitPressed = new Image("img/exit_pressed.png");
    Image minPressed = new Image("img/min_pressed.png");
    Image min = new Image("img/min.png");
    List<String> typeList = new ArrayList<String>();
    List<String> monthList = new ArrayList<String>();

    /**Initializes controller. Prints total number of appointments by type and by month.
     * Uses Lambda to functionally print type and month information.*/
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        try {
            for (Appointment a : AppointmentList.getAllAppointments()) {
                if (!containsType(a.getType())) {
                    typeList.add(a.getType());
                }
                if (!containsMonth(a.getMonth())) {
                    monthList.add(a.getMonth());
                }
            }
        }catch(SQLException e){
                throw new RuntimeException(e);
        }

        for (String value : typeList) {
            System.out.println(value);
        }
        for (String value : monthList) {
            System.out.println(value);
        }

        typeList.forEach(types->{
            try {
                typeArea.appendText(types + ": " + countTypes(types) + "\n");
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
        monthList.forEach(months->{
            try {
                monthArea.appendText(months + ": " + countMonths(months) + "\n");
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }

    /**Checks to see if the list already contains the type.
     *
     * @param s The type to check.
     * */
    private boolean containsType(String s) {
        for (String value : typeList) {
            if (Objects.equals(s, value)) {
                return true;
            }
        }

        return false;
    }

    /**Counts the number of times the type appears in the database.
     *
     * @param type The type to count.
     * */
    private int countTypes(String type) throws SQLException{
        int count = 0;
        for(Appointment a : AppointmentList.getAllAppointments()){
            if(Objects.equals(a.getType(), type)){
                count++;
            }
        }
        return count;
    }

    /**Checks to see if the list already contains the month.
     *
     * @param s The month to check.
     * */
    private boolean containsMonth(String s) {
        for (String value : monthList) {
            if (Objects.equals(s, value)) {
                return true;
            }
        }

        return false;
    }

    /**Counts the number of times the month appears in the database.
     *
     * @param month The type to count.
     * */
    private int countMonths(String month) throws SQLException{
        int count = 0;
        for(Appointment a : AppointmentList.getAllAppointments()){
            if(Objects.equals(a.getMonth(), month)){
                count++;
            }
        }
        return count;
    }

    /**Controls custom button highlighting behavior on mouse press.*/
    public void exitPressed() {
        exitButton.setImage(exitPressed);
    }

    /**Exits program on exit button mouse release.*/
    public void exitReleased() {
        JDBC.closeConnection();
        System.exit(0);
    }

    /**Controls custom button highlighting behavior on mouse press.*/
    public void minPressed() {
        minButton.setImage(minPressed);
    }

    /**Minimize program on minimize button mouse release.*/
    public void minReleased() {
        minButton.setImage(min);
        Stage stage = (Stage) minButton.getScene().getWindow();
        stage.setIconified(true);
    }

    /**Controls link highlighting behavior on mouse hover.*/
    public void cLabelHover() {
        cLabel.setTextFill(Color.GRAY);
    }

    /**Controls link highlighting behavior on mouse hover.*/
    public void cLabelHoverExit() {
        cLabel.setTextFill(Color.WHITE);
    }

    /**Controls custom label highlighting behavior on mouse press.*/
    public void cLabelClick() {
        cLabel.setTextFill(Color.BLUE);
    }

    /**Opens the Customers screen on mouse release.*/
    public void cLabelClickRelease(MouseEvent mouseEvent) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/Customers.fxml")));
        Stage stage = (Stage)((Node)mouseEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 1200, 900);
        root.setOnMousePressed(pressEvent -> root.setOnMouseDragged(dragEvent -> {
            stage.setX(dragEvent.getScreenX() - pressEvent.getSceneX());
            stage.setY(dragEvent.getScreenY() - pressEvent.getSceneY());
        }));
        stage.setTitle("Scheduling System");
        scene.getStylesheets().add("/style/style.css");
        scene.setFill(Color.TRANSPARENT);
        stage.setScene(scene);
        stage.show();
    }

    /**Controls link highlighting behavior on mouse hover.*/
    public void aLabelHover() {
        aLabel.setTextFill(Color.GRAY);
    }

    /**Controls link highlighting behavior on mouse hover.*/
    public void aLabelHoverExit() {
        aLabel.setTextFill(Color.WHITE);
    }

    /**Controls custom label highlighting behavior on mouse press.*/
    public void aLabelClick() {
        aLabel.setTextFill(Color.BLUE);
    }

    /**Opens the Appointments screen on mouse release.*/
    public void aLabelClickRelease(MouseEvent mouseEvent) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/Appointments.fxml")));
        Stage stage = (Stage)((Node)mouseEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 1200, 900);
        root.setOnMousePressed(pressEvent -> root.setOnMouseDragged(dragEvent -> {
            stage.setX(dragEvent.getScreenX() - pressEvent.getSceneX());
            stage.setY(dragEvent.getScreenY() - pressEvent.getSceneY());
        }));
        stage.setTitle("Scheduling System");
        scene.getStylesheets().add("/style/style.css");
        scene.setFill(Color.TRANSPARENT);
        stage.setScene(scene);
        stage.show();
    }

}


