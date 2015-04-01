package pothole.management.api.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.jongo.marshall.jackson.oid.Id;
import org.jongo.marshall.jackson.oid.ObjectId;
import java.util.Date;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Pothole {
    @Id @ObjectId
    private String key;

    private String location;
    Date dateReported;

    //Todo: Figure out how to get this to work if fixedDate is null
    @JsonIgnore
    Date fixedDate;

    public Pothole(String location){
        this.location = location;
        this.dateReported = new Date();
        this.fixedDate = new Date();
    }

    //Introducing the dummy constructor
    public Pothole() {
        this.dateReported = new Date();
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

    public Pothole setDateReported(final Date dateReported){
        this.dateReported = dateReported;
        return this;
    }

    public String getFixedDate(){
        return fixedDate.toString();
    }

    public Pothole setFixedDate(final Date fixedDate){
        this.fixedDate = fixedDate;
        return this;
    }

    @Override
    public String toString(){
        return "City{" +
                "key='" + key +'\'' +
                ", location='" + location + '\'' +
                ", dateReported='" + dateReported.toString() + '\'' +
                '}';
    }
}
