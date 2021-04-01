package ps.moh.dopmam.models;

import com.owlike.genson.annotation.JsonProperty;
import org.hyperledger.fabric.contract.annotation.DataType;
import org.hyperledger.fabric.contract.annotation.Property;
import ps.moh.dopmam.utils.Result;
import ps.moh.dopmam.utils.Utils;

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
    private String dateOfBirth;

    @Property()
    private String insuranceNumber;

    @Property()
    private String insuranceDueDate;

    public Patient(@JsonProperty("nationalId") final String nationalId,
                   @JsonProperty("firstName") final String firstName,
                   @JsonProperty("lastName") final String lastName,
                   @JsonProperty("gender") final Gender gender,
                   @JsonProperty("dateOfBirth") final String dateOfBirth,
                   @JsonProperty("insuranceNumber") final String insuranceNumber,
                   @JsonProperty("insuranceDueDate") final String insuranceDueDate) {
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
        Result<Integer> result = Utils.StringToInt(nationalId);
        if (result.success && result.payload > 0) {
            this.nationalId = nationalId;
        } else {
            throw new IllegalArgumentException();
        }
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
        if (!firstName.isEmpty()) {
            this.firstName = firstName;
        } else {
            throw new IllegalArgumentException();
        }
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
        if (lastName == null || lastName.isEmpty()) {
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
        this.insuranceNumber = insuranceNumber;
    }

    /**
     * Retrieves the insuranceDueDate of the patient.
     *
     * @return the insuranceDueDate of the patient
     */
    public String getInsuranceDueDate() {
        return insuranceDueDate;
    }

    /**
     * Sets the insuranceDueDate of the patient.
     *
     * @param insuranceDueDate the insuranceDueDate of the patient
     */
    public void setInsuranceDueDate(final String insuranceDueDate) {
        this.insuranceDueDate = insuranceDueDate;
    }

    /**
     * Retrieves the dateOfBirth of the patient.
     *
     * @return the dateOfBirth of the patient
     */
    public String getDateOfBirth() {
        return dateOfBirth;
    }

    /**
     * Sets the dateOfBirth of the patient.
     *
     * @param dateOfBirth the dateOfBirth of the patient
     */
    public void setDateOfBirth(final String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }
}
