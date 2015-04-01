package pothole.management.api.domain;

import org.joda.time.DateTime;
import org.jongo.marshall.jackson.oid.Id;
import org.jongo.marshall.jackson.oid.ObjectId;

import java.util.Date;

public class Pothole {
    @Id @ObjectId
    private String key;

    private String location;
    DateTime dateReported;
    DateTime fixedDate;

    public Pothole(String location){
        this.location = location;
        this.dateReported = DateTime.now();
        this.fixedDate = null;
    }

    public String getKey(){
        return key;
    }

    public Pothole setKey(final String key){
        this.key = key;
        return this;
    }

    public String getLocation(){
        return location;
    }

    public Pothole setLocation(final String location){
        this.location = location;
        return this;
    }

    public String getDateReported(){
        return dateReported.toString();
    }

    public Pothole setDateReported(final DateTime dateReported){
        this.dateReported = dateReported;
        return this;
    }

    public String getFixedDate(){
        return fixedDate.toString();
    }

    public Pothole setFixedDate(final DateTime fixedDate){
        this.fixedDate = fixedDate;
        return this;
    }

    @Override
    public String toString(){
        return "City{" +
                "key='" + key +'\'' +
                ", location='" + location + '\'' +
                ", dateReported='" + dateReported + '\'' +
                '}';
    }
}
