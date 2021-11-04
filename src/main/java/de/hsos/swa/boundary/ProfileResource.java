package de.hsos.swa.boundary;

import java.util.Collection;

import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
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

/**
 * This class contains all the 
 * api-functions related to user management.
 * @author: Maxim Zitnikowski
 */
@RequestScoped
@Path("/user/profile")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tags(value = @Tag(name = "user profile", description = "All the user profile methods"))
@SecurityScheme(securitySchemeName = "basicAuth", type = SecuritySchemeType.HTTP, scheme = "basic")
public class ProfileResource {

    @Inject
    UserRepository userRepository;

    @CheckedTemplate
    public static class Templates {
        public static native TemplateInstance show(User user, Collection<Weapon> weapons);

        public static native TemplateInstance edit(User user);

        public static native TemplateInstance addFunds(User user, float sum);
    }

    /**
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
     * * * * * * * * * * * * * * * * * * API Methods * * * * * * * * * * * * * * * *
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
     */

    /**
     * API-method: edit the user profile
     * 
     * @param ctx       SecurityContext
     * @param firstName user first name
     * @param lastName  user last name
     * 
     * @return Response if the operation failed or succeded
     */
    @PUT
    @Path("edit")
    @RolesAllowed("Customer")
    @SecurityRequirement(name = "basicAuth", scopes = {})
    @Transactional
    @Operation(summary = "Edit user profile.")
    public Response editProfileAPI(@Context SecurityContext ctx, @QueryParam("firstName") String firstName,
            @QueryParam("lastName") String lastName, @QueryParam("email") String email) {

        User user = this.userRepository.getUser(ctx);
        user.kunde.firstname = firstName;
        user.kunde.lastname = lastName;
        user.email = email;
        user.persist();

        return Response.status(Status.CREATED).entity(user).build();
    }

    /**
     * API-method: add money
     * 
     * @param ctx The securitycontext of the user
     * @param sum The amount of money to be added
     * 
     * @return Response if the operation failed or succeded
     */
    @POST
    @Path("addFunds")
    @RolesAllowed("Customer")
    @SecurityRequirement(name = "basicAuth", scopes = {})
    @Transactional
    @APIResponses(value = { @APIResponse(responseCode = "400", description = "user error"),
            @APIResponse(responseCode = "200", description = "funds were successfully added", content = @Content(schema = @Schema(implementation = User.class))) })
    @Operation(summary = "Add funds")
    public Response addFundsAPI(@Context SecurityContext ctx, @QueryParam("sum") float sum) {
        User user = this.userRepository.getUser(ctx);
        user.balance = user.balance + sum;
        user.persist();

        return Response.ok().build();
    }

    /**
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
     * * * * * * * * * * * * * * * * * * HTML Methods * * * * * * * * * * * * * * *
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
     */

    /**
     * HTML-method: show the user profile
     * 
     * @param ctx SecurityContext
     */
    @GET
    @Path("show.html")
    @RolesAllowed("Customer")
    @SecurityRequirement(name = "basicAuth", scopes = {})
    @Produces(MediaType.TEXT_HTML)
    @Operation(summary = "Show user profile.")
    public TemplateInstance showProfile(@Context SecurityContext ctx) {
        return Templates.show(this.userRepository.getUser(ctx), this.userRepository.getUser(ctx).kunde.myWeapons);
    }

    /**
     * HTML-method: edit the user profile
     * 
     * @param ctx SecurityContext
     */
    @GET
    @Path("edit.html")
    @RolesAllowed("Customer")
    @SecurityRequirement(name = "basicAuth", scopes = {})
    @Produces(MediaType.TEXT_HTML)
    @Operation(summary = "Edit user profile HTML.")
    public TemplateInstance editProfileHTML(@Context SecurityContext ctx) {
        return Templates.edit(this.userRepository.getUser(ctx));
    }

    /**
     * HTML-method: add funds
     * 
     * @param ctx SecurityContext of the user
     * @param sum The amount of money to be added to the user balance
     */
    @GET
    @Path("addFunds.html")
    @RolesAllowed("Customer")
    @SecurityRequirement(name = "basicAuth", scopes = {})
    @Produces(MediaType.TEXT_HTML)
    @Operation(summary = "Add funds HTML.")
    public TemplateInstance addFundsHTML(@Context SecurityContext ctx, @QueryParam("sum") float sum) {
        return Templates.addFunds(this.userRepository.getUser(ctx), sum);
    }

}