package de.hsos.swa.infrastructure.security;

import javax.persistence.Entity;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import io.quarkus.security.jpa.RolesValue;

/**
 * This class models a role for a user to grant
 * limited access to our api
 * @author Maxim Zitnikowski
 */
@Entity
public class Role extends PanacheEntity {

    /** Name of the Role which will be given to the user */
    @RolesValue
    public String role;

    /**
     * The default constructor
     */
    public Role() {
    }
    
    /**
     * The parametrized constructor
     * 
     * @param role The name of the role
     */
    public Role(String role) {
        this.role = role;
    }

}