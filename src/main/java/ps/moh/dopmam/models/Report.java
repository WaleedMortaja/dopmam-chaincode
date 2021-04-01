package ps.moh.dopmam.models;

import com.owlike.genson.annotation.JsonProperty;
import org.hyperledger.fabric.contract.annotation.DataType;
import org.hyperledger.fabric.contract.annotation.Property;
import ps.moh.dopmam.utils.Result;
import ps.moh.dopmam.utils.Utils;

import java.util.Date;

@DataType()
public class Report {
    @Property()
    public String id;

    @Property()
    public String patientNationalId;

    @Property()
    public String date;

    @Property()
    public String summary;

    @Property()
    public String diagnosis;

    @Property()
    public String procedure;

    @Property()
    public String transferId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        if (!Utils.IsNotNullOrEmpty(id)) {
            throw new IllegalArgumentException();
        }
        this.id = id;
    }

    public String getPatientNationalId() {
        return patientNationalId;
    }

    public void setPatientNationalId(String patientNationalId) {
        if (!Utils.IsNotNullOrEmpty(patientNationalId)) {
            throw new IllegalArgumentException();
        }
        this.patientNationalId = patientNationalId;
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

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        if (!Utils.IsNotNullOrEmpty(summary)) {
            throw new IllegalArgumentException();
        }
        this.summary = summary;
    }

    public String getDiagnosis() {
        return diagnosis;
    }

    public void setDiagnosis(String diagnosis) {
        if (!Utils.IsNotNullOrEmpty(diagnosis)) {
            throw new IllegalArgumentException();
        }
        this.diagnosis = diagnosis;
    }

    public String getProcedure() {
        return procedure;
    }

    public void setProcedure(String procedure) {
        if (!Utils.IsNotNullOrEmpty(procedure)) {
            throw new IllegalArgumentException();
        }
        this.procedure = procedure;
    }

    public String getTransferId() {
        return transferId;
    }

    public void setTransferId(String transferId) {
        if (!Utils.IsNotNullOrEmpty(transferId)) {
            throw new IllegalArgumentException();
        }
        this.transferId = transferId;
    }

    public Report(@JsonProperty("id") final String id,
                  @JsonProperty("patientNationalId") final String patientNationalId,
                  @JsonProperty("date") final String date,
                  @JsonProperty("summary") final String summary,
                  @JsonProperty("diagnosis") final String diagnosis,
                  @JsonProperty("procedure") final String procedure,
                  @JsonProperty("transferId") final String transferId) {
        this.id = id;
        this.patientNationalId = patientNationalId;
        this.date = date;
        this.summary = summary;
        this.diagnosis = diagnosis;
        this.procedure = procedure;
        this.transferId = transferId;
    }
}
