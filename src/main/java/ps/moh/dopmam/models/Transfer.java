package ps.moh.dopmam.models;

import com.owlike.genson.annotation.JsonProperty;
import org.hyperledger.fabric.contract.annotation.DataType;
import org.hyperledger.fabric.contract.annotation.Property;
import ps.moh.dopmam.utils.Result;
import ps.moh.dopmam.utils.Utils;

import java.util.Date;

@DataType()
public class Transfer {
    @Property()
    private String id;

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
    private String date;

    @Property()
    private String coverage;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        if (!Utils.IsNotNullOrEmpty(id)) {
            throw new IllegalArgumentException();
        }
        this.id = id;
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

    public String getCoverage() {
        return coverage;
    }

    public void setCoverage(String coverage) {
        Result<Integer> result = Utils.StringToInt(coverage);
        if (!result.success) {
            throw new IllegalArgumentException();
        }
        this.coverage = coverage;
    }

    public Transfer(@JsonProperty("id") final String id,
                    @JsonProperty("country") final String country,
                    @JsonProperty("city") final String city,
                    @JsonProperty("hospital") final String hospital,
                    @JsonProperty("department") final String department,
                    @JsonProperty("doctor") final String doctor,
                    @JsonProperty("date") final String date,
                    @JsonProperty("coverage") final String coverage) {
        this.id = id;
        this.country = country;
        this.city = city;
        this.hospital = hospital;
        this.department = department;
        this.doctor = doctor;
        this.date = date;
        this.coverage = coverage;
    }
}
