package de.hsos.swa.entity;

import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.Table;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

/**
 * This class models a discount coupon which can be used
 * to reduce the price of a weapon
 * @author Mathias Hölz
 */
@Entity
@Table(name = "DISCOUNT")
public class Discount extends PanacheEntity {

    /** Wether the code has already been used or not. */
    public boolean valid;
    /** The hashcode to use the code. */
    public int code;
    /** The value which will be subtracted from a weapons price. */
    public float value;

    /**
     * The default constructor
     */
    public Discount() {

    }

    /**
     * Parametrized constructor
     * 
     * @param value Lets a user create a discount with a given value
     */
    public Discount(float value) {
        this.value = value;
        this.valid = true;
        this.code = this.hashCode();
    }

    /**
     * Equals method to check if two discounts are equal
     * 
     * @param o Discount object to check for equality
     */
    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof Discount)) {
            return false;
        }
        Discount discount = (Discount) o;
        return valid == discount.valid && Objects.equals(code, discount.code) && value == discount.value;
    }

    /**
     * Hashcode method used to create the hashcode for the discount
     * 
     * @return A hashcode which can be used for a discount
     */
    @Override
    public int hashCode() {
        return Objects.hash(valid, code, value);
    }

    
}
