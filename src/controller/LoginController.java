package controller;

import database.DbHandler;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.Appointment;
import model.User;
import util.Lang;
import util.Location;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class LoginController implements Initializable {

    @FXML private Label credentialsText;
    @FXML private Label errorText;
    @FXML private Label passwordText;
    @FXML private Label titleText;
    @FXML private Label userLocationText;
    @FXML private Label usernameText;
    @FXML private Button loginButton;
    @FXML private TextField passwordTextfield;
    @FXML private TextField usernameTextfield;

    private DbHandler dbHandler;


    /**
     * This method initializes the login controller class
     * @param rb ResourceBundle
     * @param url url
     * */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        String userLocation = Location.getUserLocation();
        userLocationText.setText(userLocation);
        String lang = Lang.getUserLanguage();
        if (lang.equals("fr")) {
            titleText.setText("Utilisateur en Ligne");
            usernameTextfield.setPromptText("Nom d'utilisateur");
            passwordTextfield.setPromptText("Mot de passe");
            loginButton.setText("S'identifier");
            credentialsText.setText("Veuillez vous connecter avec les informations d'identification suivantes:");
            usernameText.setText("Nom d'utilisateur: admin");
            passwordText.setText("Mot de passe: admin");
        }
    }


    /**
     * This method controls the behaviors after the button to log in is clicked
     * @param event the mouse event
     **/
    @FXML
    void handleLogin(ActionEvent event) {
        dbHandler = new DbHandler();
        loginButton.setDisable(true);

        //get username and password input
        String loginText = usernameTextfield.getText().trim();
        String loginPwd = passwordTextfield.getText().trim();

        Logger logging = Logger.getLogger("loginAttempts");

        User user = new User();
        user.setPassword(loginPwd);
        user.setUserName(loginText);

        try{
            if(dbHandler.checkCredentials(user.getUserNameProperty().getValue(), user.getPasswordProperty().getValue())) {
                errorText.setText("");

                FileHandler fileHandler = new FileHandler("login_activity.txt", true);
                logging.addHandler(fileHandler);
                SimpleFormatter simpleFormatter = new SimpleFormatter();
                fileHandler.setFormatter(simpleFormatter);

                String ts = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
                logging.info("user "+ user.getUserNameProperty().getValue() + " successfully logged in on " + ts);

                FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/customer.fxml"));
                Parent root = loader.load();
                Scene scene = new Scene(root);
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

                try{
                    int userId = dbHandler.getUserId(usernameTextfield.getText());
                    CustomerController customerController = loader.getController();
                    customerController.getUserId(userId);
                } catch(SQLException | ClassNotFoundException e){
                    Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, e);
                    throw e;
                }
                stage.setScene(scene);
                stage.show();
                appointmentNotification();
            } else {
                FileHandler fileHandler = new FileHandler("login_activity.txt", true);
                logging.addHandler(fileHandler);
                SimpleFormatter simpleFormatter = new SimpleFormatter();
                fileHandler.setFormatter(simpleFormatter);

                String ts = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
                logging.info( "user " + user.getUserNameProperty().getValue() + " failed to login on " + ts);

                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Invalid credentials", ButtonType.OK);
                alert.showAndWait().filter(c -> c == ButtonType.OK);

                String lang = Lang.getUserLanguage();
                if (lang.equals("fr")) {
                    errorText.setText("Erreur rencontrée avec le nom d'utilisateur et le mot de passe.");
                } else {
                    errorText.setText("Error encountered with the username and password");
                }

            }
        }catch(SQLException | ClassNotFoundException | IOException e){
            Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    /**
     * This method checks whether there is an immediate appointment
     **/
    private void appointmentNotification() throws SQLException, ClassNotFoundException{
        try{
            ObservableList<Appointment> appointments = dbHandler.getAllAppointments15min();
            if(!appointments.isEmpty()) {
                String appointmentText = "";
                int i = 0;
                while(i < appointments.size()) {
                    int appointmentId = appointments.get(i).IdProperty().getValue();
                    String startTime = appointments.get(i).startTimeProperty().getValue();
                    appointmentText = appointmentText + "Appointment " + appointmentId + " begins on " + startTime;
                }
                Alert alert = new Alert(Alert.AlertType.INFORMATION, appointmentText, ButtonType.OK);
                alert.showAndWait().filter(c -> c == ButtonType.OK);
            } else {
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "No immediate appointment", ButtonType.OK);
                alert.showAndWait().filter(c -> c == ButtonType.OK);
            }
        }catch(SQLException | ClassNotFoundException e){
            e.printStackTrace();
            throw e;
        }

    }






}
