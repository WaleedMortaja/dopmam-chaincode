package ps.moh.dopmam.models;

import com.owlike.genson.annotation.JsonProperty;
import org.hyperledger.fabric.contract.annotation.DataType;
import org.hyperledger.fabric.contract.annotation.Property;
import ps.moh.dopmam.utils.Utils;

import java.util.Date;

@DataType()
public class Patient {
    @Property()
    private String nationalId;

    @Property()
    private String firstName;

    @Property()
    private String lastName;

    @Property()
    private Gender gender;

    @Property()
    private Date dateOfBirth;

    @Property()
    private String insuranceNumber;

    @Property()
    private Date insuranceDueDate;

    public Patient(@JsonProperty("nationalId") final String nationalId,
                   @JsonProperty("firstName") final String firstName,
                   @JsonProperty("lastName") final String lastName,
                   @JsonProperty("gender") final Gender gender,
                   @JsonProperty("dateOfBirth") final Date dateOfBirth,
                   @JsonProperty("insuranceNumber") final String insuranceNumber,
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
    public String getNationalId() {
        return nationalId;
    }

    /**
     * Set the nationalId for the patient.
     *
     * @param nationalId the nationalId of the patient
     */
    public void setNationalId(final String nationalId) {
        if (!Utils.isNotNullOrEmpty(nationalId)) {
            throw new IllegalArgumentException();
        }
        this.nationalId = nationalId;
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
     * Sets the firstName of the patient.
     *
     * @param firstName the firstName of the patient
     */
    public void setFirstName(final String firstName) {
        if (!Utils.isNotNullOrEmpty(firstName)) {
            throw new IllegalArgumentException();
        }
        this.firstName = firstName;
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
     * Sets the lastName of the patient.
     *
     * @param lastName the lastName of the patient
     */
    public void setLastName(final String lastName) {
        if (!Utils.isNotNullOrEmpty(lastName)) {
            throw new IllegalArgumentException();
        }
        this.lastName = lastName;
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
     * Sets the gender of the patient.
     *
     * @param gender the gender of the patient
     */
    public void setGender(final Gender gender) {
        if (gender == null) {
            throw new IllegalArgumentException();
        }
        this.gender = gender;
    }

    /**
     * Retrieves the insuranceNumber of the patient.
     *
     * @return the insuranceNumber of the patient
     */
    public String getInsuranceNumber() {
        return insuranceNumber;
    }

    /**
     * Sets the insuranceNumber of the patient.
     *
     * @param insuranceNumber the insuranceNumber of the patient
     */
    public void setInsuranceNumber(final String insuranceNumber) {
        if (!Utils.isNotNullOrEmpty(insuranceNumber)) {
            throw new IllegalArgumentException();
        }
        this.insuranceNumber = insuranceNumber;
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
     * Sets the insuranceDueDate of the patient.
     *
     * @param insuranceDueDate the insuranceDueDate of the patient
     */
    public void setInsuranceDueDate(final Date insuranceDueDate) {
        if (insuranceDueDate == null) {
            throw new IllegalArgumentException();
        }
        this.insuranceDueDate = insuranceDueDate;
    }

    /**
     * Retrieves the dateOfBirth of the patient.
     *
     * @return the dateOfBirth of the patient
     */
    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    /**
     * Sets the dateOfBirth of the patient.
     *
     * @param dateOfBirth the dateOfBirth of the patient
     */
    public void setDateOfBirth(final Date dateOfBirth) {
        if (dateOfBirth == null) {
            throw new IllegalArgumentException();
        }
        this.dateOfBirth = dateOfBirth;
    }
}
