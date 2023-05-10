package controller;

import helper.CustomerQuery;
import helper.DivisionQuery;
import helper.JDBC;
import javafx.event.ActionEvent;
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
import java.util.Objects;
import java.util.ResourceBundle;


/**AddCustomer Class provides control for the Add Customer form.
 *
 * @author Joseph Merritt
 * */
public class AddCustomer implements Initializable {

    public ImageView exitButton;
    public ImageView minButton;
    public ImageView saveB;
    public ImageView cancelB;
    public TextField name;
    public TextField phone;
    public TextField address;
    public TextField postal;
    public ComboBox<Country> countryComboBox;
    public ComboBox<Division> divisionComboBox;
    Image exitPressed = new Image("img/exit_pressed.png");
    Image minPressed = new Image("img/min_pressed.png");
    Image min = new Image("img/min.png");
    Image save = new Image("img/save.png");
    Image savePressed = new Image("img/save_pressed.png");
    Image cancel = new Image("img/cancel.png");
    Image cancelPressed = new Image("img/cancel_pressed.png");

    private static Country selectedCountry = null;


    /**Initializes controller. Populates Country and division combo boxes.*/
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            countryComboBox.setItems(CountryList.getAllCountries());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        countryComboBox.getSelectionModel().selectFirst();
        selectedCountry = countryComboBox.getSelectionModel().getSelectedItem();

        try {
            DivisionQuery.selectRelatedDivisions(selectedCountry.getId());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        divisionComboBox.setItems(DivisionList.getAllDivisions());
        divisionComboBox.getSelectionModel().selectFirst();
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

    /**Add new customer to database on a successful save and loads the main screen.
     * Text validation is performed on text field inputs.
     *
     * @param mouseEvent Mouse event action from button release used to get main Stage.
     * @throws IOException
     * */
    public void saveReleased(MouseEvent mouseEvent) throws IOException, SQLException {
        saveB.setImage(save);

        int divId = divisionComboBox.getSelectionModel().getSelectedItem().getId();

        if(name.getText().isBlank() || phone.getText().isBlank() || address.getText().isBlank() || postal.getText().isBlank()) {
            Alert failedLoginAlert = new Alert(Alert.AlertType.ERROR);
            failedLoginAlert.setTitle("Invalid Customer");
            failedLoginAlert.setContentText("Invalid Customer. Please fill out all fields.");
            failedLoginAlert.showAndWait();
        }else {
            CustomerQuery.insertCustomer(name.getText(), phone.getText(), address.getText(), postal.getText(), divId);
        }

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

    /**Changes contents of Division combo box based on Country combo box.*/
    public void onCountry() {
        selectedCountry = countryComboBox.getSelectionModel().getSelectedItem();
        if (selectedCountry != null) {
            try {
                DivisionQuery.selectRelatedDivisions(selectedCountry.getId());
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            divisionComboBox.setItems(DivisionList.getAllDivisions());
            divisionComboBox.getSelectionModel().selectFirst();
        }
    }

}


