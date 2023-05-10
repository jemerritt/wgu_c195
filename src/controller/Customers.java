package controller;

import helper.JDBC;
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
import model.Customer;
import model.CustomerList;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;


/**Customers class provides control for the Customers screen.
 *
 * @author Joseph Merritt
 * */
public class Customers implements Initializable {

    public ImageView exitButton;
    public ImageView minButton;
    public ImageView customerAddB;
    public ImageView customerModB;
    public ImageView customerDelB;
    public TableView<model.Customer> customersTable;
    public TableColumn<model.Customer,Integer> customerIdColumn;
    public TableColumn<model.Customer,String> customerNameColumn;
    public TableColumn<model.Customer,String> customerPhoneColumn;
    public TableColumn<model.Customer,String> customerAddressColumn;
    public TableColumn<model.Customer,String> customerPostalColumn;
    public TableColumn<model.Customer,Integer> customerDivColumn;
    public TextField customerSearch;
    public Label customerSearchError;
    public Label cLabel;
    public Label aLabel;
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
        try {
            customersTable.setItems(CustomerList.getAllCustomers());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        customerIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        customerNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        customerPhoneColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));
        customerAddressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));
        customerPostalColumn.setCellValueFactory(new PropertyValueFactory<>("postal"));
        customerDivColumn.setCellValueFactory(new PropertyValueFactory<>("division"));
    }

    /**Initialize static variable for sending customer information between screens.*/
    private static Customer selectedCustomer = null;

    /**
     * @return Selected customer from allcustomers table.
     * */
    public static Customer getSelectedCustomer(){
        return selectedCustomer;
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
    public void customerAddPressed() {
        customerAddB.setImage(addPressed);
    }

    /**Loads Add customer screen.
     *
     * @param mouseEvent Mouse event action from button release used to get main Stage.
     * @throws IOException
     * */
    public void customerAddReleased(MouseEvent mouseEvent) throws IOException {
        customerAddB.setImage(add);
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/AddCustomer.fxml")));
        Stage stage = (Stage)((Node)mouseEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 960, 720);
        root.setOnMousePressed(pressEvent -> root.setOnMouseDragged(dragEvent -> {
            stage.setX(dragEvent.getScreenX() - pressEvent.getSceneX());
            stage.setY(dragEvent.getScreenY() - pressEvent.getSceneY());
        }));
        stage.setTitle("Add customer");
        scene.getStylesheets().add("/style/style.css");
        scene.setFill(Color.TRANSPARENT);
        stage.setScene(scene);
        stage.show();
    }

    /**Controls custom button highlighting behavior on mouse press.*/
    public void customerModPressed() {
        customerModB.setImage(modPressed);
    }

    /**Loads Modify customer screen.
     *
     * @param mouseEvent Mouse event action from button release used to get main Stage.
     * @throws IOException
     * */
    public void customerModReleased(MouseEvent mouseEvent) throws IOException {
        customerModB.setImage(mod);
        selectedCustomer = customersTable.getSelectionModel().getSelectedItem();

        if (selectedCustomer == null)
            return;

        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/ModifyCustomer.fxml")));
        Stage stage = (Stage)((Node)mouseEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 960, 720);
        root.setOnMousePressed(pressEvent -> root.setOnMouseDragged(dragEvent -> {
            stage.setX(dragEvent.getScreenX() - pressEvent.getSceneX());
            stage.setY(dragEvent.getScreenY() - pressEvent.getSceneY());
        }));
        stage.setTitle("Modify customer");
        scene.getStylesheets().add("/style/style.css");
        scene.setFill(Color.TRANSPARENT);
        stage.setScene(scene);
        stage.show();
    }

    /**Controls custom button highlighting behavior on mouse press.*/
    public void customerDelPressed() {
        customerDelB.setImage(delPressed);
    }

    /**Prompts user for delete confirmation. Successful confirmation removes selected customer.*/
    public void customerDelReleased() throws SQLException {
        customerDelB.setImage(del);
        selectedCustomer = customersTable.getSelectionModel().getSelectedItem();

        if (selectedCustomer == null)
            return;

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION,"Are you sure you want to delete " +
                selectedCustomer.getName() + "?");
        Optional<ButtonType> result = alert.showAndWait();
        if(result.isPresent() && result.get() == ButtonType.OK){
            if(!CustomerList.deleteCustomer(selectedCustomer)){
                Alert deleteAlert = new Alert(Alert.AlertType.ERROR);
                deleteAlert.setTitle("Error Dialog");
                deleteAlert.setContentText("No customer found.");
                deleteAlert.showAndWait();
            }
        }
    }

    /**Filter all customers table using text entered.*/
    public void customerSearchAction() throws SQLException {
        String q = customerSearch.getText();

        ObservableList<Customer> customers = CustomerList.lookupCustomer(q);

        if (customers.size() == 0) {
            try {
                int id = Integer.parseInt(q);
                Customer p = CustomerList.lookupCustomer(id);
                if (p != null)
                    customers.add(p);
            }
            catch (NumberFormatException e)
            {
                //ignore
            }
        }

        if (customers.size() == 0) {
            customerSearchError.setText("No Matching customers Found");
        }
        else {
            customerSearchError.setText("");
        }

        customersTable.setItems(customers);
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
        Scene scene = new Scene(root, 960, 720);
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


