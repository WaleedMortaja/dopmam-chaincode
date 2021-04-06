package ps.moh.dopmam.models;

import com.owlike.genson.annotation.JsonProperty;
import org.hyperledger.fabric.contract.annotation.DataType;
import org.hyperledger.fabric.contract.annotation.Property;
import ps.moh.dopmam.utils.Utils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@DataType()
public class Report {
    @Property()
    private String reportId;

    @Property()
    private String patientNationalId;

    @Property()
    private Date reportDate;

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
    private List<Signature> medicalCommitteeSignatures;

    @Property()
    private List<Signature> financialCommitteeSignatures;

    public Report(@JsonProperty("id") final String reportId,
                  @JsonProperty("patientNationalId") final String patientNationalId,
                  @JsonProperty("date") final Date reportDate,
                  @JsonProperty("summary") final String summary,
                  @JsonProperty("diagnosis") final String diagnosis,
                  @JsonProperty("procedure") final String procedure,
                  @JsonProperty("transferId") final String transferId) {
        this.reportId = reportId;
        this.patientNationalId = patientNationalId;
        this.reportDate = reportDate;
        this.summary = summary;
        this.diagnosis = diagnosis;
        this.procedure = procedure;
        this.transferId = transferId;
        medicalCommitteeSignatures = new ArrayList<>();
        financialCommitteeSignatures = new ArrayList<>();
    }

    public String getReportId() {
        return reportId;
    }

    public void setReportId(String reportId) {
        if (!Utils.IsNotNullOrEmpty(reportId)) {
            throw new IllegalArgumentException();
        }
        this.reportId = reportId;
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

    public Date getReportDate() {
        return reportDate;
    }

    public void setReportDate(Date reportDate) {
        if (reportDate == null) {
            throw new IllegalArgumentException();
        }
        this.reportDate = reportDate;
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

    public List<Signature> getMedicalCommitteeSignatures() {
        return medicalCommitteeSignatures;
    }

    public void setMedicalCommitteeSignatures(List<Signature> medicalCommitteeSignatures) {
        if (medicalCommitteeSignatures == null) {
            throw new IllegalArgumentException();
        }
        this.medicalCommitteeSignatures = medicalCommitteeSignatures;
    }

    public List<Signature> getFinancialCommitteeSignatures() {
        return financialCommitteeSignatures;
    }

    public void setFinancialCommitteeSignatures(List<Signature> financialCommitteeSignatures) {
        if (financialCommitteeSignatures == null) {
            throw new IllegalArgumentException();
        }
        this.financialCommitteeSignatures = financialCommitteeSignatures;
    }
}
