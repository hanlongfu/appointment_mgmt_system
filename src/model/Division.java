package model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;


/*
* This defines the Division Class
* */
public class Division {
    private IntegerProperty idProperty;
    private StringProperty divisionNameProperty;
    private IntegerProperty countryIdProperty;

    /**
     * This is the constructor
     * */
    public Division(){
        this.idProperty = new SimpleIntegerProperty();
        this.divisionNameProperty = new SimpleStringProperty();
        this.countryIdProperty = new SimpleIntegerProperty();
    }

    /*
    * These are the getters
    * */
    public IntegerProperty getCountryIdProperty(){
        return countryIdProperty;
    }
    public IntegerProperty getIdProperty(){
        return idProperty;
    }
    public StringProperty getDivisionNameProperty(){
        return divisionNameProperty;
    }

    /**
     * These are the setters
     * */
    public void setCountryIdProperty(IntegerProperty countryIdProperty){
        this.countryIdProperty = countryIdProperty;
    }
    public void setCountryId(int id){
        this.countryIdProperty.set(id);
    }

    public void setIdProperty(IntegerProperty idProperty){
        this.idProperty = idProperty;
    }
    public void setId(int id){
        this.idProperty.set(id);
    }

    public void setDivisionNameProperty(StringProperty divisionNameProperty){ this.divisionNameProperty = divisionNameProperty; }
    public void setDivisionName(String divisionName){
        this.divisionNameProperty.set(divisionName);
    }

}
