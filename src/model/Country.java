package model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/*
* This defines the Country Class
* */
public class Country {
    private IntegerProperty idProperty;
    private StringProperty countryNameProperty;

    /**
     * This is the constructor
     * */
    public Country() {
        this.idProperty = new SimpleIntegerProperty();
        this.countryNameProperty = new SimpleStringProperty();
    }

   /**
    * These are the getters
    * */
    public IntegerProperty getIdProperty() {
        return idProperty;
    }
    public StringProperty getCountryNameProperty() {
        return countryNameProperty;
    }


    /**
     * These are the setters
     * */
    public void setCountryId(int id) {
        this.idProperty.set(id);
    }
    public void setCountryIdProperty(IntegerProperty idProperty) {
        this.idProperty = idProperty;
    }
    public void setCountryName(String name) {
        this.countryNameProperty.set(name);
    }
    public void setCountryNameProperty(StringProperty countryNameProperty) {
        this.countryNameProperty = countryNameProperty;
    }
}
