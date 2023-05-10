package controller;

import helper.JDBC;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
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
import model.*;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;

/**Appointments Class provides control for the main Appointments screen.
 *
 * @author Joseph Merritt
 * */
public class Appointments implements Initializable {

    public ImageView exitButton;
    public ImageView minButton;
    public ImageView appointmentAddB;
    public ImageView appointmentModB;
    public ImageView appointmentDelB;
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
    public Label cLabel;
    public Label aLabel;
    public RadioButton allRadio;
    public RadioButton monthRadio;
    public RadioButton weekRadio;
    public Button reportButton;
    public ToggleGroup report;
    public RadioButton report1;
    public RadioButton report2;
    public RadioButton report3;
    Image exitPressed = new Image("img/exit_pressed.png");
    Image minPressed = new Image("img/min_pressed.png");
    Image min = new Image("img/min.png");
    Image add = new Image("img/add.png");
    Image addPressed = new Image("img/add_pressed.png");
    Image mod = new Image("img/modify.png");
    Image modPressed = new Image("img/modify_pressed.png");
    Image del = new Image("img/delete.png");
    Image delPressed = new Image("img/delete_pressed.png");

    /**Initializes controller and sets initial table contents.*/
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        allRadio.setSelected(true);
        try {
            ContactList.clearAllContacts();
            ContactList.queryContacts();
            CustomerList.getAllCustomers();
            appointmentsTable.setItems(AppointmentList.getAllAppointments());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
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

    /**Initialize static variable for sending appointment information between screens.*/
    private static Appointment selectedAppointment = null;

    /**
     * @return Selected appointment from allappointments table.
     * */
    public static Appointment getSelectedAppointment(){
        return selectedAppointment;
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

    /**Controls custom button highlighting behavior on mouse press.*/
    public void appointmentAddPressed() {
        appointmentAddB.setImage(addPressed);
    }

    /**Loads Add appointment screen.
     *
     * @param mouseEvent Mouse event action from button release used to get main Stage.
     * @throws IOException
     * */
    public void appointmentAddReleased(MouseEvent mouseEvent) throws IOException {
        appointmentAddB.setImage(add);
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/AddAppointment.fxml")));
        Stage stage = (Stage)((Node)mouseEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 960, 720);
        root.setOnMousePressed(pressEvent -> root.setOnMouseDragged(dragEvent -> {
            stage.setX(dragEvent.getScreenX() - pressEvent.getSceneX());
            stage.setY(dragEvent.getScreenY() - pressEvent.getSceneY());
        }));
        stage.setTitle("Add appointment");
        scene.getStylesheets().add("/style/style.css");
        scene.setFill(Color.TRANSPARENT);
        stage.setScene(scene);
        stage.show();
    }

    /**Controls custom button highlighting behavior on mouse press.*/
    public void appointmentModPressed() {
        appointmentModB.setImage(modPressed);
    }

    /**Loads Modify appointment screen.
     *
     * @param mouseEvent Mouse event action from button release used to get main Stage.
     * @throws IOException
     * */
    public void appointmentModReleased(MouseEvent mouseEvent) throws IOException {
        appointmentModB.setImage(mod);
        selectedAppointment = appointmentsTable.getSelectionModel().getSelectedItem();

        if (selectedAppointment == null)
            return;

        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/ModifyAppointment.fxml")));
        Stage stage = (Stage)((Node)mouseEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 900, 720);
        root.setOnMousePressed(pressEvent -> root.setOnMouseDragged(dragEvent -> {
            stage.setX(dragEvent.getScreenX() - pressEvent.getSceneX());
            stage.setY(dragEvent.getScreenY() - pressEvent.getSceneY());
        }));
        stage.setTitle("Modify appointment");
        scene.getStylesheets().add("/style/style.css");
        scene.setFill(Color.TRANSPARENT);
        stage.setScene(scene);
        stage.show();
    }

    /**Controls custom button highlighting behavior on mouse press.*/
    public void appointmentDelPressed() {
        appointmentDelB.setImage(delPressed);
    }

    /**Prompts user for delete confirmation. Successful confirmation removes selected appointment.*/
    public void appointmentDelReleased() throws SQLException {
        appointmentDelB.setImage(del);
        selectedAppointment = appointmentsTable.getSelectionModel().getSelectedItem();

        if (selectedAppointment == null)
            return;

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION,"Are you sure you want to delete " +
                selectedAppointment.getTitle() + "?");
        Optional<ButtonType> result = alert.showAndWait();
        if(result.isPresent() && result.get() == ButtonType.OK){
            if(!AppointmentList.deleteAppointment(selectedAppointment)){
                Alert deleteAlert = new Alert(Alert.AlertType.ERROR);
                deleteAlert.setTitle("Error Dialog");
                deleteAlert.setContentText("No appointment found.");
                deleteAlert.showAndWait();
            }
            else{
                Alert deleteAlert = new Alert(Alert.AlertType.INFORMATION);
                deleteAlert.setTitle("Appointment Cancelled");
                deleteAlert.setContentText("Appointment ID: " + selectedAppointment.getId() + " - " +
                        selectedAppointment.getType() + " was cancelled.");
                deleteAlert.showAndWait();
            }
        }
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
        stage.setTitle("Customer Management System");
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

    /**Filters the Appointment table to show all appointments.*/
    public void allRadioAction() throws SQLException {
        appointmentsTable.setItems(AppointmentList.getAllAppointments());
    }

    /**Filters the Appointment table to show appointments from this month.*/
    public void monthRadioAction() throws SQLException {
        ObservableList<Appointment> monthAptList = FXCollections.observableArrayList();
        ObservableList<Appointment> allApt = AppointmentList.getAllAppointments();
        LocalDateTime mEnd = LocalDateTime.now().plusMonths(1);
        LocalDateTime mStart = LocalDateTime.now().minusMonths(1);
        Timestamp monthEnd = Timestamp.valueOf(mEnd);
        Timestamp monthStart = Timestamp.valueOf(mStart);

        for(Appointment a : allApt){
            if(a.getStart().before(monthEnd) && a.getStart().after(monthStart)){
                monthAptList.add(a);
            }
        }
        appointmentsTable.setItems(monthAptList);
    }

    /**Filters the Appointment table to show appointments from this week.*/
    public void weekRadioAction() throws SQLException {
        ObservableList<Appointment> weekAptList = FXCollections.observableArrayList();
        ObservableList<Appointment> allApt = AppointmentList.getAllAppointments();
        LocalDateTime wEnd = LocalDateTime.now().plusWeeks(1);
        LocalDateTime wStart = LocalDateTime.now().minusWeeks(1);
        Timestamp weekEnd = Timestamp.valueOf(wEnd);
        Timestamp weekStart = Timestamp.valueOf(wStart);

        for(Appointment a : allApt){
            if(a.getStart().before(weekEnd) && a.getStart().after(weekStart)){
                weekAptList.add(a);
            }
        }
        appointmentsTable.setItems(weekAptList);
    }

    /**Opens the appropriate report page based on radio button selection.*/
    public void onReportB(ActionEvent actionEvent) throws IOException {
        if (report1.isSelected()){
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/Report1.fxml")));
            Stage stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
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
        } else if (report2.isSelected()) {
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/Report2.fxml")));
            Stage stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
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
        } else if (report3.isSelected()) {
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/Report3.fxml")));
            Stage stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
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
}


