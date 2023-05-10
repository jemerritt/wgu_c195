package controller;

import helper.*;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
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
import java.time.*;
import java.util.Objects;
import java.util.ResourceBundle;


/**AddAppointment Class provides control for the Add Appointment form.
 *
 * @author Joseph Merritt
 * */
public class AddAppointment implements Initializable {

    public ImageView exitButton;
    public ImageView minButton;
    public ImageView saveB;
    public ImageView cancelB;
    public ComboBox<LocalTime> endComboBox;
    public ComboBox<LocalTime> startComboBox;
    public ComboBox<Contact> contactComboBox;
    public DatePicker date = null;
    public TextArea desc;
    public TextField title;
    public TextField location;
    public TextField type;
    public ComboBox<Customer> customerComboBox;
    Image exitPressed = new Image("img/exit_pressed.png");
    Image minPressed = new Image("img/min_pressed.png");
    Image min = new Image("img/min.png");
    Image save = new Image("img/save.png");
    Image savePressed = new Image("img/save_pressed.png");
    Image cancel = new Image("img/cancel.png");
    Image cancelPressed = new Image("img/cancel_pressed.png");
    LocalTime open = TimeHelper.getOpen();
    LocalTime close = TimeHelper.getClose();


    /**Initializes controller. Populates time, contact and customer Comboboxes.*/
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        date.setValue(LocalDate.now());
        while(open.isBefore(close.plusSeconds(1))){
            startComboBox.getItems().add(open);
            endComboBox.getItems().add(open.plusMinutes(15));
            open = open.plusMinutes(15);
        }
        startComboBox.getSelectionModel().selectFirst();
        endComboBox.getSelectionModel().selectFirst();

        try {
            contactComboBox.setItems(ContactList.getAllContacts());
            customerComboBox.setItems(CustomerList.getAllCustomers());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        contactComboBox.getSelectionModel().selectFirst();
        customerComboBox.getSelectionModel().selectFirst();
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
    public void savePressed() {
        saveB.setImage(savePressed);
    }

    /**Add new Appointment to database on a successful save and loads the Appointments screen.
     * Text validation is performed on text field inputs.
     *
     * @param mouseEvent Mouse event action from button release used to get main Stage.
     * @throws IOException
     */
    public void saveReleased(MouseEvent mouseEvent) throws IOException, SQLException {
        saveB.setImage(save);

        LocalDate selectedDate = date.getValue();
        LocalDateTime startLDT = LocalDateTime.of(selectedDate, startComboBox.getSelectionModel().getSelectedItem());
        Timestamp checkStart = Timestamp.valueOf(startLDT.plusSeconds(1));
        ZonedDateTime sZDT = startLDT.atZone(ZoneId.of(ZoneId.systemDefault().toString()));
        ZonedDateTime startConverted = sZDT.withZoneSameInstant(ZoneId.of("UTC"));
        Timestamp startStamp = Timestamp.valueOf(startConverted.toLocalDateTime());

        LocalDateTime endLDT = LocalDateTime.of(selectedDate, endComboBox.getSelectionModel().getSelectedItem());
        Timestamp checkEnd = Timestamp.valueOf(endLDT.minusSeconds(1));
        ZonedDateTime eZDT = endLDT.atZone(ZoneId.of(ZoneId.systemDefault().toString()));
        ZonedDateTime endConverted = eZDT.withZoneSameInstant(ZoneId.of("UTC"));
        Timestamp endStamp = Timestamp.valueOf(endConverted.toLocalDateTime());

        int cId = contactComboBox.getSelectionModel().getSelectedItem().getId();
        boolean valid = true;

        if(title.getText().isBlank() || desc.getText().isBlank() || location.getText().isBlank() || type.getText().isBlank()){
            Alert failedLoginAlert = new Alert(Alert.AlertType.ERROR);
            failedLoginAlert.setTitle("Invalid Appointment");
            failedLoginAlert.setContentText("Invalid Appointment. Please fill out all fields.");
            failedLoginAlert.showAndWait();
            valid = false;
            contactComboBox.getSelectionModel().selectFirst();
        } else {
            for (Appointment a : AppointmentList.getAllAppointments()) {
                if (a.getCustomerId() == customerComboBox.getSelectionModel().getSelectedItem().getId()) {
                    if (
                                    ((checkStart.after(a.getStart())) && (checkStart.before(a.getEnd()))) ||
                                    ((checkEnd.after(a.getStart()))   && (checkEnd.before(a.getEnd())))
                    ) {
                        System.out.println("Overlap error");
                        Alert failedLoginAlert = new Alert(Alert.AlertType.ERROR);
                        failedLoginAlert.setTitle("Invalid Appointment");
                        failedLoginAlert.setContentText("Invalid Appointment Due to Customer Overlap");
                        failedLoginAlert.showAndWait();
                        valid = false;
                        contactComboBox.getSelectionModel().selectFirst();
                        break;
                    }
                }
            }
        }

        if(valid){
            AppointmentQuery.insertAppointment(title.getText(), desc.getText(), location.getText(), type.getText(),
                    startStamp, endStamp, customerComboBox.getSelectionModel().getSelectedItem().getId(), Login.currentUser.getId(),
                    cId);

            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/Appointments.fxml")));
            Stage stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
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

    /**Controls custom button highlighting behavior on mouse press.*/
    public void cancelPressed() {
        cancelB.setImage(cancelPressed);
    }

    /**Cancels input and returns to main screen without saving changes.
     *
     * @param mouseEvent Mouse event action from button release used to get main Stage.
     * @throws IOException
     * */
    public void cancelReleased(MouseEvent mouseEvent) throws IOException {
        cancelB.setImage(cancel);

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

    /**Updates End time combo box with appropriate times when Start time is changed.*/
    public void onStart() {
        LocalTime selectedStart = startComboBox.getSelectionModel().getSelectedItem();
            if (selectedStart != null) {
                endComboBox.getSelectionModel().clearSelection();
                endComboBox.getItems().clear();

                LocalTime i = selectedStart;
                while(i.isBefore(close.plusSeconds(1))){
                    endComboBox.getItems().add(i.plusMinutes(15));
                    i = i.plusMinutes(15);
                }
                endComboBox.getSelectionModel().selectFirst();
            }
    }
}



