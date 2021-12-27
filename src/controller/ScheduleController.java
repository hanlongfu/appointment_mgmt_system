package controller;

import database.DbHandler;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ScheduleController implements Initializable {

    @FXML private TableView<Appointment> appointmentTableView;
    @FXML private TableColumn<Appointment, Integer> apptIdCol;
    @FXML private TableColumn<Appointment, Integer> contactIdCol;
    @FXML private TableColumn<Appointment, Integer> customerIdCol;
    @FXML private TableColumn<Appointment, String> descriptionCol;
    @FXML private TableColumn<Appointment, String> startCol;
    @FXML private TableColumn<Appointment, String> endCol;
    @FXML private TableColumn<Appointment, String> locationCol;
    @FXML private TableColumn<Appointment, String> titleCol;
    @FXML private TableColumn<Appointment, String> typeCol;

    @FXML private RadioButton all;
    @FXML private RadioButton thisMonth;
    @FXML private RadioButton thisWeek;

    @FXML private Label userIdText;

    private DbHandler dbHandler;

    /**
     * This method initializes the schedule controller class
     * @param rb ResourceBundle
     * @param url url
     * */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        dbHandler = new DbHandler();

        apptIdCol.setCellValueFactory(cellData -> cellData.getValue().IdProperty().asObject());
        titleCol.setCellValueFactory(cellData -> cellData.getValue().titleProperty());
        descriptionCol.setCellValueFactory(cellData -> cellData.getValue().descriptionProperty());
        locationCol.setCellValueFactory(cellData -> cellData.getValue().locationProperty());
        contactIdCol.setCellValueFactory(cellData -> cellData.getValue().contactIdProperty().asObject());
        typeCol.setCellValueFactory(cellData -> cellData.getValue().typeProperty());
        startCol.setCellValueFactory(cellData -> cellData.getValue().startTimeProperty());
        endCol.setCellValueFactory(cellData -> cellData.getValue().endTimeProperty());
        customerIdCol.setCellValueFactory(cellData -> cellData.getValue().customerIdProperty().asObject());

        ToggleGroup toggleGroup = new ToggleGroup();
        all.setToggleGroup(toggleGroup);
        all.setSelected(true);
        thisWeek.setToggleGroup(toggleGroup);
        thisMonth.setToggleGroup(toggleGroup);

        try{
            ObservableList<Appointment> appointments = dbHandler.getAllAppointments();
            appointmentTableView.setItems(appointments);
        }catch(SQLException | ClassNotFoundException e){
            Logger.getLogger(ScheduleController.class.getName()).log(Level.SEVERE, null, e);
        }

        thisWeek.selectedProperty().addListener(new ChangeListener<Boolean>() {
            public void changed(ObservableValue<? extends Boolean> item, Boolean oldItem, Boolean newItem) {
                if (newItem) {
                    try {
                        ObservableList<Appointment> appointments = dbHandler.getAllAppointments7Days();
                        appointmentTableView.setItems(appointments);
                    } catch (ClassNotFoundException | SQLException e) {
                        Logger.getLogger(ScheduleController.class.getName()).log(Level.SEVERE, null, e);
                    }
                }
            }
        });

        thisMonth.selectedProperty().addListener(new ChangeListener<Boolean>() {
            public void changed(ObservableValue<? extends Boolean> item, Boolean oldItem, Boolean newItem) {
                if (newItem) {
                    try {
                        ObservableList<Appointment> appointments = dbHandler.getAllAppointments30Days();
                        appointmentTableView.setItems(appointments);
                    }  catch (ClassNotFoundException | SQLException e) {
                        Logger.getLogger(ScheduleController.class.getName()).log(Level.SEVERE, null, e);
                    }
                }
            }
        });

        all.selectedProperty().addListener(new ChangeListener<Boolean>() {
            public void changed(ObservableValue<? extends Boolean> item, Boolean oldItem, Boolean newItem) {
                if (newItem) {
                    try {
                        ObservableList<Appointment> appointments = dbHandler.getAllAppointments();
                        appointmentTableView.setItems(appointments);
                    } catch (ClassNotFoundException | SQLException e) {
                        Logger.getLogger(ScheduleController.class.getName()).log(Level.SEVERE, null, e);
                    }
                }
            }
        });

    }

    /**
     * This method controls the behaviors after the button to go back is clicked
     * @param event the mouse event
     **/
    @FXML
    void handleBackBtn(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/customer.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            CustomerController controller = loader.getController();

            int userId = Integer.parseInt(userIdText.getText().split(" ")[2]);
            controller.getUserId(userId);

            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    /**
     * This method set the user id
     * @param id  user id to be set
     * */
    public void getUserIdText(int id) {
        userIdText.setText("User ID: " + id);
    }

}
