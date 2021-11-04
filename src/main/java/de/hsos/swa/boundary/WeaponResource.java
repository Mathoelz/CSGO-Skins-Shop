package de.hsos.swa.boundary;

import java.util.Collection;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
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
import de.hsos.swa.gateway.WeaponRepository;
import de.hsos.swa.infrastructure.security.User;
import io.quarkus.qute.TemplateInstance;
import io.quarkus.qute.api.CheckedTemplate;
import io.quarkus.runtime.StartupEvent;

/**
 * This class contains all the 
 * api-functions related to the class weapon.
 * @author: Maxim Zitnikowski
 */
@RequestScoped
@Path("/weapon")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tags(value = @Tag(name = "weapon", description = "All the weapon methods"))
@SecurityScheme(securitySchemeName = "basicAuth", type = SecuritySchemeType.HTTP, scheme = "basic")
public class WeaponResource {

    @Inject
    WeaponRepository weaponRepository;

    @Inject
    UserRepository userRepository;

    @CheckedTemplate
    public static class Templates {
        public static native TemplateInstance getAll(Collection<Weapon> weapons);

        public static native TemplateInstance weapon(Weapon weapon);

        public static native TemplateInstance filter(Collection<Weapon> weapons, String value);
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
        Weapon.deleteAll();
        weaponRepository.addNewWeapon(new Weapon("Dragon Lore", "-9a81dlWLwJ2UUGcVs_nsVtzdOEdtWwKGZZLQHTxDZ7I56KU0Zwwo4NUX4oFJZEHLbXH5ApeO4YmlhxYQknCRvCo04DEVlxkKgpot621FAR17P7NdTRH-t26q4SZlvD7PYTQgXtu5Mx2gv2P9o6migzl_Us5ZmCmLYDDJgU9NA6B81S5yezvg8e-7cycnXJgvHZx5WGdwUJqz1Tl4g", "Sniper Rifle", "AWP", "Factory New", "Covert", "eb4b4b", 1995.42f));
        weaponRepository.addNewWeapon(new Weapon("Fire Serpent", "-9a81dlWLwJ2UUGcVs_nsVtzdOEdtWwKGZZLQHTxDZ7I56KU0Zwwo4NUX4oFJZEHLbXH5ApeO4YmlhxYQknCRvCo04DEVlxkKgpot7HxfDhjxszOeC9H_9mkhIWFg8j1OO-GqWlD6dN-teXI8oTht1i1uRQ5fWDwLYbAdVBqYVHRrwC2kO7rhpLq6J_IzXE2unFxs3-JmkG200ofZ-JxxavJKZiOt4k", "Rifle", "AK-47", "Factory New", "Covert", "eb4b4b", 1750f));
        weaponRepository.addNewWeapon(new Weapon("Doppler", "-9a81dlWLwJ2UUGcVs_nsVtzdOEdtWwKGZZLQHTxDZ7I56KU0Zwwo4NUX4oFJZEHLbXH5ApeO4YmlhxYQknCRvCo04DEVlxkKgpovbSsLQJf3qr3czxb49KzgL-KmsjwPKvBmm5D19V5i_rEobP5gVO8vywwMiukcZiddFU3NA6FqQe5xLu51pK1us6amiMyuyIr4XvYmkazgBoaZuVo0fWaVxzAUDlCnIuN", "Knife", "M9 Bayonet", "Factory New", "Covert", "eb4b4b", 7230f));
        weaponRepository.addNewWeapon(new Weapon("Gamma Doppler", "-9a81dlWLwJ2UUGcVs_nsVtzdOEdtWwKGZZLQHTxDZ7I56KU0Zwwo4NUX4oFJZEHLbXH5ApeO4YmlhxYQknCRvCo04DEVlxkKgpotLu8JAllx8zJfAJF7dG7lb-PmOfkP77DqXtZ6dZ02dbN_Iv9nGu4qgE7NnfxcYSTcVA8NVGD-Vnrwri808S4uczAzHsxuyBzsXbUzRfihh0ea7Rum7XAHqV4wf13", "Knife", "Bayonet", "Factory New", "Covert", "eb4b4b", 420f));
        weaponRepository.addNewWeapon(new Weapon("Aquamarine Revenge", "-9a81dlWLwJ2UUGcVs_nsVtzdOEdtWwKGZZLQHTxDZ7I56KU0Zwwo4NUX4oFJZEHLbXH5ApeO4YmlhxYQknCRvCo04DEVlxkKgpot7HxfDhjxszJemkV09-5gZKKkPLLMrfFqWNU6dNoxL3H94qm3Ffm_RE6amn2ctWXdlI2ZwqB-FG_w-7s0ZK-7cjLzyE37HI8pSGKrIDGOAI", "Rifle", "AK-47", "Battle-Scarred", "Covert", "eb4b4b", 33.31f));
        weaponRepository.addNewWeapon(new Weapon("Case Hardened", "-9a81dlWLwJ2UUGcVs_nsVtzdOEdtWwKGZZLQHTxDZ7I56KU0Zwwo4NUX4oFJZEHLbXH5ApeO4YmlhxYQknCRvCo04DEVlxkKgpovbSsLQJf0ebcZThQ6tCvq4GaqPj9P77VqWZU7Mxkh9bN9J7yjRrk-BJtMGCncdKccgU7aVDY-lfqyLy705a96ZTJynBl6HYg5y6MyR3hn1gSOVnglyPR", "Knife", "Butterfly Knife", "Field-Tested", "Covert", "eb4b4b", 1262.3f));
        weaponRepository.addNewWeapon(new Weapon("Blaze", "-9a81dlWLwJ2UUGcVs_nsVtzdOEdtWwKGZZLQHTxDZ7I56KU0Zwwo4NUX4oFJZEHLbXH5ApeO4YmlhxYQknCRvCo04DEVlxkKgposr-kLAtl7PLJTjtO7dGzh7-HnvD8J4Tdl3lW7Yt1jriVpY-migfh8hBtZTqgcI7Aewc2MgnWqwW-k-zph569uZyfnHMwpGB8slBfgSNs", "Pistol", "Desert Eagle", "Factory New", "Restricted", "8847ff", 425f));
        weaponRepository.addNewWeapon(new Weapon("Knight", "-9a81dlWLwJ2UUGcVs_nsVtzdOEdtWwKGZZLQHTxDZ7I56KU0Zwwo4NUX4oFJZEHLbXH5ApeO4YmlhxYQknCRvCo04DEVlxkKgpou-6kejhz2v_Nfz5H_uO3mb-GkuP1P6jummJW4NFOhujT8om72VGy-kJpZjr0JYSWdg9sYwmBrwS2wOnt1JXo7Zqfm3M2vCJ35HzbnQv330-9f4-Ixw", "Rifle", "M4A1-S", "Factory New", "Classified", "d32ce6", 730f));
        weaponRepository.addNewWeapon(new Weapon("Emerald Dragon", "-9a81dlWLwJ2UUGcVs_nsVtzdOEdtWwKGZZLQHTxDZ7I56KU0Zwwo4NUX4oFJZEHLbXH5ApeO4YmlhxYQknCRvCo04DEVlxkKgpopuP1FAR17PfacDpN4uOmzdC0leX1JbTum25V4dB8teXA54vwxgTm_hFqNjzzJI_DcA43M1uDqQW8w-rp1JG_tZqfmCM1vyQgt3vazhepwUYb67ogGAs", "SMG", "P90", "Field-Tested", "Classified", "d32ce6", 52.80f));
        weaponRepository.addNewWeapon(new Weapon("Howl", "-9a81dlWLwJ2UUGcVs_nsVtzdOEdtWwKGZZLQHTxDZ7I56KU0Zwwo4NUX4oFJZEHLbXH5ApeO4YmlhxYQknCRvCo04DEVlxkKgpou-6kejhjxszFJTwT09S5g4yCmfDLP7LWnn9u5MRjjeyP9tqhiQ2yqEo6Mmn3doPBcwZqZQrRr1O-we_sgMO5tZ_BzCFr6ycltmGdwULa1vGJFg", "Rifle", "M4A4", "Factory New", "Contraband", "e4ae39", 2400f));
        weaponRepository.addNewWeapon(new Weapon("Medusa", "-9a81dlWLwJ2UUGcVs_nsVtzdOEdtWwKGZZLQHTxDZ7I56KU0Zwwo4NUX4oFJZEHLbXH5ApeO4YmlhxYQknCRvCo04DEVlxkKgpot621FAR17P7NdShR7eO3g5C0m_7zO6_ummpD78A_2rzCo4qgiwLjqkE6MT_0cIaRcAA9Zl3W8gPvw7-9h5PpuJmbm3Jr6T5iuyhpU6MIVQ", "Sniper Rifle", "AWP", "Minimal Wear", "Covert", "eb4b4b", 1876.11f));
        weaponRepository.addNewWeapon(new Weapon("Kill Confirmed", "-9a81dlWLwJ2UUGcVs_nsVtzdOEdtWwKGZZLQHTxDZ7I56KU0Zwwo4NUX4oFJZEHLbXH5ApeO4YmlhxYQknCRvCo04DEVlxkKgpoo6m1FBRp3_bGcjhQ09-jq5WYh8j_OrfdqWhe5sN4mOTE8bP5gVO8vywwMiukcZjEcVc5M1CG-1jtyLi9jJW97pzBmnM27nQlsSvfnkGzhU1OPeY8h6CeVxzAUEsa6pHf", "Pistol", "USP-S", "Factory New", "Covert", "eb4b4b", 116.54f));
        weaponRepository.addNewWeapon(new Weapon("Knight", "-9a81dlWLwJ2UUGcVs_nsVtzdOEdtWwKGZZLQHTxDZ7I56KU0Zwwo4NUX4oFJZEHLbXH5ApeO4YmlhxYQknCRvCo04DEVlxkKgpou-6kejhz2v_Nfz5H_uO3mb-GkuP1P6jummJW4NFOhujT8om72VGy-kJpZjr0JYSWdg9sYwmBrwS2wOnt1JXo7Zqfm3M2vCJ35HzbnQv330-9f4-Ixw", "Rifle", "M4A1-S", "Factory New", "Classified", "d32ce6", 1472.93f));
        weaponRepository.addNewWeapon(new Weapon("Fade", "-9a81dlWLwJ2UUGcVs_nsVtzdOEdtWwKGZZLQHTxDZ7I56KU0Zwwo4NUX4oFJZEHLbXH5ApeO4YmlhxYQknCRvCo04DEVlxkKgpovbSsLQJf2PLacDBA5ciJlYG0kfbwNoTdn2xZ_Pp9i_vG8ML20QXi80M4ZGzwddLGcFBtMl2FrlPrxeu71MC0vZifzyZn63In5S3agVXp1g0meEXB", "Knife", "Karambit", "Factory New", "Covert", "eb4b4b", 1766.45f));
        weaponRepository.addNewWeapon(new Weapon("Asiimov", "-9a81dlWLwJ2UUGcVs_nsVtzdOEdtWwKGZZLQHTxDZ7I56KU0Zwwo4NUX4oFJZEHLbXH5ApeO4YmlhxYQknCRvCo04DEVlxkKgpou-6kejhjxszFJQJD_9W7m5a0mvLwOq7cqWdQ-sJ0xOvEpIj0jAbkqEE_ZD3xctLGJAE_Zw7U-QTowefth8TpvM_InHZh6XQ8pSGKWYJAoJI", "Rifle", "M4A4", "Field-Tested", "Covert", "eb4b4b", 150f));

    }

    /**
     * API-function: Get all existing weapons
     * 
     * @return Collection<Weapon> The list containing all existing weapons
     * 
     */ 
    @GET
    @PermitAll
    @Path("getAll")
    @Operation(summary = "Get all weapons.")
    public Collection<Weapon> getAll() {
        return weaponRepository.getAllWeapons();
    }

    /**
     * API-function: Get the weapon with the designated id
     * 
     * @param id the weapon_id
     * 
     * @return The weapon with the designated id
     */
    @GET
    @PermitAll
    @Path("{id}")
    @Operation(summary = "Get weapon by id.")
    public Weapon weapon(@PathParam("id") Long id) {
        return this.weaponRepository.getWeaponById(id);
    }

    /**
     * API-function: delete weapon
     * 
     * @param id the weapon_id
     */
    @DELETE
    @PermitAll
    @Path("{id}")
    @Transactional
    @Operation(summary = "Delete weapon by id.")
    public Response deleteWeapon(@PathParam("id") Long id) {
        Weapon.deleteById(id);
        return Response.ok().build();
    }

    /**
     * API-function: sell weapon as registered user
     * 
     * @param weapon_name The name of the weapon
     * @param icon_url The icon code of the weapon
     * @param weapon_type The type of the weapon
     * @param gun_type The name of the weapon model
     * @param exterior The status pf the exterior of the weapon
     * @param rarity The rarity of the weapon
     * @param rarity_color The color code for the designated rarity
     * @param price The price of the weapon at which it will be sold
     */
    @POST
    @Path("addToSell")
    // @RolesAllowed("Customer")
    @Transactional
    @Operation(summary = "Sell weapon.")
    public Response addToSell(@Context SecurityContext ctx, @QueryParam("weapon_name") String weapon_name, @QueryParam("icon_url") String icon_url,
            @QueryParam("weapon_type") String weapon_type, @QueryParam("gun_type") String gun_type,
            @QueryParam("exterior") String exterior, @QueryParam("rarity") String rarity,
            @QueryParam("rarity_color") String rarity_color, @QueryParam("price") float price) {
        
        User user = userRepository.getUser(ctx);
        Weapon weapon = new Weapon(weapon_name, icon_url, weapon_type, gun_type, exterior, rarity, rarity_color, price);
        user.kunde.myWeapons.add(weapon);

        return this.weaponRepository.addNewWeapon(weapon);
    }

    /**
     * API-function: add a weapon to customer cart
     * 
     * @param ctx      user SecurityContext
     * @param weapon_id the weapon_id
     */
    @PUT
    @Path("addToCart/{weapon_id}")
    //@RolesAllowed("Customer")
    @Transactional
    @SecurityRequirement(name = "basicAuth", scopes = {})
    @Operation(summary = "Add a weapon to the cart.")
    public int addWeaponToCart(@Context SecurityContext ctx, @PathParam("weapon_id") Long weapon_id) {
        // if the user hase the role as customer
        //String name = ctx.getUserPrincipal() != null ? ctx.getUserPrincipal().getName() : "anonymous";
        //System.out.println(name);

        if(ctx.isUserInRole("Customer")){
            
            Weapon weapon = Weapon.findById(weapon_id);
            if(this.userRepository.addWeaponToCart(this.userRepository.getUser(ctx), weapon)){
                return 0;
            }else{
                return 1;
            }
        }else{
            return 2;
        }
    }

    /**
     * API-function: remove a weapon from customer cart
     * 
     * @param ctx      user SecurityContext
     * @param weapon_id the weapon_id
     */
    @PUT
    @Path("removeFromCart/{weapon_id}")
    @RolesAllowed("Customer")
    @Transactional
    @SecurityRequirement(name = "basicAuth", scopes = {})
    @Operation(summary = "Remove a weapon from the cart.")
    public Response removeWeaponFromCart(@Context SecurityContext ctx, @PathParam("weapon_id") Long weapon_id) {

        Weapon weapon = Weapon.findById(weapon_id);

        return this.userRepository.removeWeaponFromCart(this.userRepository.getUser(ctx), weapon);
    }

    /**
     * API-function: remove a weapon from customer cart
     * 
     * @param ctx      user SecurityContext
     * @param weapon_id the weapon_id
     */
    @PUT
    @Path("clearCart")
    @RolesAllowed("Customer")
    @Transactional
    @SecurityRequirement(name = "basicAuth", scopes = {})
    @Operation(summary = "Delete all weapons from cart.")
    public Response clearCart(@Context SecurityContext ctx) {
        return this.userRepository.clearCart(this.userRepository.getUser(ctx));
    }

    /**
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
     * * * * * * * * * * * * * * * * * * HTML Methods * * * * * * * * * * * * * * *
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
     */

    /**
     * HTML-function: The index page showing all the existing weapons
     * 
     */ 
    @GET
    @PermitAll
    @Produces(MediaType.TEXT_HTML)
    @Operation(summary = "Get all weapons HTML.")
    public @Path("index.html") TemplateInstance getAllHtml() {
        return Templates.getAll(Weapon.listAll());
    }

    /**
     * HTML-function: Webpage showing detailed information of a weapon
     * 
     * @param id The id of the designated weapon to show
     */
    @GET
    @PermitAll
    @Path("{id}.html")
    @Produces(MediaType.TEXT_HTML)
    @Operation(summary = "Get weapon by id HTML.")
    public TemplateInstance getWeaponById(@PathParam("id") Long id) {
        return Templates.weapon(this.weaponRepository.getWeaponById(id));
    }

    /**
     * HTML-function: find weapons by keyword
     * 
     * @param keyWord the search keyword
     */
    @GET
    @PermitAll
    @Produces(MediaType.TEXT_HTML)
    @Path("{keyWord}/search.html")
    @Operation(summary = "find weapons by keyword HTML.")
    public TemplateInstance findWeaponByKeyword(@PathParam("keyWord") String keyWord) {
        return Templates.filter(this.weaponRepository.findWeapons(keyWord), keyWord);
    }

    /**
     * HTML-function: filter weapons by weapon_type
     * 
     * @param weapon_type the search keyword
     */
    @GET
    @PermitAll
    @Produces(MediaType.TEXT_HTML)
    @Path("filter/weapon_type/{weapon_type}.html")
    @Operation(summary = "Filter weapons by weapon_type HTML.")
    public TemplateInstance filterByWeaponType(@PathParam("weapon_type") String weapon_type) {

        return Templates.filter(this.weaponRepository.filterByWeaponType(weapon_type), weapon_type);
    }

    /**
     * HTML-function: filter weapons by exterior
     * 
     * @param exterior the search keyword
     */
    @GET
    @PermitAll
    @Produces(MediaType.TEXT_HTML)
    @Path("filter/exterior/{exterior}.html")
    @Operation(summary = "Filter weapons by exterior HTML.")
    public TemplateInstance filterByExterior(@PathParam("exterior") String exterior) {

        return Templates.filter(this.weaponRepository.filterByExterior(exterior), exterior);
    }

}