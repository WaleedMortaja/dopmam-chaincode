package ps.moh.dopmam.models;

import com.owlike.genson.annotation.JsonProperty;
import org.hyperledger.fabric.contract.annotation.DataType;
import org.hyperledger.fabric.contract.annotation.Property;
import ps.moh.dopmam.utils.Result;
import ps.moh.dopmam.utils.Utils;

import java.util.Date;

@DataType()
public class Signature {
    @Property()
    private String id;

    @Property()
    private String date;

    @Property()
    private String name;

    @Property()
    private String role;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        if (!Utils.IsNotNullOrEmpty(id)) {
            throw new IllegalArgumentException();
        }
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        Result<Date> result = Utils.StringToDate(date);
        if (!result.success) {
            throw new IllegalArgumentException();
        }
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (!Utils.IsNotNullOrEmpty(name)) {
            throw new IllegalArgumentException();
        }
        this.name = name;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        if (!Utils.IsNotNullOrEmpty(role)) {
            throw new IllegalArgumentException();
        }
        this.role = role;
    }

    public Signature(@JsonProperty("id") final String id,
                     @JsonProperty("date") final String date,
                     @JsonProperty("name") final String name,
                     @JsonProperty("role") final String role) {
        this.id = id;
        this.date = date;
        this.name = name;
        this.role = role;
    }
}
