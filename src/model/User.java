package model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * This defines the User Class
 * */
public class User {
    private IntegerProperty idProperty;
    private StringProperty userNameProperty;
    private StringProperty passwordProperty;

    /**
     * This is the constructor
     * */
    public User(){
        this.idProperty = new SimpleIntegerProperty();
        this.userNameProperty = new SimpleStringProperty();
        this.passwordProperty = new SimpleStringProperty();
    }

    /**
     * These are the getters
     * */
    public IntegerProperty getIdProperty(){
        return idProperty;
    }
    public StringProperty getUserNameProperty(){
        return userNameProperty;
    }
    public StringProperty getPasswordProperty(){
        return passwordProperty;
    }

    /**
     * These are the setters
     * */
    public void setIdProperty(IntegerProperty idProperty) {
        this.idProperty = idProperty;
    }
    public void setUserNameProperty(StringProperty userNameProperty){
        this.userNameProperty = userNameProperty;
    }
    public void setPasswordProperty(StringProperty passwordProperty) {
        this.passwordProperty = passwordProperty;
    }

    //setters
    public void setUserId(int id) { this.idProperty.set(id); }
    public void setUserName(String userName){ this.userNameProperty.set(userName); }
    public void setPassword(String password) { this.passwordProperty.set(password); }

}


