package ps.moh.dopmam.models;

import com.owlike.genson.annotation.JsonProperty;
import org.hyperledger.fabric.contract.annotation.DataType;
import org.hyperledger.fabric.contract.annotation.Property;
import ps.moh.dopmam.utils.Utils;

import java.util.Date;

@DataType()
public class Patient {
    @Property()
    private long nationalId;

    @Property()
    private String firstName;

    @Property()
    private String lastName;

    @Property()
    private Gender gender;

    @Property()
    private Date dateOfBirth;

    @Property()
    private long insuranceNumber;

    @Property()
    private Date insuranceDueDate;

    public Patient(@JsonProperty("nationalId") final long nationalId,
                   @JsonProperty("firstName") final String firstName,
                   @JsonProperty("lastName") final String lastName,
                   @JsonProperty("gender") final Gender gender,
                   @JsonProperty("dateOfBirth") final Date dateOfBirth,
                   @JsonProperty("insuranceNumber") final long insuranceNumber,
                   @JsonProperty("insuranceDueDate") final Date insuranceDueDate) {
        this.nationalId = nationalId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.dateOfBirth = dateOfBirth;
        this.insuranceNumber = insuranceNumber;
        this.insuranceDueDate = insuranceDueDate;
    }

    /**
     * Retrieves the nationalId of the patient.
     *
     * @return the nationalId of the patietnt
     */
    public long getNationalId() {
        return nationalId;
    }

    /**
     * Retrieves the firstName of the patient.
     *
     * @return the firstName of the patient
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Retrieves the lastName of the patient.
     *
     * @return the lastName of the patient
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Retrieves the gender of the patient.
     *
     * @return the gender of the patient
     */
    public Gender getGender() {
        return gender;
    }

    /**
     * Retrieves the insuranceNumber of the patient.
     *
     * @return the insuranceNumber of the patient
     */
    public long getInsuranceNumber() {
        return insuranceNumber;
    }

    /**
     * Retrieves the insuranceDueDate of the patient.
     *
     * @return the insuranceDueDate of the patient
     */
    public Date getInsuranceDueDate() {
        return insuranceDueDate;
    }

    /**
     * Retrieves the dateOfBirth of the patient.
     *
     * @return the dateOfBirth of the patient
     */
    public Date getDateOfBirth() {
        return dateOfBirth;
    }
}
