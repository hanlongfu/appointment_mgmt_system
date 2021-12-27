package model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;


/**
 * This defines the Contact Class
 * */
public class Contact {

    private IntegerProperty idProperty;
    private StringProperty nameProperty;
    private StringProperty emailProperty;

    /**
     * These are the getters
     * */
    public IntegerProperty getIdProperty(){
        return idProperty;
    }
    public StringProperty getNameProperty(){
        return nameProperty;
    }
    public StringProperty getEmailProperty(){
        return emailProperty;
    }

    /**
     * These are the setters
     * */
    public void setIdProperty(IntegerProperty idProperty){
        this.idProperty = idProperty;
    }
    public void setNameProperty(StringProperty nameProperty) {
        this.nameProperty = nameProperty;
    }
    public void setEmailProperty(StringProperty emailProperty){
        this.emailProperty = emailProperty;
    }

    public void setId(int id) {
        this.idProperty.set(id);
    }
    public void setName(String name){
        this.nameProperty.set(name);
    }
    public void setEmail(String email) {
        this.emailProperty.set(email);
    }

    /*
    * This is the constructor
    * */
    public Contact(){
        this.idProperty = new SimpleIntegerProperty();
        this.nameProperty = new SimpleStringProperty();
        this.emailProperty = new SimpleStringProperty();
    }


}
