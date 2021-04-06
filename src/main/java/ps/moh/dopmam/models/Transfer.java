package ps.moh.dopmam.models;

import com.owlike.genson.annotation.JsonProperty;
import org.hyperledger.fabric.contract.annotation.DataType;
import org.hyperledger.fabric.contract.annotation.Property;
import ps.moh.dopmam.utils.Utils;

import java.util.Date;

@DataType()
public class Transfer {
    @Property()
    private String transferId;

    @Property()
    private String country;

    @Property()
    private String city;

    @Property()
    private String hospital;

    @Property()
    private String department;

    @Property()
    private String doctor;

    @Property()
    private Date transferDate;

    @Property()
    private int coverage;

    public Transfer(@JsonProperty("id") final String transferId,
                    @JsonProperty("country") final String country,
                    @JsonProperty("city") final String city,
                    @JsonProperty("hospital") final String hospital,
                    @JsonProperty("department") final String department,
                    @JsonProperty("doctor") final String doctor,
                    @JsonProperty("date") final Date transferDate,
                    @JsonProperty("coverage") final int coverage) {
        this.transferId = transferId;
        this.country = country;
        this.city = city;
        this.hospital = hospital;
        this.department = department;
        this.doctor = doctor;
        this.transferDate = transferDate;
        this.coverage = coverage;
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

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        if (!Utils.IsNotNullOrEmpty(country)) {
            throw new IllegalArgumentException();
        }
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        if (!Utils.IsNotNullOrEmpty(city)) {
            throw new IllegalArgumentException();
        }
        this.city = city;
    }

    public String getHospital() {
        return hospital;
    }

    public void setHospital(String hospital) {
        if (!Utils.IsNotNullOrEmpty(hospital)) {
            throw new IllegalArgumentException();
        }
        this.hospital = hospital;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        if (!Utils.IsNotNullOrEmpty(department)) {
            throw new IllegalArgumentException();
        }
        this.department = department;
    }

    public String getDoctor() {
        return doctor;
    }

    public void setDoctor(String doctor) {
        if (!Utils.IsNotNullOrEmpty(doctor)) {
            throw new IllegalArgumentException();
        }
        this.doctor = doctor;
    }

    public Date getTransferDate() {
        return transferDate;
    }

    public void setTransferDate(Date transferDate) {
        if (transferDate == null) {
            throw new IllegalArgumentException();
        }
        this.transferDate = transferDate;
    }

    public int getCoverage() {
        return coverage;
    }

    public void setCoverage(int coverage) {
        if (coverage < 0) {
            throw new IllegalArgumentException();
        }
        this.coverage = coverage;
    }
}
