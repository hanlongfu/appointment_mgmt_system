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
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;
import model.Customer;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ReportController implements Initializable {

    @FXML private RadioButton apptsByMonth;
    @FXML private RadioButton apptsByType;
    @FXML private RadioButton scheduleByContact;
    @FXML private RadioButton customersAdded7Days;

    @FXML private TextArea textArea;
    @FXML private Label userIdText;

    private DbHandler dbHandler;

    /**
     * This method initializes the report controller class
     * @param rb ResourceBundle
     * @param url url
     * */
    public void initialize(URL url, ResourceBundle rb) {

        dbHandler = new DbHandler();

        ToggleGroup group = new ToggleGroup();
        apptsByType.setToggleGroup(group);
        apptsByType.setSelected(true);
        apptsByMonth.setToggleGroup(group);
        scheduleByContact.setToggleGroup(group);
        customersAdded7Days.setToggleGroup(group);

        tallyApptByType();


        /*
         * This event listener controls when the radio button to show appointments by type is clicked
         * */
        apptsByType.selectedProperty().addListener(new ChangeListener<Boolean>() {
            public void changed(ObservableValue<? extends Boolean> item, Boolean oldItem, Boolean newItem) {
                if (newItem) {
                    textArea.setText("");
                    tallyApptByType();
                }
            }
        });

        /*
         * This event listener controls when the radio button to show appointments by month is clicked
         * */
        apptsByMonth.selectedProperty().addListener(new ChangeListener<Boolean>() {
            public void changed(ObservableValue<? extends Boolean> item, Boolean oldItem, Boolean newItem) {
                if (newItem) {
                    textArea.setText("");
                    try {
                        String result = dbHandler.getAllAppointmentsByMonth();
                        textArea.setText(result);
                    } catch (ClassNotFoundException | SQLException e) {
                        Logger.getLogger(ReportController.class.getName()).log(Level.SEVERE, null, e);
                    }
                }
            }
        });

        /*
         * This event listener controls when the radio button to show schedules by contact is clicked
         * */
        scheduleByContact.selectedProperty().addListener(new ChangeListener<Boolean>() {
            public void changed(ObservableValue<? extends Boolean> item, Boolean oldItem, Boolean newItem) {
                if (newItem) {
                    textArea.setText("");

                    try {
                        String result = dbHandler.getAllAppointmentsByContact();
                        textArea.setText(result);
                    } catch (ClassNotFoundException | SQLException e) {
                        Logger.getLogger(ReportController.class.getName()).log(Level.SEVERE, null, e);
                    }
                }
            }
        });

        /*
         * This event listener controls when the radio button to show customers added last 7 days is clicked
         * */
        customersAdded7Days.selectedProperty().addListener(new ChangeListener<Boolean>() {
            public void changed(ObservableValue<? extends Boolean> item, Boolean oldItem, Boolean newItem) {
                if (newItem) {
                    textArea.setText("");
                    try {
                        ObservableList<Customer> customers = dbHandler.getAllCustomersThisWeek();
                        textArea.setText(Integer.toString(customers.size()));
                    } catch (ClassNotFoundException | SQLException e) {
                        Logger.getLogger(ReportController.class.getName()).log(Level.SEVERE, null, e);
                    }
                }
            }
        });

    }

    /**
     * This method obtain appointments by type from database and output it to the textarea
     * */
    private void tallyApptByType() {
        try {
            String result = dbHandler.getAllAppointmentsByType();
            textArea.setText(result);

        } catch (ClassNotFoundException | SQLException e) {
            Logger.getLogger(ReportController.class.getName()).log(Level.SEVERE, null, e);
        }
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
    public void setUserIdText(int id) {
        userIdText.setText("User ID: " + id);
    }

}
