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

    public Signature(@JsonProperty("id") final String id,
                     @JsonProperty("date") final String date) {
        this.id = id;
        this.date = date;
    }
}
