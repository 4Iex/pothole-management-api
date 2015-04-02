package pothole.management.api.rest;

import com.google.common.base.Optional;
import org.bson.types.ObjectId;
import pothole.management.api.domain.Pothole;
import pothole.management.api.util.PotholeUtil;
import restx.Status;
import restx.annotations.*;
import restx.factory.Component;
import restx.jongo.JongoCollection;
import restx.security.PermitAll;

import javax.inject.Named;

import static restx.common.MorePreconditions.checkEquals;

@Component @RestxResource
@PermitAll
public class PotholeResource {
    private final JongoCollection potholes;

    public PotholeResource(@Named("potholes") JongoCollection potholes){
        this.potholes = potholes;
    }

    @GET("/potholes")
    public Iterable<Pothole> findPotholes(Optional<String> location){
        if(location.isPresent()){
            return potholes.get().find("{location: #}", location.get()).as(Pothole.class);
        } else {
            Iterable<Pothole> results = potholes.get().find().as(Pothole.class);
            return results;
        }
    }

    @PermitAll
    @POST("/potholes")
    public Pothole createPothole(Pothole pothole){
        pothole.setLocation(PotholeUtil.parseGeoCode(pothole.getLocation()));
        potholes.get().save(pothole);
        return pothole;
    }

    @GET("/potholes/{oid}")
    public Optional<Pothole> findPotholeById(String oid) {
        return Optional.fromNullable(potholes.get().findOne(new ObjectId(oid)).as(Pothole.class));
    }

    @PUT("/potholes/{oid}")
    public Pothole updatePothole(String oid, Pothole pothole){
        checkEquals("oid", oid, "pothole.key", pothole.getKey());
        potholes.get().save(pothole);
        return pothole;
    }

    @DELETE("/potholes/{oid}")
    public Status deletePothole(String oid){
        potholes.get().remove(new ObjectId(oid));
        return Status.of("deleted");
    }
}
