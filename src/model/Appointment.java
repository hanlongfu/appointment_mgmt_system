package model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.sql.Timestamp;
import java.time.LocalDateTime;


/*
* This defines the Appointment Class
* */
public class Appointment {

    private IntegerProperty idProperty;
    private IntegerProperty customerIdProperty;
    private StringProperty titleProperty;
    private IntegerProperty userIdProperty;
    private IntegerProperty contactIdProperty;
    private StringProperty descriptionProperty;
    private StringProperty locationProperty;
    private StringProperty typeProperty;
    private StringProperty startTimeProperty;
    private StringProperty endTimeProperty;

    /**
     * This is the constructor
     */
    public Appointment() {
        this.idProperty = new SimpleIntegerProperty();
        this.customerIdProperty = new SimpleIntegerProperty();
        this.titleProperty = new SimpleStringProperty();
        this.userIdProperty = new SimpleIntegerProperty();
        this.contactIdProperty = new SimpleIntegerProperty();
        this.descriptionProperty = new SimpleStringProperty();
        this.locationProperty = new SimpleStringProperty();
        this.typeProperty = new SimpleStringProperty();
        this.startTimeProperty = new SimpleStringProperty();
        this.endTimeProperty = new SimpleStringProperty();
    }

    /**
     * These are the getters
     */
    public IntegerProperty IdProperty() { return idProperty; }
    public int getId() {
        return idProperty.get();
    }

    public int getCustomerId() { return customerIdProperty.get(); }
    public IntegerProperty customerIdProperty() {
        return customerIdProperty;
    }

    public String getTitle() { return titleProperty.get();}
    public StringProperty titleProperty() {
        return titleProperty;
    }

    public int getUserId() {
        return userIdProperty.get();
    }
    public IntegerProperty userIdPropertyProperty() {
        return userIdProperty;
    }

    public int getContactId() {
        return contactIdProperty.get();
    }
    public IntegerProperty contactIdProperty() {
        return contactIdProperty;
    }

    public String getDescription() {
        return descriptionProperty.get();
    }
    public StringProperty descriptionProperty() {
        return descriptionProperty;
    }

    public String getLocation() {
        return locationProperty.get();
    }
    public StringProperty locationProperty() {
        return locationProperty;
    }

    public String getType() {
        return typeProperty.get();
    }
    public StringProperty typeProperty() {
        return typeProperty;
    }

    public String getStartTime() {
        return startTimeProperty.get();
    }
    public StringProperty startTimeProperty() {
        return startTimeProperty;
    }

    public String getEndTime() { return endTimeProperty.get(); }
    public StringProperty endTimeProperty() {
        return endTimeProperty;
    }

    /**
     * These are the setters
     */
    public void setId(int id) {
        this.idProperty.set(id);
    }
    public void setIdProperty(IntegerProperty idProperty) {
        this.idProperty = idProperty;
    }

    public void setCustomerId(int id) {
        this.customerIdProperty.set(id);
    }
    public void setCustomerIdProperty(IntegerProperty customerIdProperty) { this.customerIdProperty = customerIdProperty; }

    public void setTitle(String title) {
        this.titleProperty.set(title);
    }
    public void setTitleProperty(StringProperty titleProperty) {
        this.titleProperty = titleProperty;
    }

    public void setUserId(int id) {
        this.userIdProperty.set(id);
    }
    public void setUserIdProperty(IntegerProperty userIdProperty) {
        this.userIdProperty = userIdProperty;
    }

    public void setContactId(int id) {
        this.contactIdProperty.set(id);
    }
    public void setContactIdProperty(IntegerProperty contactIdProperty) { this.contactIdProperty = contactIdProperty; }

    public void setDescription(String description) {
        this.descriptionProperty.set(description);
    }
    public void setDescriptionProperty(StringProperty descriptionProperty) { this.descriptionProperty = descriptionProperty; }

    public void setLocation(String location) {
        this.locationProperty.set(location);
    }
    public void setLocationProperty(StringProperty locationProperty){
        this.locationProperty = locationProperty;
    }

    public void setType(String type) {
        this.typeProperty.set(type);
    }
    public void setTypeProperty(StringProperty typeProperty){
        this.typeProperty = typeProperty;
    }

    public void setStartTime(String startTime) {
        this.startTimeProperty.set(startTime);
    }
    public void setStartTimeProperty(StringProperty startTimeProperty){
        this.startTimeProperty = startTimeProperty;
    }

    public void setEndTime(String endTime) {
        this.endTimeProperty.set(endTime);
    }
    public void setEndTimeProperty(StringProperty endTimeProperty) {
        this.endTimeProperty = endTimeProperty;
    }

}
