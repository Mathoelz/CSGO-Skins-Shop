package de.hsos.swa.gateway;

import java.util.Collection;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.core.Response;

import de.hsos.swa.entity.Discount;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

/**
 * This class supports the api by allowing us to manage
 * all existing discount objects in our database
 * @author Mathias Hölz
 */
@ApplicationScoped
public class DiscountRepository implements PanacheRepository<Discount> {
    
    /**
     * Create a new discount which will be added to the database
     * 
     * @param discount The discount object to add 
     * 
     * @return Response if the operation failed or succeded
     */
    public Response createDiscount(final Discount discount) {
        if(discount != null) {
            discount.persist();
            return Response.ok().build();
        }

        return Response.noContent().build();
    }

    /**
     * Get all existing discounts
     * 
     * @return Collection of all existing discounts
     */
    public Collection<Discount> getAllDiscounts() {
        return Discount.listAll();
    }

    /**
     * Get a discount object by id
     * 
     * @return Discount with the designated id
     */
    public Discount getById(Long id) {
        return Discount.findById(id);
    }

}
