package controller;

import helper.JDBC;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import model.Appointment;
import model.AppointmentList;
import model.Contact;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.ResourceBundle;

/**Ims Class provides control for the main Scheduling screen.
 *
 * @author Joseph Merritt
 * */
public class Report3 implements Initializable {

    public ImageView exitButton;
    public ImageView minButton;
    public TableView<Appointment> appointmentsTable;
    public TableColumn<Appointment,Integer> appointmentIdColumn;
    public TableColumn<Appointment,String> appointmentTitleColumn;
    public TableColumn<Appointment,String> appointmentDescColumn;
    public TableColumn<Appointment,String> appointmentLocationColumn;
    public TableColumn<Appointment,String> appointmentContactColumn;
    public TableColumn<Appointment,Integer> appointmentTypeColumn;
    public TableColumn<Appointment, Timestamp> appointmentStartColumn;
    public TableColumn<Appointment, Timestamp> appointmentEndColumn;
    public TableColumn<Appointment,Integer> appointmentCustomerIdColumn;
    public TableColumn<Appointment,Integer> appointmentUserIdColumn;
    public Label appointmentSearchError;
    public Label cLabel;
    public Label aLabel;
    Image exitPressed = new Image("img/exit_pressed.png");
    Image minPressed = new Image("img/min_pressed.png");
    Image min = new Image("img/min.png");
    ObservableList<Appointment> longAppointments = FXCollections.observableArrayList();

    /**Initializes controller and sets initial table contents. Table will be populated with
     * all appointments from database longer than one hour.*/
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        try {
            for(Appointment a : AppointmentList.getAllAppointments()){
                LocalDateTime sLDT = a.getStart().toLocalDateTime();
                LocalDateTime eLDT = a.getEnd().toLocalDateTime();
                if(sLDT.plusHours(1).isBefore(eLDT)){
                    longAppointments.add(a);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        appointmentsTable.setItems(longAppointments);

        appointmentIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        appointmentTitleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        appointmentDescColumn.setCellValueFactory(new PropertyValueFactory<>("desc"));
        appointmentLocationColumn.setCellValueFactory(new PropertyValueFactory<>("location"));
        appointmentTypeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        appointmentStartColumn.setCellValueFactory(new PropertyValueFactory<>("start"));
        appointmentEndColumn.setCellValueFactory(new PropertyValueFactory<>("end"));
        appointmentContactColumn.setCellValueFactory(new PropertyValueFactory<>("contactName"));
        appointmentCustomerIdColumn.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        appointmentUserIdColumn.setCellValueFactory(new PropertyValueFactory<>("userId"));
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


