package de.hsos.swa.boundary;

import java.util.Collection;

import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.enums.SecuritySchemeType;
import org.eclipse.microprofile.openapi.annotations.security.SecurityRequirement;
import org.eclipse.microprofile.openapi.annotations.security.SecurityScheme;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.eclipse.microprofile.openapi.annotations.tags.Tags;

import de.hsos.swa.entity.Discount;
import de.hsos.swa.entity.Weapon;
import de.hsos.swa.gateway.DiscountRepository;
import de.hsos.swa.gateway.WeaponRepository;

/**
 * This class contains all the 
 * api-functions related to the class discount.
 * @author: Mathias Hölz
 */
@RequestScoped
@Path("/discount")
@Produces(MediaType.APPLICATION_JSON)
@Tags(value = @Tag(name = "discount", description = "All the discount methods"))
@SecurityScheme(securitySchemeName = "basicAuth", type = SecuritySchemeType.HTTP, scheme = "basic")
public class DiscountResource {
    
    @Inject DiscountRepository discountRepository;
    @Inject WeaponRepository weaponRepository;

    /**
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
     * * * * * * * * * * * * * * * * * * API Methods * * * * * * * * * * * * * * * *
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
     */

    /**
     * API-method: returns all existing discount objects
     * 
     * @return All discounts.
     */
    @GET
    @Path("/getAll")
    @RolesAllowed("Admin")
    @SecurityRequirement(name = "basicAuth", scopes = {})
    @Operation(summary = "Returns all existing discount objects.")
    public Collection<Discount> getAllDiscounts() {
       return discountRepository.getAllDiscounts();
    }

    /**
     * API-method: returns the discount object with the designated id
     * 
     * @param id    The id of the discount you're looking for
     * 
     * @return Discount with the given id
     */
    @GET
    @Path("/{id}")
    @RolesAllowed("Admin")
    @SecurityRequirement(name = "basicAuth", scopes = {})
    @Operation(summary = "Returns the discount object with the designated id.")
    public Discount getDiscount(@PathParam("id") long id) {
        return discountRepository.getById(id);
    }

    /**
     * API-method: Creates a new discount and 
     * assigns it to the weapon with the designated id
     * 
     * @param id    The id of the discount you're looking for
     * @param value The value of the discount
     * 
     * @return True when the function succeded, false when it failed
     * because the weapon wasn't found.
     */
    @PUT
    @Path("/create")
    @Transactional
    @RolesAllowed("Customer")
    @SecurityRequirement(name = "basicAuth", scopes = {})
    @Operation(summary = "Creates a new discount and assigns it to the weapon with the designated id")
    public boolean createDiscount(@QueryParam("value") float value,
    @QueryParam("id") long id) {
        Discount dis = new Discount(value);
        Weapon wep = weaponRepository.findById(id);
        if(wep != null){
            wep.discount = dis;
            discountRepository.createDiscount(dis);
            return true;
        }
        else{
            return false;
        }
    
    }

    /**
     * API-method: Changes the value of the 
     * discount with the designated id
     * 
     * @param id    The id of the discount you're looking for
     * @param value The new value of the discount
     * 
     * @return True when the method succeded, false when 
     * the discount wasn't found.
     */
    @PUT @Path("setDiscount")
    @Transactional
    @RolesAllowed("Customer")
    @SecurityRequirement(name = "basicAuth", scopes = {})
    @Operation(summary = "Changes the value of the discount with the designated id")
    public boolean setWeaponDiscount(@QueryParam("value") float value,
    @QueryParam("id") long id) {
        Discount dis = discountRepository.findById(id);
        if(dis != null) {
            dis.value = value;
            dis.valid = true;
            discountRepository.persist(dis);
            return true;
        }
        else {
            return false;
        }            
    }

    /**
     * API-method: Lets a customer use a discount code
     * 
     * @param code    The hashcode of the discount you want to use
     * 
     * @return True when the discount was used successfully, false 
     * when the discount was already used or the weapon wasn't found.
     */
    @PUT @Path("adjustPrice")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    @RolesAllowed("Customer")
    @SecurityRequirement(name = "basicAuth", scopes = {})
    @Operation(summary = "Lets a customer use a discount code")
    public boolean adjustWeaponPrice(@QueryParam("DiscountCode") int code) {
        Discount dis = discountRepository.find("code", code).firstResult();
        Weapon wep = weaponRepository.find("discount", dis).firstResult();
        if(wep != null && wep.discount.valid == true) {
            wep.discount.valid = false;
            weaponRepository.persist(wep);
            return true;
        }
        else{
            return false;
        }
    }
}
