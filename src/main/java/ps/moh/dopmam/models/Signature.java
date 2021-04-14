package ps.moh.dopmam.models;

import com.owlike.genson.annotation.JsonProperty;
import org.hyperledger.fabric.contract.annotation.DataType;
import org.hyperledger.fabric.contract.annotation.Property;
import ps.moh.dopmam.utils.Utils;

import java.util.Date;

@DataType()
public class Signature {
    @Property()
    private String signerId;

    @Property()
    private Date signDate;

    @Property()
    private String signerName;

    @Property()
    private String signerRole;

    public Signature(@JsonProperty("id") final String signerId,
                     @JsonProperty("date") final Date signDate,
                     @JsonProperty("name") final String signerName,
                     @JsonProperty("role") final String signerRole) {
        this.signerId = signerId;
        this.signDate = signDate;
        this.signerName = signerName;
        this.signerRole = signerRole;
    }

    public String getSignerId() {
        return signerId;
    }

    public void setSignerId(String signerId) {
        if (!Utils.isNotNullOrEmpty(signerId)) {
            throw new IllegalArgumentException();
        }
        this.signerId = signerId;
    }

    public Date getSignDate() {
        return signDate;
    }

    public void setSignDate(Date signDate) {
        if (signDate == null) {
            throw new IllegalArgumentException();
        }
        this.signDate = signDate;
    }

    public String getSignerName() {
        return signerName;
    }

    public void setSignerName(String signerName) {
        if (!Utils.isNotNullOrEmpty(signerName)) {
            throw new IllegalArgumentException();
        }
        this.signerName = signerName;
    }

    public String getSignerRole() {
        return signerRole;
    }

    public void setSignerRole(String signerRole) {
        if (!Utils.isNotNullOrEmpty(signerRole)) {
            throw new IllegalArgumentException();
        }
        this.signerRole = signerRole;
    }
}
