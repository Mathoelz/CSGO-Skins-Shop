package de.hsos.swa.gateway;

import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.core.Response;

import de.hsos.swa.entity.Weapon;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

/**
 * This class supports the api by allowing us to manage
 * all existing weapons in our database
 * @author Maxim Zitnikowski
 */
@ApplicationScoped
public class WeaponRepository implements PanacheRepository<Weapon> {

	/**
     * Adds a new weapon to our database
     * 
     * @param weapon The weapon which will be added 
     * 
     * @return Response wether the operation failed or succeded
     */
	public Response addNewWeapon(final Weapon weapon) {
		if (weapon != null) {
			weapon.persist();
			return Response.ok().build();
		}
		return Response.noContent().build();
	}

	/**
     * Return a collection of all existing weapons in our database
     * 
     * @return Collection of all weapon objects in the database
     */
	public Collection<Weapon> getAllWeapons() {
		return Weapon.listAll();
	}

	/**
     * Get the weapon with the designated id
     * 
     * @param id Id of the weapon you're looking for 
     * 
     * @return Weapon with the given id
     */
	public Weapon getWeaponById(Long id) {
		return Weapon.findById(id);
	}

	/**
     * Find weapons matching the given keyword
     * 
     * @param keyWord Keyword to look for in weapons
     * 
     * @return Colllection of the weapons matching the keyword
     */
	public Collection<Weapon> findWeapons(String keyWord) {

		Collection<Weapon> weapons = Weapon.listAll();

		Collection<Weapon> w1 = weapons.stream()
				.filter(weapon -> weapon.weapon_name.toLowerCase().contains(keyWord.toLowerCase()))
				.collect(Collectors.toList());

		Collection<Weapon> w2 = weapons.stream()
				.filter(weapon -> weapon.gun_type.toLowerCase().contains(keyWord.toLowerCase()))
				.collect(Collectors.toList());

		Collection<Weapon> newList = Stream.concat(w1.stream(), w2.stream()).collect(Collectors.toList());

		return newList;
	}

	/**
     * Filters the weapons by the given weapon type
     * 
     * @param weapon_type The weapon type by which to filter 
     * 
     * @return Collection of all weapons matching the given weapon type
     */
	public Collection<Weapon> filterByWeaponType(final String weapon_type) {

		Collection<Weapon> weapons = Weapon.listAll();
		Collection<Weapon> filteredWeapons = weapons.stream()
				.filter(w -> w.weapon_type.toLowerCase().equals(weapon_type.toLowerCase()))
				.collect(Collectors.toList());

		return filteredWeapons;
	}

	/**
     * Filter the weapons by the given exterior type
     * 
     * @param exterior The exterior by which to filter 
     * 
     * @return Collection of all weapons matching the given exterior
     */
	public Collection<Weapon> filterByExterior(final String exterior) {
		Collection<Weapon> weapons = Weapon.listAll();
		Collection<Weapon> filteredWeapons = weapons.stream()
				.filter(w -> w.exterior.toLowerCase().equals(exterior.toLowerCase())).collect(Collectors.toList());

		return filteredWeapons;
	}

}