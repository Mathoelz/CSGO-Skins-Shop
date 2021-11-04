package de.hsos.swa.entity;

import java.util.Collection;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

/**
 * This class models a customer who will be given access 
 * to our shop
 * @author Maxim Zitnikowski
 */
@Entity
@Table(name="CUSTOMER")
public class Kunde extends PanacheEntity {
    
    /** First name of the customer */
    public String firstname;
    /** Last name of the customer */
    public String lastname;

    /** The weapons that are currently in possesion of the customer */
    @OneToMany
    public Collection<Weapon> myWeapons;

    /**
     * The default constructor
     */
    public Kunde(){

    }

    /**
     * A basic getter for first name
     * 
     * @return the first name of the customer
     */
    public String getFirstname() {
        return this.firstname;
    }

    /**
     * A basic setter for first name
     * 
     * @param firstname First name of the customer
     */
    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    /**
     * A basic getter for last name
     * 
     * @return the last name of the customer
     */
    public String getLastname() {
        return this.lastname;
    }

    /**
     * A basic setter for last name
     * 
     * @param lastname Last name of the customer
     */
    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

}
