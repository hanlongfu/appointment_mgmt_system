package database;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.*;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;


/**
 * This class controls operations involving the database
 * */
public class DbHandler extends database.Configs {

    private Connection dbConnection;

    /**
     * this establishes connection with the database
     * @return database connection
     */
    public Connection getDbConnection() throws ClassNotFoundException, SQLException {
        String connectionString = "jdbc:mysql://" + dbHost + ":" + dbPort + "/" + dbName + "?connectionTimeZone = SERVER";
        Class.forName("com.mysql.cj.jdbc.Driver");
        dbConnection = DriverManager.getConnection(connectionString, dbUser, dbPass);
        return dbConnection;
    }

    /**
     * This method create User object from ResultSet produced from a database operation
     * @param rs  result set from database operations
     * @return list of user objects
     * */
    private static ObservableList<User> createUsers(ResultSet rs) throws SQLException, ClassNotFoundException{
        try{
            ObservableList<User> users = FXCollections.observableArrayList();

            while(rs.next()) {
                User user = new User();
                user.setUserId(rs.getInt("User_ID"));
                user.setUserName(rs.getString("User_Name"));
                user.setPassword(rs.getString("Password"));
                users.add(user);
            }
            return users;

        } catch(SQLException err) {
            err.printStackTrace();
            throw err;
        }
    }

    /**
     * This method retrieves all users from the database
     * @return a list of all users
     * */
    public ObservableList<User> getAllUsers() throws SQLException, ClassNotFoundException{
        String query = "SELECT * FROM users";

        try{
            PreparedStatement ps = getDbConnection().prepareStatement(query);
            ResultSet rs =  ps.executeQuery();
            ObservableList<User> users = createUsers(rs);
            return users;
        } catch(SQLException e) {
            System.out.println("Failed to verify user against the database" + e);
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * This method check username and password against the database
     * @param userName  username from the text field input
     * @param password  password from the text field input
     * @return  true or false
     * */
    public boolean checkCredentials(String userName, String password) throws SQLException, ClassNotFoundException{
        try{
            String query = "SELECT * FROM users WHERE User_Name = '"+userName+"' AND Password = '"+password+"'";

            PreparedStatement ps = getDbConnection().prepareStatement(query);
            ResultSet rs =  ps.executeQuery();

            int counter = 0;

            while(rs.next()) {
                counter++;
                String userName_db = rs.getString("User_Name");
                String pwd_db = rs.getString("Password");
            }
            if(counter == 1) {
                return true;
            } else {
                return false;
            }
        } catch(SQLException e) {
            System.out.println("Failed to verify user against the database" + e);
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * This method returns userid from a user name
     * @param userName  user name to be checked against the database
     * @return  integer user id
     * */
    public int getUserId(String userName) throws SQLException, ClassNotFoundException {
        String query = "SELECT User_ID FROM users WHERE User_Name = '"+userName+"'";

        try{
            PreparedStatement ps = getDbConnection().prepareStatement(query);
            ResultSet rs =  ps.executeQuery();
            while(rs.next()){
                return Integer.parseInt(rs.getString(1));
            }
            return 0;
        }catch(SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }


    /**
     * This method create Customer objects from ResultSet produced from a database operation
     * @param rs  result set from database operations
     * @return list of user objects
     * */
    private static ObservableList<Customer> createCustomers(ResultSet rs) throws SQLException, ClassNotFoundException {
        try{

            ObservableList<Customer> customers = FXCollections.observableArrayList();

            while (rs.next()) {
                Customer customer = new Customer();
                customer.setNameProperty(rs.getString("Customer_Name"));
                customer.setAddressProperty(rs.getString("Address"));
                customer.setPhoneNumberProperty(rs.getString("Phone"));
                customer.setPostalCodeProperty(rs.getString("Postal_Code"));
                customer.setIdProperty(rs.getInt("Customer_ID"));
                customer.setDivisionIdProperty(rs.getInt("Division_ID"));
                customers.add(customer);
            }

            return customers;

        } catch(SQLException err) {
            err.printStackTrace();
            throw err;
        }
    }

    /**
     * This method inserts customers into the database
     * @param name  name
     * @param address  address
     * @param divisionId  division id
     * @param phoneNumber  phone number
     * @param postalCode  postal code
     * */
    public void insertCustomer(String name, String address, int divisionId, String postalCode, String phoneNumber) throws SQLException, ClassNotFoundException {

        String insert =  "insert into customers"
                + "(Customer_Name, Address, Division_ID, Postal_Code, Phone) "
                + "VALUES('"+name+"', '"+address+"', '"+divisionId+"', '"+postalCode+"', '"+phoneNumber+"')";

        try{
           PreparedStatement ps = getDbConnection().prepareStatement(insert);
           ps.executeUpdate();
        } catch(SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method delete a customer using id from the database
     * @param id  customer id
     * */
    public void deleteCustomer(int id) throws SQLException, ClassNotFoundException {
        String query = "DELETE FROM customers WHERE CUSTOMER_ID = '"+id+"' ";
        try {
            PreparedStatement preparedStatement = getDbConnection().prepareStatement(query);
            preparedStatement.executeUpdate();
        } catch(SQLException e){
            e.printStackTrace();
        }
    }

    /**
     * This method update a customer in the database
     * @param id  customer id
     * @param name name
     * @param address  address
     * @param divisionId  division id
     * @param postalCode  postal code
     * @param phoneNumber  phone number
     * */
    public void updateCustomer(int id, String name, String address, int divisionId, String postalCode, String phoneNumber) throws SQLException, ClassNotFoundException {
        String query = "update customers set Customer_Name = '"+name+"', "
                + "Address = '"+address+"', Postal_Code = '"+postalCode+"', Phone = '"+phoneNumber+"', "
                + "Division_ID = '"+divisionId+"' "
                + "where Customer_ID = '"+id+"' ";
        try{
            PreparedStatement ps = getDbConnection().prepareStatement(query);
            ps.executeUpdate();
        }catch(SQLException e){
            e.printStackTrace();
        }
    }


    /**
     * This method retrieves all the customers from the database
     * @return a list of all the customers
     * */
    public ObservableList<Customer> getAllCustomers() throws SQLException, ClassNotFoundException{
        String query = "SELECT * FROM customers";

        try{
            PreparedStatement ps = getDbConnection().prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            ObservableList<Customer> customers = createCustomers(rs);
            return customers;

        }catch(SQLException e){
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * This method retrieves all the customers added the past week
     * @return a list of all the qualified customers
     * */
    public ObservableList<Customer> getAllCustomersThisWeek() throws SQLException, ClassNotFoundException{
        String query = "SELECT * FROM customers WHERE Create_Date >= DATE_ADD(NOW(), INTERVAL -7 DAY)";   // DATEADD(DAY, -7, NOW())?

        try{
            PreparedStatement ps = getDbConnection().prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            ObservableList<Customer> customers = createCustomers(rs);
            return customers;

        }catch(SQLException e){
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * This method create Country objects from ResultSet produced from a database operation
     * @param rs  result set from database operations
     * @return list of user objects
     * */
    private ObservableList<Country> createCountries(ResultSet rs) throws SQLException, ClassNotFoundException {
        try{
            ObservableList<Country> countries = FXCollections.observableArrayList();

            while(rs.next()) {
                Country country  = new Country();
                country.setCountryId(rs.getInt("Country_ID"));
                country.setCountryName(rs.getString("Country"));
                countries.add(country);
            }
            return countries;
        } catch(SQLException e){
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * This method returns country name using country id
     * @param id  country id
     * @return country name
     * */
    public String getCountryName(int id) throws SQLException, ClassNotFoundException{
        String query = "SELECT Country FROM countries WHERE Country_ID = "+id+"";
        try{
            PreparedStatement ps = getDbConnection().prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                String countryName = rs.getString(1);
                return countryName;
            }
            return "";
        } catch(SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * This method returns country id through division id
     * @param id  division id
     * @return country id
     * */
    public int getCountryIdViaDivision(int id) throws SQLException, ClassNotFoundException{
        String query = "SELECT Country_ID FROM first_level_divisions WHERE Division_ID = "+id+"";
        try{
            PreparedStatement ps = getDbConnection().prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                int countryId = Integer.parseInt(rs.getString(1));
                return countryId;
            }
            return 0;
        } catch(SQLException e){
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * This method returns country id through country name
     * @param countryName country name
     * @return country id
     * */
    public int getSelectedCountryId(String countryName) throws SQLException, ClassNotFoundException{
        String query = "SELECT Country_ID from countries WHERE Country = '"+countryName+"' ";
        try{
            PreparedStatement ps = getDbConnection().prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                int countryId = Integer.parseInt(rs.getString(1));
                return countryId;
            }
            return 0;
        } catch(SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }


    /**
     * This method returns all the countries
     * @return a list of all the countries
     * */
    public ObservableList<Country> getAllCountries() throws SQLException, ClassNotFoundException{
        String query = "SELECT * FROM countries";

        try{
            PreparedStatement ps = getDbConnection().prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            ObservableList<Country> countries = createCountries(rs);
            return countries;
        } catch(SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * This method create Division objects from ResultSet produced from a database operation
     * @param rs  result set from database operations
     * @return list of user objects
     * */
    private ObservableList<Division> createDivisions(ResultSet rs) throws SQLException, ClassNotFoundException {
        try{
            ObservableList<Division> divisions = FXCollections.observableArrayList();

            while(rs.next()){
                Division division = new Division();
                division.setId(rs.getInt("Division_ID"));
                division.setCountryId(rs.getInt("Country_ID"));
                division.setDivisionName(rs.getString("Division"));

                divisions.add(division);
            }
            return divisions;
        }catch(SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * This method returns division id from division name
     * @param divisionName division name
     * @return division id
     * */
    public int getSelectedDivisionID(String divisionName) throws SQLException, ClassNotFoundException{
        String query = "SELECT Division_ID FROM first_level_divisions WHERE Division = '"+divisionName+"' ";
        try{
            PreparedStatement ps = getDbConnection().prepareStatement(query);
            ResultSet rs = ps.executeQuery();

            while(rs.next()) {
                int id = Integer.parseInt(rs.getString(1));
                return id;
            }
            return 0;
        }catch(SQLException e){
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * This method returns division name using division id
     * @param id  division id
     * @return division name
     * */
    public String getDivisionName(int id) throws SQLException, ClassNotFoundException{
        String query = "SELECT Division FROM first_level_divisions WHERE Division_ID = "+id+"";

        try{
            PreparedStatement ps = getDbConnection().prepareStatement(query);
            ResultSet rs = ps.executeQuery();

            while(rs.next()) {
                String name = rs.getString(1);
                return name;
            }
            return "";
        }catch(SQLException e){
            e.printStackTrace();
            throw e;
        }

    }

    /**
     * This method returns a list of all the divisions
     * @return a list of all the divisions
     * */
    public ObservableList<Division> getAllDivisions() throws SQLException, ClassNotFoundException{
        String query = "SELECT * FROM first_level_divisions";

        try{
            PreparedStatement ps = getDbConnection().prepareStatement(query);
            ResultSet rs = ps.executeQuery();

            ObservableList<Division> divisions = createDivisions(rs);
            return divisions;
        } catch(SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * This method create Appointment objects from ResultSet produced from a database operation
     * @param rs  result set from database operations
     * @return list of user objects
     * */
    private ObservableList<Appointment> createAppointments(ResultSet rs) throws SQLException, ClassNotFoundException {
        try{
            ObservableList<Appointment> appointments = FXCollections.observableArrayList();
            while(rs.next()) {
                Appointment appointment = new Appointment();
                appointment.setCustomerId(rs.getInt("Customer_ID"));
                appointment.setId(rs.getInt("Appointment_ID"));
                appointment.setTitle(rs.getString("Title"));
                appointment.setDescription(rs.getString("Description"));
                appointment.setLocation(rs.getString("Location"));
                appointment.setContactId(rs.getInt("Contact_ID"));
                appointment.setType(rs.getString("Type"));

                //convert timestamp to localDateTime, then to string
                LocalDateTime ldt_start_sys = utc_ts_2_ldt(rs.getTimestamp("Start"));
                LocalDateTime ldt_end_sys = utc_ts_2_ldt(rs.getTimestamp("End"));
                DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd kk:mm:ss");

                appointment.setStartTime(ldt_start_sys.format(df));
                appointment.setEndTime(ldt_end_sys.format(df));
                appointments.add(appointment);
            }
            return appointments;

        } catch(SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * This method converts timestamp to local date time in UTC
     * @param t  timestamp to be converted
     * @return local date time in UTC
     * */
    private LocalDateTime utc_ts_2_ldt(Timestamp t){
        LocalDateTime ldt_utc = t.toLocalDateTime();
        ZonedDateTime zdt_utc = ldt_utc.atZone(ZoneId.of("UTC"));
        ZonedDateTime zdt_sys = zdt_utc.withZoneSameInstant(ZoneId.systemDefault());
        LocalDateTime ldt_sys = zdt_sys.toLocalDateTime();
        return ldt_sys;
    }


    /**
     * This method checks whether an appoint is already present
     * @param id  appointment id
     * @return true or false
     * */
    public boolean hasAppointments(int id) throws SQLException, ClassNotFoundException{
        String query = "SELECT * FROM appointments WHERE Customer_ID = "+id+"";

        try{
            PreparedStatement ps = getDbConnection().prepareStatement(query);
            ResultSet rs = ps.executeQuery();

            ObservableList<Appointment> appointments = createAppointments(rs);
            if(appointments.isEmpty()) {
               return false;
            }
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }

    }

    /**
     * This method insert an appointment into the database
     * */
    public void insertAppointment(String title, String description, String location, int contactId, String type,
                                  Timestamp startTime, Timestamp endTime, int customerId, int userId) throws SQLException, ClassNotFoundException {
        String query = "INSERT INTO appointments"
                + "(Title, Description, Location, Contact_ID, Type, Start, End, Customer_ID, User_ID) "
                + "values(?,?, ?, ?, ?, ?, ?, ?, ?)";
        try{
            PreparedStatement ps = getDbConnection().prepareStatement(query);
            ps.setString(1, title);
            ps.setString(2, description);
            ps.setString(3, location);
            ps.setInt(4, contactId);
            ps.setString(5, type);
            ps.setTimestamp(6, startTime);
            ps.setTimestamp(7, endTime);
            ps.setInt(8, customerId);
            ps.setInt(9, userId);
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * This method update an existing appointment
     * */
    public void updateAppointment(int id, String title, String description, String location, String type, Timestamp startTime, Timestamp endTime,
                                  int customerId, int userId, int contactId) throws SQLException, ClassNotFoundException {
        String query = "UPDATE appointments SET Title = '"+title+"', "
                + "Description = '"+description+"', Location = '"+location+"', Type = '"+type+"', "
                + "Start = '"+startTime+"', End = '"+endTime+"', Customer_ID = "+customerId+", User_ID = "+userId+", Contact_ID = '"+contactId+"' "
                + "WHERE Appointment_ID = '"+id+"' ";
        try{
            PreparedStatement ps = getDbConnection().prepareStatement(query);
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * This method delete an appointment
     * @param id  an appointment id
     * */
    public void deleteAppointment(int id) throws SQLException, ClassNotFoundException{
        String query = "DELETE FROM appointments WHERE Appointment_ID = '"+id+"' ";
        try{
            PreparedStatement ps = getDbConnection().prepareStatement(query);
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * This method returns all the appointments for a given customer
     * @param id  customer id
     * @return a list of all the appointments
     * */
    public ObservableList<Appointment> getAllAppointments4Customer(int id) throws SQLException, ClassNotFoundException {
        String query = "SELECT * FROM appointments WHERE Customer_ID = "+id+"";
        try{
            PreparedStatement ps = getDbConnection().prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            ObservableList<Appointment> appointments = createAppointments(rs);
            return appointments;
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }


    /**
     * This method returns all the appointments from the database
     * @return a list of all the appointments
     * */
    public ObservableList<Appointment> getAllAppointments() throws SQLException, ClassNotFoundException{
        String query = "SELECT * FROM appointments";
        try{
            PreparedStatement ps = getDbConnection().prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            ObservableList<Appointment> appointments = createAppointments(rs);
            return appointments;
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * This method returns all the appointments in the next 7 days
     * @return a list of qualified appointments
     * */
    public ObservableList<Appointment> getAllAppointments7Days() throws SQLException, ClassNotFoundException {
        String query = "SELECT * FROM appointments WHERE Start BETWEEN NOW() AND DATE_ADD(NOW(), INTERVAL 7 DAY)";
        try{
            PreparedStatement ps = getDbConnection().prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            ObservableList<Appointment> appointments = createAppointments(rs);
            return appointments;
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * This method returns all the appointments in the next 30 days
     * @return a list of qualified appointments
     * */
    public ObservableList<Appointment> getAllAppointments30Days() throws SQLException, ClassNotFoundException{
        String query = "SELECT * FROM appointments WHERE Start BETWEEN NOW() AND DATE_ADD(NOW(), INTERVAL 1 MONTH)";
        try{
            PreparedStatement ps = getDbConnection().prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            ObservableList<Appointment> appointments = createAppointments(rs);
            return appointments;
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * This method returns all the appointments in the next 15 minutes
     * @return a list of qualified appointments
     * */
    public ObservableList<Appointment> getAllAppointments15min() throws SQLException, ClassNotFoundException{
        String query = "SELECT * FROM appointments WHERE Start BETWEEN NOW() AND DATE_ADD(NOW(), INTERVAL 15 MINUTE)";
        try{
            PreparedStatement ps = getDbConnection().prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            ObservableList<Appointment> appointments = createAppointments(rs);
            return appointments;
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * This method returns appointment breakdown by type
     * @return a list of qualified appointments
     * */
    public String getAllAppointmentsByType() throws SQLException, ClassNotFoundException{
        String query = "SELECT TYPE, COUNT(*) AS N  FROM appointments GROUP BY TYPE";
        try{
            PreparedStatement ps = getDbConnection().prepareStatement(query);
            ResultSet rs = ps.executeQuery();

            String result = "Type ----> Count" + "\n";

            while(rs.next()){
                result += rs.getString("Type") + " ----> " + rs.getString("N") + "\n";
            }
        return result;

        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }

    }

    /**
     * This method returns appointment breakdown by month
     * @return a list of qualified appointments
     * */
    public String getAllAppointmentsByMonth() throws SQLException, ClassNotFoundException {
        String query = "SELECT MONTH(Start) AS Month, COUNT(*) as N FROM appointments GROUP BY MONTH(Start)";
        try{
            PreparedStatement ps = getDbConnection().prepareStatement(query);
            ResultSet rs = ps.executeQuery();

            String result = "Month ----> Count" + "\n";

            while(rs.next()){
                result += rs.getString("Month") + " ----> " + rs.getString("N") + "\n";
            }
            return result;

        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }

    }


    /**
     * This method returns appointment breakdown by contact
     * @return a list of qualified appointments
     * */
    public String getAllAppointmentsByContact() throws SQLException, ClassNotFoundException {
        String query = "SELECT c.Contact_Name, a.Title, a.Description, a.Location, a.Type, a.Start, a.End, a.Customer_ID FROM appointments a LEFT JOIN contacts c ON a.Contact_ID = c.Contact_ID ";
        try{
            PreparedStatement ps = getDbConnection().prepareStatement(query);
            ResultSet rs = ps.executeQuery();

            String result = "Contact_Name  ---  Title  ---  Description  ---  Location  ---  Type  ---  Start  ----  End  ----  Customer_ID" + "\n";

            while(rs.next()){
                result += rs.getString("Contact_Name") + " --- " + rs.getString("Title") + " --- " + rs.getString("Description") +
                        " --- " + rs.getString("Location") + " --- " + rs.getString("Type") + " --- " + rs.getString("Start") + " --- "
                + rs.getString("End") + " --- " + rs.getString("Customer_ID") + "\n";
            }
            return result;

        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }


    /**
     * This method checks if an appointment time slot already taken
     * @param id customer id
     * @param startTime start time
     * @param endTime  end time
     * @return true or false
     * */
    public boolean appointmentSlotTaken(int id, LocalDateTime startTime, LocalDateTime endTime) throws SQLException, ClassNotFoundException{
        String query = "SELECT COUNT(*) AS CNT FROM appointments " +
                "WHERE Customer_ID = "+id+" " +
                "AND (('"+startTime+"' BETWEEN Start AND End) " +
                "OR ('"+endTime+"' BETWEEN Start AND End) " +
                "OR (Start BETWEEN '"+startTime+"' AND '"+endTime+"') " +
                "OR (End BETWEEN '"+startTime+"' AND '"+endTime+"')) ";

        try{
            PreparedStatement ps = getDbConnection().prepareStatement(query);
            ResultSet rs = ps.executeQuery();

            while(rs.next()){
                if(Integer.parseInt(rs.getString(1)) >= 1){
                    return true;
                } else {
                    return false;
                }
            }
            return false;
        } catch(SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * This method create Contact objects from ResultSet produced from a database operation
     * @param rs  result set from database operations
     * @return list of user objects
     * */
    private ObservableList<Contact> createContacts(ResultSet rs) throws SQLException, ClassNotFoundException{
        try{
            ObservableList<Contact> contacts = FXCollections.observableArrayList();

            while(rs.next()) {
                Contact contact = new Contact();
                contact.setId(rs.getInt("Contact_ID"));
                contact.setName(rs.getString("Contact_Name"));
                contact.setEmail(rs.getString("Email"));
                contacts.add(contact);
            }
            return contacts;

        } catch(SQLException e){
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * This method returns a list of all the contacts
     * @return list of all the contacts
     * */
    public ObservableList<Contact> getAllContacts() throws SQLException, ClassNotFoundException{
        String query = "SELECT * FROM contacts";

        try{
            PreparedStatement ps = getDbConnection().prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            ObservableList<Contact> contacts = createContacts(rs);
            return contacts;
        } catch(SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * This method returns contact name through contact id
     * @param id contact id
     * @return contact name
     * */
    public String getNameViaId(int id) throws SQLException, ClassNotFoundException {
        String query = "SELECT Contact_Name FROM contacts WHERE Contact_ID = '"+id+"' ";

        try{
            PreparedStatement ps = getDbConnection().prepareStatement(query);
            ResultSet rs = ps.executeQuery();

            while(rs.next()) {
                String name = rs.getString(1);
                return name;
            }
            return "";

        } catch(SQLException e) {
            e.printStackTrace();
            throw e;
        }

    }

    /**
     * This method returns contact id through contact name
     * @param name contact name
     * @return contact id
     * */
    public int getIdViaName(String name) throws SQLException, ClassNotFoundException {
        String query = "SELECT Contact_ID FROM contacts WHERE Contact_Name = '"+name+"' ";
        try{
            PreparedStatement ps = getDbConnection().prepareStatement(query);
            ResultSet rs = ps.executeQuery();

            while(rs.next()) {
                int id = Integer.parseInt(rs.getString(1));
                return id;
            }
            return 0;

        } catch(SQLException e){
            e.printStackTrace();
            throw e;
        }

    }

}
