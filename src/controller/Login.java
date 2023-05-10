package controller;

import helper.JDBC;
import helper.TimeHelper;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import model.Appointment;
import model.AppointmentList;
import model.User;
import model.UserList;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Locale;
import java.util.Objects;
import java.util.ResourceBundle;

/**Login Class provides control for the Login screen.
 *
 * @author Joseph Merritt
 * */
public class Login implements Initializable {

    public static User currentUser = null;
    public ImageView exitButton;
    public ImageView minButton;
    public TextField userNameField;
    public PasswordField passwordField;
    public Label userNameLabel;
    public Label passwordLabel;
    public Label locationLabel;
    public Button loginButton;
    public Label loginLabel;
    public Label titleLabel;
    Image exitPressed = new Image("img/exit_pressed.png");
    Image minPressed = new Image("img/min_pressed.png");
    Image min = new Image("img/min.png");
    String userName;
    String password;
    Locale currentLocale = Locale.getDefault();


    /**
     * Initializes controller and displays labels in either english on french based on system language.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        TimeHelper.setUserTime();
        try {
            UserList.getAllUsers();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        System.out.println(currentLocale.getLanguage());
        ZoneId zid = ZoneId.systemDefault();
        locationLabel.setText(zid.toString());
        if (Objects.equals(currentLocale.getLanguage(), "fr")) {
            titleLabel.setText("Système de planification");
            loginLabel.setText("Connexion");
            userNameLabel.setText("Nom d'utilisateur:");
            passwordLabel.setText("le mot de passe:");
            loginButton.setText("Connexion");
        } else if (Objects.equals(currentLocale.getLanguage(), "en")) {
            titleLabel.setText("Scheduling System");
            loginLabel.setText("Login");
            userNameLabel.setText("Username:");
            passwordLabel.setText("Password:");
            loginButton.setText("Login");
        }
    }

    /**
     * Controls custom button highlighting behavior on mouse press.
     */
    public void exitPressed() {
        exitButton.setImage(exitPressed);
    }

    /**
     * Exits program on exit button mouse release.
     */
    public void exitReleased() {
        JDBC.closeConnection();
        System.exit(0);
    }

    /**
     * Controls custom button highlighting behavior on mouse press.
     */
    public void minPressed() {
        minButton.setImage(minPressed);
    }

    /**
     * Minimize program on minimize button mouse release.
     */
    public void minReleased() {
        minButton.setImage(min);
        Stage stage = (Stage) minButton.getScene().getWindow();
        stage.setIconified(true);
    }

    /**
     * Transfers user to Appointments page.
     * User password and username are checked against the database. User is taken to appointments page if
     * check is successful. User is presented with a pop-up displaying information about appointments within
     * the next 15 minutes. If check is unsuccessful an appropriate error message is displayed in either
     * English or French depending on system language using a Lambda function for clarity.
     */
    public void onLoginButton(javafx.scene.input.MouseEvent mouseEvent) throws SQLException, IOException {
        userName = userNameField.getText();
        password = passwordField.getText();

        for (User u : UserList.getAllUsers()) {
            if (Objects.equals(u.getUserName(), userName) && Objects.equals(u.getPassword(), password)) {
                currentUser = u;
                BufferedWriter loginLog = new BufferedWriter(new FileWriter("login_activity.txt", true));
                loginLog.append(String.valueOf(ZonedDateTime.now(ZoneOffset.UTC))).append(" UTC - User: ").append(currentUser.getUserName()).append(" Logged in Successfully").append("\n");
                loginLog.flush();
                loginLog.close();
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

                try {
                    LocalDateTime now = LocalDateTime.now();
                    Timestamp aptCheck = Timestamp.valueOf(now.plusMinutes(16));
                    boolean noApt = true;
                    for(Appointment a : AppointmentList.getAllAppointments()){
                        if( (Login.currentUser.getId() == a.getUserId()) && a.getStart().before(aptCheck) &&
                                a.getStart().after(Timestamp.valueOf(now))){
                            noApt = false;
                            Alert aptAlert = new Alert(Alert.AlertType.INFORMATION);
                            aptAlert.setTitle("Upcoming Appointment");
                            aptAlert.setContentText("Appointment ID: " + a.getId() + " - " +
                                    a.getStart());
                            aptAlert.showAndWait();
                        }
                    }
                    if(noApt){
                        Alert aptAlert = new Alert(Alert.AlertType.INFORMATION);
                        aptAlert.setTitle("No Upcoming Appointments");
                        aptAlert.setContentText("No Appointments Scheduled Within 15 Minutes.");
                        aptAlert.showAndWait();
                    }
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }

                return;
            }
        }

        if (Objects.equals(currentLocale.getLanguage(), "fr")) {
            LoginError frenchError = () -> {
                Alert failedLoginAlert = new Alert(Alert.AlertType.ERROR);
                failedLoginAlert.setTitle("Échec de la connexion");
                failedLoginAlert.setContentText("Identifiant ou mot de passe incorrect");
                failedLoginAlert.showAndWait();
            };
            frenchError.error();
        } else if (Objects.equals(currentLocale.getLanguage(), "en")) {
            LoginError englishError = () -> {
                Alert failedLoginAlert = new Alert(Alert.AlertType.ERROR);
                failedLoginAlert.setTitle("Login Failed");
                failedLoginAlert.setContentText("Incorrect Username or Password");
                failedLoginAlert.showAndWait();
            };
            englishError.error();
        }

        BufferedWriter loginLog = new BufferedWriter(new FileWriter("login_activity.txt", true));
        loginLog.append(String.valueOf(ZonedDateTime.now(ZoneOffset.UTC))).append(" UTC - User: ").append(userNameField.getText()).append(" Failed Login Attempt").append("\n");
        loginLog.flush();
        loginLog.close();

    }

    /**Interface for Error Lambda functions.*/
    private interface LoginError {
        void error();
    }
}
