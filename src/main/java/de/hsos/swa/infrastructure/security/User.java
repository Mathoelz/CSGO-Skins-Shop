package de.hsos.swa.infrastructure.security;

import java.util.Collection;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.Email;

import de.hsos.swa.entity.Kunde;
import de.hsos.swa.entity.Weapon;
import io.quarkus.elytron.security.common.BcryptUtil;
import io.quarkus.hibernate.orm.panache.PanacheEntity;

import io.quarkus.security.jpa.UserDefinition;
import io.quarkus.security.jpa.Username;
import io.quarkus.security.jpa.Password;
import io.quarkus.security.jpa.Roles;

/**
 * This class models a user and holds all
 * the needed information for users in our shop
 * @author Maxim Zitnikowski
 */
@Entity
@Table(name="USER")
@UserDefinition
public class User extends PanacheEntity {
    
    /** Username of the user. */
    @Username
    public String username;
    /** Password of the user. */
    @Password
    public String password;
    /** Role of the user which limits access in the api. */
    @Roles
    public String role;
    /** Email of the user to send mails to */
    @Email
    public String email;

    /** The account balance to buy weapons with. */
    public float balance;
   
    /** Further details of the user containing personal information. */
    @OneToOne public Kunde kunde;

    /** The cart to hold the weapons which a user can buy. */
    @OneToMany
    public Collection<Weapon> cart;
    
    /**
     * The default constructor
     */
    public User(){

    }

    /**
     * Parametrized Constructor to create a User with given values
     * 
     * @param username the user name
     * @param password the unencrypted password (it will be encrypted with bcrypt)
     * @param role the comma-separated roles
     */
    private User(String username, String password, String role){
        this.username = username;
        this.password = password;
        this.role = role;
    }

    /**
     * Adds a new user to our database
     * 
     * @param username Username of the user
     * @param password Password of the user
     * @param role role of the user
     * @param firstName Firstname of the User
     * @param lastName Lastname of the user
     * @param email The email of the user
     */
    public static void add(String username, String password, String role, String firstName, String lastName, String email){
        User user = new User(username, BcryptUtil.bcryptHash(password), role);

        user.kunde = new Kunde();
        user.kunde.firstname = firstName;
        user.kunde.lastname = lastName;
        user.kunde.persist();
        user.email = email;
        user.persist();
    }

    /**
     * Add a weapon to the cart Collection
     * @param weapon
     */
    public void addToCart(Weapon weapon){
        this.cart.add(weapon);
    }

    /**
     * Remove a weapon from the cart Collection
     * @param weapon
     */
    public void removeFromCart(Weapon weapon){
        this.cart.remove(weapon);
    }

    /**
     * Accumulates all weapons prices and subtracts 
     * their discounts to give the total value of the
     * weapons currently in the cart
     */
    public float getAllWeponsPrice(){

        float totalPrice = 0;
        for(Weapon weapon: cart){
            totalPrice += weapon.price;
            if(weapon.discount != null){
                totalPrice -= weapon.discount.value;
            }
                
        }
        return totalPrice;
    }

    /**
     * Find specific user by username
     * @param username 
     * 
     * @return The user with the given username
     */
    public static User findByUsername(String username){

        Collection<User> users = listAll();
        User user = users.stream().filter(u -> u.username.equals(username)).findFirst().get();

        return user;
    }

    /**
     * Find specific user by username
     * 
     * @param totalCartSum The total sum of the weapon prices in cart
     * 
     * @return True or false wether the total Sum of 
     * the cart exceeds the current balance
     */
    public boolean hasEnoughBalance(float totalCartSum){
        if(this.balance < totalCartSum){
            return false;
        }else{
            return true;
        }
         
    }

}
