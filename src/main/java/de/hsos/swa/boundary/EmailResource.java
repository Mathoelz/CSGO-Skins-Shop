package de.hsos.swa.boundary;

import java.util.concurrent.CompletionStage;

import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.enums.SecuritySchemeType;
import org.eclipse.microprofile.openapi.annotations.security.SecurityRequirement;
import org.eclipse.microprofile.openapi.annotations.security.SecurityScheme;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.eclipse.microprofile.openapi.annotations.tags.Tags;

import de.hsos.swa.entity.Weapon;
import de.hsos.swa.gateway.UserRepository;
import de.hsos.swa.infrastructure.security.User;
import io.quarkus.mailer.MailTemplate;
import io.quarkus.mailer.Mailer;
import io.quarkus.mailer.reactive.ReactiveMailer;
import io.quarkus.qute.api.CheckedTemplate;

/**
 * This class contains all the 
 * api-functions related to sending Emails.
 * @author: Maxim Zitnikowski
 */
@RequestScoped
@Path("/email")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tags(value = @Tag(name = "email", description = "All the user email methods"))
@SecurityScheme(securitySchemeName = "basicAuth", type = SecuritySchemeType.HTTP, scheme = "basic")
public class EmailResource {

    @Inject
    Mailer mailer;

    @Inject
    ReactiveMailer reactiveMailer;

    @Inject
    UserRepository userRepository;

    

    
    @CheckedTemplate
    static class Templates {
        public static native MailTemplate.MailTemplateInstance confirm(User user);

        public static native MailTemplate.MailTemplateInstance recommend(String email, Weapon weapon);

        public static native MailTemplate.MailTemplateInstance discount(String username, String email, Weapon weapon);

    }

    /**
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
     * * * * * * * * * * * * * * * * * * HTML Methods * * * * * * * * * * * * * * *
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
     */

    /**
     * HTML-method: sends the given user an email
     * confirming their purchase
     * 
     * @param user User to receive the email
     */
    public CompletionStage<Response> confirm(User user) {
        // the template looks like: Hello {name}!
        return Templates.confirm(user).to(user.email).subject("Successfull Payment").send()
                .thenApply(x -> Response.accepted().build());
    }

    /**
     * HTML-method: sends a person an email
     * recommendation for one of our weapons
     * 
     * @param email The full email adress of the receiver: example@gmail.com
     * @param weapon_id The id of the weapon which will be recommmended
     */
    @POST
    @Path("recommend")
    @Operation(summary = "Recommend a weapon to an friend.")
    public CompletionStage<Response> recommend(@QueryParam("email") String email,
            @QueryParam("weapon_id") Long weapon_id) {
        Weapon weapon = Weapon.findById(weapon_id);
        return Templates.recommend(email, weapon).to(email).subject("This skin was recommended to you by from").send()
                .thenApply(x -> Response.accepted().build());
    }


    /**
     * HTML-method: sends the owner of a weapon an email
     * inquiring for a discount
     * 
     * @param ctx The user who is going to send the email
     * @param weapon_id The id of the weapon which will be recommmended
     */
    @POST
    @Path("discount")
    @RolesAllowed("Customer")
    @SecurityRequirement(name = "basicAuth", scopes = {})
    @Operation(summary = "Ask weapon owner for discount.")
    public CompletionStage<Response> discount(@Context SecurityContext ctx, @QueryParam("weapon_id") Long weapon_id) {
        Weapon weapon = Weapon.findById(weapon_id);
        User user = userRepository.getUser(ctx);

        // get the owner of the weapon by weapon_id
        User owner = userRepository.getOwnerOfWeapon(weapon_id);

        return Templates.discount(user.username, user.email, weapon).to(owner.email)
        .subject(user.username + " asked you for a discount").send()
        .thenApply(x -> Response.accepted().build());
    }

}
