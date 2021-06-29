package ps.moh.dopmam.models;

import com.owlike.genson.annotation.JsonProperty;
import org.hyperledger.fabric.contract.annotation.DataType;
import org.hyperledger.fabric.contract.annotation.Property;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@DataType()
public class Report {
    @Property()
    private final long reportId;

    @Property()
    private final long patientNationalId;

    @Property()
    private final Date reportDate;

    @Property()
    private final String medicalHistoryAndClinicalFindings;

    @Property()
    private final String diagnosis;

    @Property()
    private final String recommendation;

    @Property()
    private String transferToCountry;

    @Property()
    private String transferToCity;

    @Property()
    private String transferToHospital;

    @Property()
    private String transferToDepartment;

    @Property()
    private Date transferDueDate;

    @Property()
    private double coverage;

    @Property()
    private String doctorSignature;

    @Property()
    private String doctorDepartment;

    @Property()
    private String headOfDepartmentSignature;

    @Property()
    private String hospitalManagerSignature;

    @Property()
    private List<String> medicalCommitteeSignatures;

    @Property()
    private List<String> financialCommitteeSignatures;

    @Property()
    private boolean rejected;

    public Report(@JsonProperty("reportId") final long reportId,
                  @JsonProperty("patientNationalId") final long patientNationalId,
                  @JsonProperty("reportDate") final Date reportDate,
                  @JsonProperty("medicalHistoryAndClinicalFindings") final String medicalHistoryAndClinicalFindings,
                  @JsonProperty("diagnosis") final String diagnosis,
                  @JsonProperty("recommendation") final String recommendation) {
        this.reportId = reportId;
        this.patientNationalId = patientNationalId;
        this.reportDate = reportDate;
        this.medicalHistoryAndClinicalFindings = medicalHistoryAndClinicalFindings;
        this.diagnosis = diagnosis;
        this.recommendation = recommendation;

        this.medicalCommitteeSignatures = new ArrayList<>();
        this.financialCommitteeSignatures = new ArrayList<>();
        this.rejected = false;
    }

    public long getReportId() {
        return reportId;
    }

    public long getPatientNationalId() {
        return patientNationalId;
    }

    public Date getReportDate() {
        return reportDate;
    }

    public String getMedicalHistoryAndClinicalFindings() {
        return medicalHistoryAndClinicalFindings;
    }

    public String getDiagnosis() {
        return diagnosis;
    }

    public String getRecommendation() {
        return recommendation;
    }

    public String getTransferToCountry() {
        return transferToCountry;
    }

    public String getTransferToCity() {
        return transferToCity;
    }

    public String getTransferToHospital() {
        return transferToHospital;
    }

    public String getTransferToDepartment() {
        return transferToDepartment;
    }

    public Date getTransferDueDate() {
        return transferDueDate;
    }

    public String getDoctorSignature() {
        return doctorSignature;
    }

    public String getDoctorDepartment() {
        return doctorDepartment;
    }

    public String getHeadOfDepartmentSignature() {
        return headOfDepartmentSignature;
    }

    public String getHospitalManagerSignature() {
        return hospitalManagerSignature;
    }

    public List<String> getMedicalCommitteeSignatures() {

        return this.medicalCommitteeSignatures;
    }

    public List<String> getFinancialCommitteeSignatures() {
        return this.financialCommitteeSignatures;
    }

    public void setDoctorSignature(String doctorSignature) {
        if (this.doctorSignature == null) {
            this.doctorSignature = doctorSignature;
            return;
        }

        throw new IllegalStateException("Doctor had already signed!");
    }

    public void setDoctorDepartment(String doctorDepartment) {
        this.doctorDepartment = doctorDepartment;
    }

    public void setHeadOfDepartmentSignature(final String headOfDepartmentSignature) {
        if (this.headOfDepartmentSignature == null) {
            this.headOfDepartmentSignature = headOfDepartmentSignature;
            return;
        }

        throw new IllegalStateException("Head of Department had already signed!");
    }

    public void setHospitalManagerSignature(String hospitalManagerSignature) {
        if (this.hospitalManagerSignature == null) {
            this.hospitalManagerSignature = hospitalManagerSignature;
            return;
        }

        throw new IllegalStateException("Hospital Manager had already signed!");
    }

    public void setMedicalCommitteeSignatures(List<String> medicalCommitteeSignatures) {
        this.medicalCommitteeSignatures = medicalCommitteeSignatures;
    }

    public void setFinancialCommitteeSignatures(List<String> financialCommitteeSignatures) {
        this.financialCommitteeSignatures = financialCommitteeSignatures;
    }

    public void setTransferToCountry(String transferToCountry) {
        this.transferToCountry = transferToCountry;
    }

    public void setTransferToCity(String transferToCity) {
        this.transferToCity = transferToCity;
    }

    public void setTransferToHospital(String transferToHospital) {
        this.transferToHospital = transferToHospital;
    }

    public void setTransferToDepartment(String transferToDepartment) {
        this.transferToDepartment = transferToDepartment;
    }

    public void setTransferDueDate(Date transferDueDate) {
        this.transferDueDate = transferDueDate;
    }

    public boolean isRejected() {
        return rejected;
    }

    public void setRejected(boolean rejected) {
        this.rejected = rejected;
    }

    public double getCoverage() {
        return coverage;
    }

    public void setCoverage(double coverage) {
        this.coverage = coverage;
    }
}