package controller;

import database.DbHandler;
import javafx.collections.FXCollections;
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
import model.Contact;
import model.Customer;
import model.Customer_Addon;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * This class controls behaviors for the appointment screen.
 * @author hanlongfu
 *  */
public class AppointmentController implements Initializable {

    @FXML private ComboBox<String> contactComboBox;

    @FXML private Label customerIdText;
    @FXML private Label titleText;
    @FXML private Label userIdText;

    @FXML private Button addAppointmentBtn;
    @FXML private Button deleteAppointmentBtn;
    @FXML private Button updateAppointmentBtn;

    @FXML private DatePicker startDatePicker;
    @FXML private DatePicker endDatePicker;

    @FXML private TextField appointmentIdTextField;
    @FXML private TextField descriptionTextField;
    @FXML private TextField endTimeTextField;
    @FXML private TextField locationTextField;
    @FXML private TextField startTimeTextField;
    @FXML private TextField titleTextField;
    @FXML private TextField typeTextField;

    @FXML private TableView<Appointment> appointmentTableView;
    @FXML private TableColumn<Appointment, Integer> apptIdColumn;
    @FXML private TableColumn<Appointment, Integer> contactIdColumn;
    @FXML private TableColumn<Appointment, Integer> customerIdColumn;
    @FXML private TableColumn<Appointment, String> descriptionColumn;
    @FXML private TableColumn<Appointment, String> locationColumn;
    @FXML private TableColumn<Appointment, String> startColumn;
    @FXML private TableColumn<Appointment, String> endColumn;
    @FXML private TableColumn<Appointment, String> titleColumn;
    @FXML private TableColumn<Appointment, String> typeColumn;

    private DbHandler dbHandler;

    /**
     * This method initializes the appointment controller class
     * @param rb ResourceBundle
     * @param url url
     * */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        dbHandler = new DbHandler();
        int customerId = Customer_Addon.id;

        //set default value for DatePicker
        startDatePicker.setValue(LocalDate.now());
        endDatePicker.setValue(LocalDate.now());

        //optional
        addAppointmentBtn.setDisable(false);
        updateAppointmentBtn.setDisable(true);
        deleteAppointmentBtn.setDisable(true);

        //populate the tableview with data
        apptIdColumn.setCellValueFactory(cellData -> cellData.getValue().IdProperty().asObject());
        titleColumn.setCellValueFactory(cellData -> cellData.getValue().titleProperty());
        descriptionColumn.setCellValueFactory(cellData -> cellData.getValue().descriptionProperty());
        locationColumn.setCellValueFactory(cellData -> cellData.getValue().locationProperty());
        contactIdColumn.setCellValueFactory(cellData -> cellData.getValue().contactIdProperty().asObject());
        typeColumn.setCellValueFactory(cellData -> cellData.getValue().typeProperty());
        startColumn.setCellValueFactory(cellData -> cellData.getValue().startTimeProperty());
        endColumn.setCellValueFactory(cellData -> cellData.getValue().endTimeProperty());
        customerIdColumn.setCellValueFactory(cellData -> cellData.getValue().customerIdProperty().asObject());

        try{
            ObservableList<Appointment> appointments = dbHandler.getAllAppointments4Customer(customerId);
            appointmentTableView.setItems(appointments);
        } catch(SQLException | ClassNotFoundException e){
            Logger.getLogger(AppointmentController.class.getName()).log(Level.SEVERE, null, e);
        }

        //Initialize contacts in contact comboBox
        try{
            ObservableList<Contact> contacts = dbHandler.getAllContacts();
            ObservableList<String> contactNames = FXCollections.observableArrayList();
            contacts.forEach((contact) -> {
               String selectedContact = contact.getNameProperty().getValue();
               contactNames.add(selectedContact);
            });
            contactComboBox.setItems(contactNames);
        } catch(SQLException | ClassNotFoundException err) {
            Logger.getLogger(AppointmentController.class.getName()).log(Level.SEVERE, null, err);
        }

        //when an appointment row is selected
        appointmentTableView.getSelectionModel().selectedItemProperty().addListener((item, oldItem, newItem) ->{
            if(newItem != null) {
                //optional
                addAppointmentBtn.setDisable(true);
                updateAppointmentBtn.setDisable(false);
                deleteAppointmentBtn.setDisable(false);

                //obtain data from selected row of appointment table
                Appointment appointment = appointmentTableView.getSelectionModel().getSelectedItem();
                int appointmentId = appointment.IdProperty().getValue();
                int contactId = appointment.contactIdProperty().getValue();
                String description = appointment.descriptionProperty().getValue();
                String title = appointment.titleProperty().getValue();
                String location = appointment.locationProperty().getValue();
                String type = appointment.typeProperty().getValue();

                DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd H:mm:ss");
                LocalDateTime startTime = LocalDateTime.parse(appointment.startTimeProperty().getValue(), df);
                LocalDateTime endTime = LocalDateTime.parse(appointment.endTimeProperty().getValue(), df);

                //obtain and set contact in combobox
                try{
                    String contact = dbHandler.getNameViaId(contactId);
                    contactComboBox.setValue(contact);
                } catch(SQLException | ClassNotFoundException err) {
                    Logger.getLogger(AppointmentController.class.getName()).log(Level.SEVERE, null, err);
                }

                //obtain and set other column fields
                appointmentIdTextField.setText(Integer.toString(appointmentId));
                descriptionTextField.setText(description);
                titleTextField.setText(title);
                locationTextField.setText(location);
                typeTextField.setText(type);

                //set start and end date
                startDatePicker.setValue(startTime.toLocalDate());
                endDatePicker.setValue(endTime.toLocalDate());

                //set start and end time
                startTimeTextField.setText(startTime.toLocalTime().toString());
                endTimeTextField.setText(endTime.toLocalTime().toString());

            }
        });
    }

    /**
     * This method clears the defined text fields
     **/
    public void clearTxtFields() {
        appointmentIdTextField.clear();
        descriptionTextField.clear();
        titleTextField.clear();
        locationTextField.clear();
        contactComboBox.getSelectionModel().clearSelection();
        typeTextField.clear();
        startDatePicker.getEditor().clear();
        endDatePicker.getEditor().clear();
        startTimeTextField.clear();
        endTimeTextField.clear();
    }

    /**
     * This method set user and customer id
     **/
    public void setUserCustomerId(Customer customer, int userId) {
        titleText.setText("Appointments for " + customer.getNameProperty().getValue());
        userIdText.setText("User ID: " + userId);
        customerIdText.setText("Customer ID: " + customer.getIdProperty().getValue());
    }

    /**
     * This method check whether selected appointment times are within allowed range
     * @param start starting datetime
     * @param end  ending datetime
     * */
    public boolean appointmentTimeCheck(LocalDateTime start, LocalDateTime end) {
        start = ldt_local2EST(start);
        end = ldt_local2EST(end);
        int startHour = start.getHour();
        int endHour = end.getHour();
        int endMin = end.getMinute();

        if(startHour < 22 && startHour >= 8 && endHour >= 8 && endHour <= 22){
            return endHour != 22 || endMin <= 0;
        }
        return false;
    }

    /**
     * This method controls the behaviors after the button to add appointment is clicked
     * @param event the mouse event
     * */
    @FXML
    void handleAddAppointment(ActionEvent event) throws SQLException, ClassNotFoundException {
        try{
            // should the first item be an int instead of String
            String id = appointmentIdTextField.getText();
            String description = descriptionTextField.getText();
            String title = titleTextField.getText();
            String type = typeTextField.getText().toLowerCase();
            String location = locationTextField.getText();
            String contact = contactComboBox.getValue();

            //obtain local date
            LocalDate startDate = startDatePicker.getValue();
            LocalDate endDate = endDatePicker.getValue();

            if(appointmentCheck(title, description, location, contact, type, startTimeTextField.getText(), endTimeTextField.getText(), startDate, endDate)) {
                //obtain local time
                DateTimeFormatter df = DateTimeFormatter.ofPattern("H:mm:ss");
                LocalTime startTime = LocalTime.parse(startTimeTextField.getText()+":00", df);
                LocalTime endTime = LocalTime.parse(endTimeTextField.getText()+":00", df);

                //obtain local date and time in UTC
                LocalDateTime startTS = LocalDateTime.of(startDate, startTime);
                LocalDateTime endTS = LocalDateTime.of(endDate, endTime);
                LocalDateTime startTS_utc = ldt_local2UTC(startTS);
                LocalDateTime endTS_utc = ldt_local2UTC(endTS);

                int customerId = Integer.parseInt(customerIdText.getText().split(" ")[2]);
                int userId = Integer.parseInt(userIdText.getText().split(" ")[2]);
                int contactId = dbHandler.getIdViaName(contact);

                //appointment time conflict check
                boolean apptTimeCheck = appointmentTimeCheck(startTS, endTS);
                boolean apptTimeSlotTaken = dbHandler.appointmentSlotTaken(customerId, startTS_utc, endTS_utc);


                    if(apptTimeCheck) {
                        if(!apptTimeSlotTaken) {

                            dbHandler.insertAppointment(title, description, location, contactId, type, Timestamp.valueOf(startTS_utc), Timestamp.valueOf(endTS_utc), customerId, userId);
                            ObservableList<Appointment> appointments = dbHandler.getAllAppointments4Customer(customerId);
                            appointmentTableView.setItems(appointments);

                            clearTxtFields();

                            //optional
                            addAppointmentBtn.setDisable(false);
                            updateAppointmentBtn.setDisable(true);
                            deleteAppointmentBtn.setDisable(true);

                            startDatePicker.setValue(LocalDate.now());
                            endDatePicker.setValue(LocalDate.now());

                        } else {
                            Alert alert = new Alert(Alert.AlertType.WARNING, "The slot was already taken. Pick another slot.", ButtonType.OK);
                            alert.showAndWait().filter(c -> c == ButtonType.OK);
                        }
                    } else {
                        Alert alert = new Alert(Alert.AlertType.WARNING, "Pick between 8 am to 10 pm EST.", ButtonType.OK);
                        alert.showAndWait().filter(c -> c == ButtonType.OK);
                    }
          } else {
              Alert alert = new Alert(Alert.AlertType.WARNING, "All fields must be filled.", ButtonType.OK);
              alert.showAndWait().filter(c -> c == ButtonType.OK);
          }

        }catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * This method check whether all the text fields in the appointment screen have been filled
     * before the button to add appointment is clicked
     * */
    private boolean appointmentCheck(String title, String description, String location, String contact, String type, String startTime, String endTime, LocalDate startDate, LocalDate endDate){
        if(title.equals("") || description.equals("") || location.equals("") || contact.equals("") || type.equals("") || startTime.equals("") || endTime.equals("") || startDate.toString().equals("") || endDate.toString().equals("")){
            return false;
        }
        return true;
    }


   /**
    * this method converts LocalDateTime in System Time to UTC Time
    * @param ldt_sys  localDateTime in system timezone
    * @return localDateTime in UTC
    * */

    private LocalDateTime ldt_local2UTC(LocalDateTime ldt_sys) {
        //turn LDT in system time to ZDT in system time
        ZonedDateTime ZDT_sys = ldt_sys.atZone(ZoneId.systemDefault());
        //turn ZDT in system time to UTC
        ZonedDateTime ZDT_utc = ZDT_sys.withZoneSameInstant(ZoneId.of("UTC"));
        //turn ZDT in UTC to LDT in UTC
        LocalDateTime ldt_utc = ZDT_utc.toLocalDateTime();
        return ldt_utc;
    }

    /**
     * this method converts LocalDateTime in System Time to EST Time
     * @param ldt_sys  localDateTime in system timezone
     * @return localDateTime in UTC
     * */

    private LocalDateTime ldt_local2EST(LocalDateTime ldt_sys){
        ZonedDateTime ZDT_sys = ldt_sys.atZone(ZoneId.systemDefault());
        ZonedDateTime ZDT_est = ZDT_sys.withZoneSameInstant(ZoneId.of("America/New_York"));
        LocalDateTime ldt_est = ZDT_est.toLocalDateTime();
        return ldt_est;
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
     * This method controls the behaviors after the button to delete a selected appointment is clicked
     * @param event the mouse event
     * */
    @FXML
    void handleDeleteAppointment(ActionEvent event) throws SQLException, ClassNotFoundException {
        Appointment selectedAppointment = appointmentTableView.getSelectionModel().getSelectedItem();
        int appointmentId = selectedAppointment.IdProperty().getValue();

        try{
            dbHandler.deleteAppointment(appointmentId);
            int customerId = Integer.parseInt(customerIdText.getText().split(" ")[2]);
            ObservableList<Appointment> appointments = dbHandler.getAllAppointments4Customer(customerId);
            appointmentTableView.setItems(appointments);
        }catch(SQLException e){
            e.printStackTrace();
            throw e;
        }

        clearTxtFields();

        updateAppointmentBtn.setDisable(true);
        deleteAppointmentBtn.setDisable(true);
        addAppointmentBtn.setDisable(false);

        Alert alert = new Alert(Alert.AlertType.INFORMATION, "Appointment has been deleted.", ButtonType.OK);
        alert.showAndWait().filter(c -> c == ButtonType.OK);

    }

    /**
     * This method controls the behaviors after the button to update a selected appointment is clicked
     * @param event the mouse event
     * */
    @FXML
    void handleUpdateAppointment(ActionEvent event) throws SQLException, ClassNotFoundException {
        String title = titleTextField.getText();
        String description = descriptionTextField.getText();
        String location = locationTextField.getText();
        String contact = contactComboBox.getValue();
        String type = typeTextField.getText();
        int customerId = Integer.parseInt(customerIdText.getText().split(" ")[2]);
        int userId = Integer.parseInt(userIdText.getText().split(" ")[2]);

        //obtain local date
        LocalDate startDate = startDatePicker.getValue();
        LocalDate endDate = endDatePicker.getValue();

        //obtain local time
        DateTimeFormatter df = DateTimeFormatter.ofPattern("H:mm:ss");
        LocalTime startTime = LocalTime.parse(startTimeTextField.getText()+":00", df);
        LocalTime endTime = LocalTime.parse(endTimeTextField.getText()+":00", df);

        //obtain local date and time in UTC
        LocalDateTime startTS = LocalDateTime.of(startDate, startTime);
        LocalDateTime endTS = LocalDateTime.of(endDate, endTime);
        LocalDateTime startTS_utc = ldt_local2UTC(startTS);
        LocalDateTime endTS_utc = ldt_local2UTC(endTS);

        try{
            int contactId = dbHandler.getIdViaName(contact);

            Appointment appointment = appointmentTableView.getSelectionModel().getSelectedItem();
            int appointmentId = appointment.IdProperty().getValue();

            //appointment time conflict check
            boolean apptTimeCheck = appointmentTimeCheck(startTS, endTS);
            boolean apptTimeSlotTaken = dbHandler.appointmentSlotTaken(customerId, startTS_utc, endTS_utc);

            if(apptTimeCheck) {
                if(!apptTimeSlotTaken) {
                    dbHandler.updateAppointment(appointmentId, title, description, location, type, Timestamp.valueOf(startTS_utc), Timestamp.valueOf(endTS_utc), customerId, userId, contactId);
                    ObservableList<Appointment> appointments = dbHandler.getAllAppointments4Customer(customerId);
                    appointmentTableView.setItems(appointments);

                    clearTxtFields();

                    //optional
                    deleteAppointmentBtn.setDisable(true);
                    updateAppointmentBtn.setDisable(true);
                    addAppointmentBtn.setDisable(false);
                } else {
                    Alert alert = new Alert(Alert.AlertType.WARNING, "The slot was already taken. Pick another slot.", ButtonType.OK);
                    alert.showAndWait().filter(c -> c == ButtonType.OK);
                }
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING, "Pick between 8 am to 10 pm.", ButtonType.OK);
                alert.showAndWait().filter(c -> c == ButtonType.OK);
            }

        } catch(SQLException e) {
            e.printStackTrace();
            throw e;
        }

    }


}
