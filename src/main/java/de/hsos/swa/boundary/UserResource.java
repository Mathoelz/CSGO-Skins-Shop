package de.hsos.swa.boundary;

import java.security.Principal;
import java.util.Collection;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.SecurityContext;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.enums.SecuritySchemeType;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.security.SecurityRequirement;
import org.eclipse.microprofile.openapi.annotations.security.SecurityScheme;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.eclipse.microprofile.openapi.annotations.tags.Tags;

import de.hsos.swa.entity.Weapon;
import de.hsos.swa.gateway.UserRepository;
import de.hsos.swa.infrastructure.security.User;
import io.quarkus.qute.TemplateInstance;
import io.quarkus.qute.api.CheckedTemplate;
import io.quarkus.runtime.StartupEvent;

/**
 * This class contains all the 
 * api-functions related to the class user.
 * @author: Maxim Zitnikowski
 * 
 * Sources: 
 * @see <a href= "https://kostenko.org/blog/2020/02/jwt-openapi-microprofile-quarkus.html">Well secured and documented REST API with Eclipse Microprofile and Quarkus</a>
 *      <a href=https://github.com/OAI/OpenAPI-Specification/blob/master/versions/3.0.0.md#security-scheme-object">OpenAPI Specification</a>
 */
@ApplicationScoped
@Path("/user")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tags(value = @Tag(name = "user", description = "All the user methods"))
@SecurityScheme(securitySchemeName = "basicAuth", type = SecuritySchemeType.HTTP, scheme = "basic")
public class UserResource {

    @Inject
    UserRepository userRepository;
    @Inject
    EmailResource emailResource;

    @CheckedTemplate
    public static class Templates {
        public static native TemplateInstance register();

        public static native TemplateInstance cart(User user, Collection<Weapon> weapons);

        public static native TemplateInstance sell();
    }

    /**
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
     * * * * * * * * * * * * * * * * * * API Methods * * * * * * * * * * * * * * * *
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
     */

    /**
     * API-method: when the API is initialized this method
     * inserts 3 example users in our database which we've
     * used for testing
     * 
     */
    @Transactional
    public void onStart(@Observes StartupEvent evt) {
        // reset and load all test users
        User.deleteAll();
        User.add("admin", "admin", "Admin", "Max", "Mustermann", "csgo.shop2021@gmail.com");
        User.add("test", "test", "Customer", "Max", "Mustermann", "csgo.shop2021@gmail.com");
        User.add("kunde", "kunde", "Customer", "Max", "Mustermann", "csgo.shop2021@gmail.com");

        User test = userRepository.find("username", "test").firstResult();

        Collection<Weapon> weapons = Weapon.listAll();
        test.kunde.myWeapons = weapons;
    }

    /**
     * API-method: get a list of all existing customers in the database
     * 
     * @return Collection<User> The list containing all existing users
     * 
     */
    @GET
    @Path("getAll")
    @PermitAll
    @Operation(summary = "Get list of all customers.")
    public Collection<User> getCustomers(@Context SecurityContext ctx) {
        return User.listAll();
    }

    /**
     * API-method: Adds a new customer to the database
     *
     * @param username Username of the new customer
     * @param password Password of the new customer
     * @param firstName Firstname of the new customer
     * @param lastName  Lastname of the new customer
     * @param email E-mail adress of the new customer
     * 
     */
    @POST
    @Path("register")
    @PermitAll
    @Transactional
    @Operation(summary = "Register new unser.")
    public void regiter(@NotNull @QueryParam("username") String username,
            @NotNull @QueryParam("password") String password, @QueryParam("firstname") @DefaultValue("") String firstname,
            @QueryParam("lastname") @DefaultValue("") String lastname, @QueryParam("email") @DefaultValue("") String email) {
        User.add(username, password, "Customer", firstname, lastname, email);
    }

    /**
     * API-method: Deletes the designated user from the database
     * 
     * @param ctx The securitycontext belonging to the user 
     */
    @DELETE
    @Path("delete")
    @RolesAllowed("Customer")
    @Transactional
    @SecurityRequirement(name = "basicAuth", scopes = {})
    @Operation(summary = "Delete unser.")
    public void deleteUser(@Context SecurityContext ctx) {
        User user = userRepository.getUser(ctx);
        User.deleteById(user.id);
    }
    
    /**
     * API-method: Method completes an order by changing weapon
     * ownership and subtracting weapon prices from the users
     * balance
     * 
     * @param ctx The securitycontext belonging to the user
     * 
     * @return 0, for cart is Empty
     *         1, if the order was successfully completed
     *         2, if the user has not enough money to complete the order
     */
    @PUT
    @Path("completeOrder")
    @RolesAllowed("Customer")
    @SecurityRequirement(name = "basicAuth", scopes = {})
    @Transactional
    @APIResponses(value = { @APIResponse(responseCode = "404", description = "Unauthorized Error"),
            @APIResponse(responseCode = "200", description = "User Order successfully completed", content = @Content(schema = @Schema(implementation = User.class))) })
    @Operation(summary = "Complete user order.")
    public int completeOrder(@Context SecurityContext ctx) {
        User user = userRepository.getUser(ctx);

        float sum = 0;
        // if user cart is empty
        if (user.cart.isEmpty()) {
            // 0: user cart is empty
            return 0;
        }

        for (Weapon weapon : user.cart) {
            sum += weapon.price;
                if (weapon.discount != null) {
                   sum -= weapon.discount.value; 
                }
        }

        if (user.hasEnoughBalance(sum)) {
            user.balance = user.balance - sum;
            user.persist();

            emailResource.confirm(user);

            for(Weapon weapon : user.cart){
                User seller = this.userRepository.getOwnerOfWeapon(weapon.id);
                seller.kunde.myWeapons.remove(weapon);
                seller.persist();
                weapon.discount = null;
                user.kunde.myWeapons.add(weapon);
            }
            user.cart.clear();
            user.persist();
            // 1: payment is successfull
            return 1;
        }

        // 2: not enough money
        return 2;
    }

    /**
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
     * * * * * * * * * * * * * * * * * * HTML Methods * * * * * * * * * * * * * * *
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
     */

    /**
     * HTML-method: The webpage for registering a new user 
     */ 
    @GET
    @Path("register.html")
    @PermitAll
    @Produces(MediaType.TEXT_HTML)
    @Operation(summary = "Register new user HTML.")
    public TemplateInstance registerWeb() {
        return Templates.register();
    }

    /**
     * HTML-method: The webpage showing for managing the cart of the user
     * 
     * @param ctx The securitycontext belonging to a user
     */
    @GET
    @Path("cart.html")
    @RolesAllowed("Customer")
    @Produces(MediaType.TEXT_HTML)
    @SecurityRequirement(name = "basicAuth", scopes = {})
    @Operation(summary = "Get user cart HTML.")
    public TemplateInstance getCart(@Context SecurityContext ctx) {
        Principal user = ctx.getUserPrincipal();
        return Templates.cart(this.userRepository.getUser(ctx), this.userRepository.getCart(user));
    }

    /**
     * HTML-method: The webpage for adding a new weapon to sell
     * 
     */
    @GET
    @Path("sell.html")
    @RolesAllowed("Customer")
    @Produces(MediaType.TEXT_HTML)
    @SecurityRequirement(name = "basicAuth", scopes = {})
    @Operation(summary = "Sell a weapon HTML.")
    public TemplateInstance sellWeapon() {
        return Templates.sell();
    }

}