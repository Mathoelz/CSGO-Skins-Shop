package de.hsos.swa.entity;

import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.OneToOne;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

/**
 * This class models a weapon which can be sold at our shop
 * @author Maxim Zitnikowski
 */
@Entity
public class Weapon extends PanacheEntity{

    /** Name of the weapon. */
    public String weapon_name; //Aquamarine Revenge

    /** URL of the image which will be used by our shop. */
    public String icon_url; // -9a81dlWLwJ2UUGcVs_nsVtzdOEdtWwKGZZLQHTxDZ7I56KU0Zwwo4NUX4oFJZEHLbXH5ApeO4YmlhxYQknCRvCo04DEVlxkKgpot7HxfDhjxszJemkV09-5gZKKkPLLMrfFqWNU6dNoxL3H94qm3Ffm_RE6amn2ctWXdlI2ZwqB-FG_w-7s0ZK-7cjLzyE37HI8pSGKrIDGOAI 

    /** Weapon type which can be choosen e.g. knife etc. */
    public String weapon_type; // Rifle
    /** The name of the weapon model. */
    public String gun_type; //AK-47

    /** The status of the exterior giving info on how much the weapon has been used */
    public String exterior; //Battle-Scarred

    /** The rarity of the weapon */
    public String rarity; // Covert
    /** The rarity color code the weapon will be given */
    public String rarity_color; //eb4b4b
    
    /** The price at which the weapon can be bought */
    public float price;

    /** The discount which can be used to reduce the price */
    @OneToOne
    public Discount discount;

    /**
     * The default constructor
     */
    public Weapon() {
        
    }

    /**
     * The parametrized constructor 
     * 
     * @param name The name of the weapon
     * @param icon The icon code of the weapon
     * @param type The type of the weapon
     * @param model The name of the weapon model
     * @param exterior The status pf the exterior of the weapon
     * @param rarity The rarity of the weapon
     * @param color The color code for the designated rarity
     * @param price The price of the weapon at which it will be sold
     */
    public Weapon(String name, String icon, String type, String model, String exterior,
    String rarity, String color, float price) {
        this.weapon_name = name;
        this.icon_url = icon;
        this.weapon_type = type;
        this.gun_type = model;
        this.exterior = exterior;
        this.rarity = rarity;
        this.rarity_color = color;
        this.price = price;
        this.discount = null;
    }
    
    /**
     * Equals method to check if two weapons are equal
     * 
     * @param o Weapon object to check for equality
     */
    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof Weapon)) {
            return false;
        }
        Weapon weapon = (Weapon) o;
        return Objects.equals(weapon_name, weapon.weapon_name) && Objects.equals(icon_url, weapon.icon_url) && Objects.equals(weapon_type, weapon.weapon_type) && Objects.equals(gun_type, weapon.gun_type) && Objects.equals(exterior, weapon.exterior) && Objects.equals(rarity, weapon.rarity) && Objects.equals(rarity_color, weapon.rarity_color) && price == weapon.price;
    }

    /**
     * Hashcode method used to create a hashcode
     * 
     * @return A hashcode
     */
    @Override
    public int hashCode() {
        return Objects.hash(weapon_name, icon_url, weapon_type, gun_type, exterior, rarity, rarity_color, price);
    }


}