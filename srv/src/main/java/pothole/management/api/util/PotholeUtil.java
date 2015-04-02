package pothole.management.api.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;

import java.net.URL;
import java.util.Map;

public class PotholeUtil {
    public static String parseGeoCode(String location){
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
