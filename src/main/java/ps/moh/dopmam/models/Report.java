package ps.moh.dopmam.models;

import com.owlike.genson.annotation.JsonProperty;
import org.hyperledger.fabric.contract.annotation.DataType;
import org.hyperledger.fabric.contract.annotation.Property;
import ps.moh.dopmam.utils.Result;
import ps.moh.dopmam.utils.Utils;

import java.util.ArrayList;
import java.util.Date;

@DataType()
public class Report {
    @Property()
    private String id;

    @Property()
    private String patientNationalId;

    @Property()
    private String date;

    @Property()
    private String summary;

    @Property()
    private String diagnosis;

    @Property()
    private String procedure;

    @Property()
    private String transferId;

    @Property()
    private Signature doctorSignature;

    @Property()
    private Signature departmentSignature;

    @Property()
    private Signature hospitalSignature;

    @Property()
    private ArrayList<Signature> medicalCommitteeSignatures;

    @Property()
    private ArrayList<Signature> financialCommitteeSignatures;

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

    public Signature getDoctorSignature() {
        return doctorSignature;
    }

    public void setDoctorSignature(Signature doctorSignature) {
        if (doctorSignature == null) {
            throw new IllegalArgumentException();
        }
        this.doctorSignature = doctorSignature;
    }

    public Signature getDepartmentSignature() {
        return departmentSignature;
    }

    public void setDepartmentSignature(Signature departmentSignature) {
        if (departmentSignature == null) {
            throw new IllegalArgumentException();
        }
        this.departmentSignature = departmentSignature;
    }

    public Signature getHospitalSignature() {
        return hospitalSignature;
    }

    public void setHospitalSignature(Signature hospitalSignature) {
        if (hospitalSignature == null) {
            throw new IllegalArgumentException();
        }
        this.hospitalSignature = hospitalSignature;
    }

    public ArrayList<Signature> getMedicalCommitteeSignatures() {
        return medicalCommitteeSignatures;
    }

    public void setMedicalCommitteeSignatures(ArrayList<Signature> medicalCommitteeSignatures) {
        if (medicalCommitteeSignatures == null) {
            throw new IllegalArgumentException();
        }
        this.medicalCommitteeSignatures.addAll(medicalCommitteeSignatures);
    }

    public ArrayList<Signature> getFinancialCommitteeSignatures() {
        return financialCommitteeSignatures;
    }

    public void setFinancialCommitteeSignatures(ArrayList<Signature> financialCommitteeSignatures) {
        if (financialCommitteeSignatures == null) {
            throw new IllegalArgumentException();
        }
        this.financialCommitteeSignatures.addAll(financialCommitteeSignatures);
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
        medicalCommitteeSignatures = new ArrayList<>();
        financialCommitteeSignatures = new ArrayList<>();
    }
}
