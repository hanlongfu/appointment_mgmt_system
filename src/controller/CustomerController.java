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
import model.Country;
import model.Customer;
import model.Customer_Addon;
import model.Division;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CustomerController implements Initializable {

    @FXML private Label userIdText;

    @FXML private TableView<Customer> customerTableView;
    @FXML private TableColumn<Customer, String> customerAddressCol;
    @FXML private TableColumn<Customer, Integer> customerDivisionCol;
    @FXML private TableColumn<Customer, Integer> customerIdCol;
    @FXML private TableColumn<Customer, String> customerNameCol;
    @FXML private TableColumn<Customer, String> customerPhoneNumberCol;
    @FXML private TableColumn<Customer, String> customerPostalCodeCol;

    @FXML private ComboBox<String> firstLevelDivisionComboBox;
    @FXML private ComboBox<String> countryComboBox;

    @FXML private TextField customerAddressTextField;
    @FXML private TextField customerNameTextField;
    @FXML private TextField phoneNumberTextField;
    @FXML private TextField postalCodeTextField;

    @FXML private Button addCustomerBtn;
    @FXML private Button deleteCustomerBtn;
    @FXML private Button updateCustomerBtn;
    @FXML private Button viewAppointmentsBtn;

    private DbHandler dbHandler;

    /**
     * This method initializes the customer controller class
     * @param rb ResourceBundle
     * @param url url
     * */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        dbHandler = new DbHandler();
        addCustomerBtn.setDisable(false);
        updateCustomerBtn.setDisable(true);
        deleteCustomerBtn.setDisable(true);
        viewAppointmentsBtn.setDisable(true);

        //populate TableView with existing data from the database
        customerIdCol.setCellValueFactory(cellData -> cellData.getValue().getIdProperty().asObject());
        customerDivisionCol.setCellValueFactory(cellData -> cellData.getValue().getDivisionIdProperty().asObject());
        customerNameCol.setCellValueFactory(cellData -> cellData.getValue().getNameProperty());
        customerAddressCol.setCellValueFactory(cellData -> cellData.getValue().getAddressProperty());
        customerPostalCodeCol.setCellValueFactory(cellData -> cellData.getValue().getPostalCodeProperty());
        customerPhoneNumberCol.setCellValueFactory(cellData -> cellData.getValue().getPhoneNumberProperty());

        //initialize country combo box
        try {
            ObservableList<Country> countries = dbHandler.getAllCountries();
            ObservableList<String> countryNames = FXCollections.observableArrayList();
            countries.forEach((country) ->{
                countryNames.add(country.getCountryNameProperty().getValue());
            });
            countryComboBox.setItems(countryNames);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        //populate the customer tableview with data from the database
        try {
            ObservableList<Customer> customers = dbHandler.getAllCustomers();
            customerTableView.setItems(customers);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        //read data for a selected customer
        customerTableView.getSelectionModel().selectedItemProperty().addListener((item, oldItem, newItem) -> {
            if(newItem != null) {
                addCustomerBtn.setDisable(true);
                updateCustomerBtn.setDisable(false);
                deleteCustomerBtn.setDisable(false);
                viewAppointmentsBtn.setDisable(false);

                Customer selectedCustomer = customerTableView.getSelectionModel().getSelectedItem();
                int divisionId = selectedCustomer.getDivisionIdProperty().getValue();
                String address = selectedCustomer.getAddressProperty().getValue();
                String name = selectedCustomer.getNameProperty().getValue();
                String postalCode = selectedCustomer.getPostalCodeProperty().getValue();
                String phoneNumber = selectedCustomer.getPhoneNumberProperty().getValue();

                try{
                    int countryId = dbHandler.getCountryIdViaDivision(divisionId);
                    String countryName = dbHandler.getCountryName(countryId);
                    String divisionName = dbHandler.getDivisionName(divisionId);

                    countryComboBox.setValue(countryName);
                    firstLevelDivisionComboBox.setValue(divisionName);

                } catch(SQLException | ClassNotFoundException e) {
                    Logger.getLogger(CustomerController.class.getName()).log(Level.SEVERE, null, e);
                }

                customerAddressTextField.setText(address);
                customerNameTextField.setText(name);
                postalCodeTextField.setText(postalCode);
                phoneNumberTextField.setText(phoneNumber);

            }
        });
    }

    /**
     * This method check all the text fields have been filled before the button to add a customer is clicked
     * @return true or false
     **/
    private boolean customerCheck(String name, String address, String country, String postalCode, String phoneNumber, String firstLevelDivision){
        if(name.equals("") || address.equals("") || country.equals("") || postalCode.equals("") || phoneNumber.equals("") || firstLevelDivision.equals("")){
            return false;
        }
        return true;
    }

    /**
     * This method clears the defined text fields
     **/
    private void clearTxtFields() {
        customerNameTextField.clear();
        customerAddressTextField.clear();
        postalCodeTextField.clear();
        phoneNumberTextField.clear();
        countryComboBox.getSelectionModel().clearSelection();
        countryComboBox.setPromptText("Pick a country");
        firstLevelDivisionComboBox.getSelectionModel().clearSelection();
        firstLevelDivisionComboBox.setPromptText("Pick a division");
    }

    /**
     * This method set the user id
     * @param id  user id to be set
     **/
    public void getUserId(int id) {
        userIdText.setText("User ID: " + id);
    }

    /**
     * This method controls the behaviors after the button to add a customer is clicked
     * @param event the mouse event
     * */
    @FXML
    void handleAddCustomer(ActionEvent event) throws SQLException, ClassNotFoundException {
        try{
            String address = customerAddressTextField.getText();
            String name = customerNameTextField.getText();
            String country = countryComboBox.getValue();
            String postalCode = postalCodeTextField.getText();
            String phoneNumber = phoneNumberTextField.getText();
            String firstLevelDivision = firstLevelDivisionComboBox.getValue();
            int selectedDivisionID = dbHandler.getSelectedDivisionID(firstLevelDivision);

            boolean customerCheck = customerCheck(name, address, country, postalCode, phoneNumber, firstLevelDivision);
            if(customerCheck){
                dbHandler.insertCustomer(name, address, selectedDivisionID, postalCode, phoneNumber);
                ObservableList<Customer> customers = dbHandler.getAllCustomers();
                customerTableView.setItems(customers);
                clearTxtFields();

                deleteCustomerBtn.setDisable(true);
                addCustomerBtn.setDisable(false);
                updateCustomerBtn.setDisable(true);
                viewAppointmentsBtn.setDisable(true);
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING, "All fields must be filled", ButtonType.OK);
                alert.showAndWait().filter(c -> c == ButtonType.OK);
            }
        } catch(SQLException e){
            e.printStackTrace();
            throw e;
        }
    }


    /**
     * This method controls the behaviors after a country is selected from the comboBox
     * @param event the mouse event
     * */
    @FXML
    void countrySelected(ActionEvent event) throws SQLException, ClassNotFoundException{
        try{
            String selectedCountry = countryComboBox.getValue();
            int selectedCountryId = dbHandler.getSelectedCountryId(selectedCountry);

            ObservableList<Division> divisions = dbHandler.getAllDivisions();
            ObservableList<String> divisionNames = FXCollections.observableArrayList();

            divisions.forEach((division) -> {
                if(selectedCountryId == division.getCountryIdProperty().getValue()) {
                    String selectedDivision = division.getDivisionNameProperty().getValue();
                    divisionNames.add(selectedDivision);
                }
            });
            firstLevelDivisionComboBox.setItems(divisionNames);
        } catch(SQLException e){
            e.printStackTrace();
            throw e;
        }
    }


    /**
     * This method controls the behaviors after the button to update a customer is clicked
     * @param event the mouse event
     * */
    @FXML
    void handleUpdateCustomer(ActionEvent event) throws SQLException, ClassNotFoundException {
        try{
            String name = customerNameTextField.getText();
            String address = customerAddressTextField.getText();
            String postalCode = postalCodeTextField.getText();
            String phoneNumber = phoneNumberTextField.getText();
            String country = countryComboBox.getValue();
            String firstLevelDivision = firstLevelDivisionComboBox.getValue();
            int divisionId = dbHandler.getSelectedDivisionID(firstLevelDivision);

            Customer customer = customerTableView.getSelectionModel().getSelectedItem();
            int id = customer.getIdProperty().getValue();
            boolean customerCheck = customerCheck(name, address, country, postalCode, phoneNumber, firstLevelDivision);

            if(customerCheck) {
                dbHandler.updateCustomer(id, name, address, divisionId, postalCode, phoneNumber);
                ObservableList<Customer> customers = dbHandler.getAllCustomers();
                customerTableView.setItems(customers);

                clearTxtFields();

                updateCustomerBtn.setDisable(true);
                deleteCustomerBtn.setDisable(true);
                viewAppointmentsBtn.setDisable(true);
                addCustomerBtn.setDisable(false);
            } else{
                Alert alert = new Alert(Alert.AlertType.WARNING, "Failed to update customer", ButtonType.OK);
                alert.showAndWait().filter(c -> c == ButtonType.OK);
            }

        } catch(SQLException e){
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * This method controls the behaviors after the button to delete a customer is clicked
     * @param event the mouse event
     * */
    @FXML
    void handleDeleteCustomer(ActionEvent event) throws SQLException, ClassNotFoundException {

        Customer customer = customerTableView.getSelectionModel().getSelectedItem();
        int id = customer.getIdProperty().getValue();
        boolean hasAppt = dbHandler.hasAppointments(id);
        if(!hasAppt){
            try{
                dbHandler.deleteCustomer(id);
                ObservableList<Customer> customers = dbHandler.getAllCustomers();
                customerTableView.setItems(customers);
            }catch(SQLException e) {
                e.printStackTrace();
                throw e;
            }

        clearTxtFields();

        updateCustomerBtn.setDisable(true);
        deleteCustomerBtn.setDisable(true);
        viewAppointmentsBtn.setDisable(true);
        addCustomerBtn.setDisable(false);

        Alert alert = new Alert(Alert.AlertType.INFORMATION, "Customer has been successfully deleted!", ButtonType.OK);
        alert.showAndWait().filter(c -> c == ButtonType.OK);
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Failed to delete the customer due to associated appointments! Delete appointments first.", ButtonType.OK);
            alert.showAndWait().filter(c -> c == ButtonType.OK);
        }
    }


    /**
     * This method controls the behaviors after the button to exit is clicked
     * @param event the mouse event
     * */
    @FXML
    void handleExit(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/view/login.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            LoginController controller = loader.getController();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    /**
     * This method controls the behaviors after the button to view full schedule is clicked
     * @param event the mouse event
     * */
    @FXML
    void handleViewSchedule(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/view/schedule.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            //trouble spot
            ScheduleController controller = loader.getController();
            int userId = Integer.parseInt(userIdText.getText().split(" ")[2]);
            controller.getUserIdText(userId);

            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    /**
     * This method controls the behaviors after the button to view appointments is clicked
     * @param event the mouse event
     * */
    @FXML
    void handleViewAppointments(ActionEvent event) throws IOException {
        try{
            Customer customer = customerTableView.getSelectionModel().getSelectedItem();
            Customer_Addon.id = customer.getIdProperty().getValue();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/appointment.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            //trouble spot
            AppointmentController controller = loader.getController();
            int userId = Integer.parseInt(userIdText.getText().split(" ")[2]);
            controller.setUserCustomerId(customer, userId);

            stage.setScene(scene);
            stage.show();
        } catch(IOException e) {
            Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    /**
     * This method controls the behaviors after the button to view reports is clicked
     * @param event the mouse event
     * */
    @FXML
    void handleViewReports(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/report.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            ReportController controller = loader.getController();
            int userId = Integer.parseInt(userIdText.getText().split(" ")[2]);
            controller.setUserIdText(userId);

            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, e);
        }

    }




}
