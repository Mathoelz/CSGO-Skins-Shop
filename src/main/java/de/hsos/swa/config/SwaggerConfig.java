package de.hsos.swa.config;

import org.eclipse.microprofile.openapi.annotations.OpenAPIDefinition;
import org.eclipse.microprofile.openapi.annotations.info.Contact;
import org.eclipse.microprofile.openapi.annotations.info.Info;
import org.eclipse.microprofile.openapi.annotations.info.License;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import javax.ws.rs.core.Application;

 /**
 * SwaggerConfig
 * 
 * Source: 
 * @see <a href= "https://quarkus.io/guides/openapi-swaggerui">QUARKUS - USING OPENAPI AND SWAGGER UI</a>
 */

@OpenAPIDefinition(
        tags = {
                @Tag(name = "weapon", description = "Weapon operations."),
                @Tag(name = "user", description = "User operations."),
                @Tag(name = "user profile", description = "User Profile operations."),
                @Tag(name = "email", description = "Email operations."),
                @Tag(name = "discount", description = "Discount operations.")
        },
        info = @Info(
                title = "User API with Quarkus by MM-Skins",
                version = "1.1.4",
                contact = @Contact(
                        name = "Maxim Zitnikowski, Mathias Hölz",
                        url = "http://geekyhacker.com/contact",
                        email = "maxim.zitnikowski@hs-osnabrueck.de, mathias.hoelz@hs-osnabrueck.de"),
                license = @License(
                        name = "MI",
                        url = "https://opensource.org/licenses/MIT"))
)
public class SwaggerConfig extends Application {

}