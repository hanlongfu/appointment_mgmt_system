package model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * This defines the Customer Class
 * */
public class Customer {
    private IntegerProperty idProperty;
    private IntegerProperty divisionIdProperty;
    private StringProperty nameProperty;
    private StringProperty addressProperty;
    private StringProperty postalCodeProperty;
    private StringProperty phoneNumberProperty;


    /**
     * This is the constructor
     * */
    public Customer() {
        this.idProperty =new SimpleIntegerProperty();
        this.divisionIdProperty = new SimpleIntegerProperty();
        this.nameProperty = new SimpleStringProperty();
        this.addressProperty = new SimpleStringProperty();
        this.postalCodeProperty = new SimpleStringProperty();
        this.phoneNumberProperty = new SimpleStringProperty();
    }


   /**
    * These are the getters
    **/
    public IntegerProperty getIdProperty() {
        return idProperty;
    }

    public IntegerProperty getDivisionIdProperty() {
        return divisionIdProperty;
    }

    public StringProperty getNameProperty() {
        return nameProperty;
    }

    public StringProperty getAddressProperty() {
        return addressProperty;
    }

    public StringProperty getPostalCodeProperty() {
        return postalCodeProperty;
    }

    public StringProperty getPhoneNumberProperty() {
        return phoneNumberProperty;
    }


    /**
     * These are the setters
     **/
    public void setIdProperty(int idProperty) {
        this.idProperty.set(idProperty);
    }

    public void setDivisionIdProperty(int divisionIdProperty) {
        this.divisionIdProperty.set(divisionIdProperty);
    }

    public void setNameProperty(String nameProperty) {
        this.nameProperty.set(nameProperty);
    }

    public void setAddressProperty(String addressProperty) {
        this.addressProperty.set(addressProperty);
    }

    public void setPostalCodeProperty(String postalCodeProperty) {
        this.postalCodeProperty.set(postalCodeProperty);
    }

    public void setPhoneNumberProperty(String phoneNumberProperty) { this.phoneNumberProperty.set(phoneNumberProperty); }
}
