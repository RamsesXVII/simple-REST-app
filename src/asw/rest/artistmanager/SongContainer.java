package asw.rest.artistmanager;

import javax.ws.rs.*;
import javax.ws.rs.core.*;

import javax.persistence.*;

import java.net.URI;

import java.util.*;
import javax.ejb.*;

// import javax.annotation.PostConstruct;

/*
 * L'annotazione @Stateless (e' un EJB) serve soprattutto a consentire
 * l'iniezione della dipendenza dell'entity manager (em).
 * In alternativa si potrebbe fare con Java CDI (@ApplicationScoped o @RequestScoped)
 * ma nessuno degli scopi previsti e' adeguato per l'iniezione dell'em.
 * Pertanto va bene @Stateless.
 */

@Stateless
@Path("/songs")
public class SongContainer {
	@Context
	private UriInfo uriInfo;

    @PersistenceContext(unitName="product-manager-pu")
    private EntityManager em;

    public SongContainer() { }

    /* GET: Restituisce la collezione di tutti le canzoni */
    @GET
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON, MediaType.TEXT_XML})
    public Collection<Song> getSong() {
		try {
			Collection<Song> songs = em.createQuery("SELECT p FROM Song p").getResultList();
			if (songs == null) {
                throw new WebApplicationException(Response.Status.NOT_FOUND);
			} else {
				return songs;
			}
		} catch (Exception e) {
			String errorMessage = "Error while finding all songs: " + e.getMessage();
    		throw new WebApplicationException(
				Response.status(Response.Status.INTERNAL_SERVER_ERROR)
				                        .entity(errorMessage).type("text/plain").build());
		}
    }

    /* POST: Aggiunge una nuova canzone
     * sulla base di un form con campi id, description e price */
    @POST
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response createSong(
			@FormParam("id") int id,
			@FormParam("idArtista") int idArtista,
			@FormParam("name") String name,
			@FormParam("year") String year) {
		/* fa questa ricerca per evitare che venga sollevata un'eccezione al momento del commit */
    	Song p = null;
    	Artist artist = em.find(Artist.class, idArtista);
		if (artist!=null) {
	    	p = new Song(artist,name, year);
			try {
				em.persist(p);
	            return Response.created(URI.create("/" + id)).entity(p).build();
			} catch (Exception e) {
	    		String errorMessage = "Error while creating Song " + p.toString() + ": " + e.getMessage();
	    		throw new WebApplicationException(
					Response.status(Response.Status.INTERNAL_SERVER_ERROR)
				                        .entity(errorMessage).type("text/plain").build());
			}
		} else {
    		String errorMessage = "Error while creating Song with id " + id + ": the artist doesn't exist";
    		throw new WebApplicationException(
			Response.status(Response.Status.INTERNAL_SERVER_ERROR)
			                        .entity(errorMessage).type("text/plain").build());
		}
    }
}