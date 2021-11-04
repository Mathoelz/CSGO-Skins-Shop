package de.hsos.swa.gateway;

import java.security.Principal;
import java.util.Collection;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import de.hsos.swa.entity.Weapon;
import de.hsos.swa.infrastructure.security.User;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

/**
 * This class supports the api by allowing us to manage all existing users in
 * our database
 * 
 * @author Maxim Zitnikowski
 */
@ApplicationScoped
public class UserRepository implements PanacheRepository<User> {

    /**
     * Create a new discount which will be added to the database
     * 
     * @param ctx The Securitycontext
     * 
     * @return The user you're looking for or null if failed
     */
    public User getUser(final SecurityContext ctx) {

        Principal userPrincipal = ctx.getUserPrincipal();
        String name = userPrincipal != null ? userPrincipal.getName() : "anonymous";
        User user = User.findByUsername(name);

        if (user != null) {
            return user;
        }

        return null;
    }

    /**
     * Get the cart of the designated user
     * 
     * @param user The user whose cart you want access to
     * 
     * @return the cart of the user or null if failed
     */
    public Collection<Weapon> getCart(final Principal user) {

        String name = user != null ? user.getName() : "anonymous";

        User us = User.findByUsername(name);
        if (us == null) {
            return null; // später 404
        }

        return us.cart;
    }

    /**
     * Add a weapon to the cart of the given user
     * 
     * @param user   The user whose cart you want to use
     * @param weapon The weapon which will be added to the cart
     * 
     * @return Response wether the operation failed or succeded
     */
    public boolean addWeaponToCart(User user, Weapon weapon) {

        for (Weapon wep : user.cart) {
            if (wep.id == weapon.id) {
                return false;
            }
        }

        user.addToCart(weapon);
        user.persist();
        return true;

    }

    /**
     * Remove a weapon of the cart from the given user
     * 
     * @param user   The user whose cart you want to use
     * @param weapon The weapon which will be removed from the cart
     * 
     * @return Response wether the operation failed or succeded
     */
    public Response removeWeaponFromCart(User user, Weapon weapon) {

        user.removeFromCart(weapon);
        user.persist();

        return Response.ok().build();
    }

    /**
     * Remove all weapons of the cart from the given user
     * 
     * @param user The user whose cart you want to use
     * 
     * @return Response wether the operation failed or succeded
     */
    public Response clearCart(User user) {
        user.cart.clear();

        return Response.ok().build();
    }

    /**
     * Get the owner of a weapon by weapon id 
     * 
     * @param weapon_id The user whose cart you want to use
     * 
     * @return The user who owns the weapon or null if failed
     */
    public User getOwnerOfWeapon(final Long weapon_id) {

        Collection<User> users = User.listAll();
        User owner = null;
        for (User user : users) {
            if (user.kunde.myWeapons.size() > 0) {
                Weapon wep = user.kunde.myWeapons.stream().filter(w -> w.id == weapon_id).findFirst().get();
                if (wep != null) {
                    owner = user;
                    break;
                }
            }
        }
        if (owner != null) {
            return owner;
        }
        return null;
    }
    
    
    
}