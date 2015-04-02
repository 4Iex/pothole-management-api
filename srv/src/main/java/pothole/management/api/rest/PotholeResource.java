package pothole.management.api.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Optional;
import com.mongodb.util.JSON;
import com.sun.tools.doclets.formats.html.SourceToHTMLConverter;
import org.bson.types.ObjectId;
import org.json.JSONArray;
import org.json.JSONObject;
import pothole.management.api.domain.Pothole;
import restx.Status;
import restx.annotations.*;
import restx.factory.Component;
import restx.jongo.JongoCollection;
import restx.security.PermitAll;

import javax.inject.Named;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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
        pothole.setLocation(parseGeoCode(pothole.getLocation()));
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

    public String parseGeoCode(String location){
        String locationClean = location.replaceAll(" ","+");
        try {
            URL url = new URL("http://maps.googleapis.com/maps/api/geocode/json?address="+locationClean);
            ObjectMapper mapper = new ObjectMapper();
            JSONObject parsedJson = new JSONObject(mapper.readValue(url, Map.class));
            JSONObject results = parsedJson.getJSONArray("results").getJSONObject(0);
            JSONObject geometry = results.getJSONObject("geometry");
            JSONObject loc = geometry.getJSONObject("location");
            String lng = loc.get("lng").toString();
            String lat = loc.get("lat").toString();
            System.out.println("The geo location of " + location + " is: " +lat+ "," + lng);

            return lat + "," + lng;
        }
        catch (Exception e){
            System.out.println(e);
            return "LOCATION NOT SET";
        }
    }
}
