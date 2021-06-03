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
    private final String summary;

    @Property()
    private final String diagnosis;

    @Property()
    private final String procedure;

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

    public Report(@JsonProperty("reportId") final long reportId,
                  @JsonProperty("patientNationalId") final long patientNationalId,
                  @JsonProperty("reportDate") final Date reportDate,
                  @JsonProperty("summary") final String summary,
                  @JsonProperty("diagnosis") final String diagnosis,
                  @JsonProperty("procedure") final String procedure) {
        this.reportId = reportId;
        this.patientNationalId = patientNationalId;
        this.reportDate = reportDate;
        this.summary = summary;
        this.diagnosis = diagnosis;
        this.procedure = procedure;

        this.medicalCommitteeSignatures = new ArrayList<>();
        this.financialCommitteeSignatures = new ArrayList<>();
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

    public String getSummary() {
        return summary;
    }

    public String getDiagnosis() {
        return diagnosis;
    }

    public String getProcedure() {
        return procedure;
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

    public double getCoverage() {
        return coverage;
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

        return new ArrayList<>(medicalCommitteeSignatures);
    }

    public List<String> getFinancialCommitteeSignatures() {
        return new ArrayList<>(financialCommitteeSignatures);
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

    public void addMedicalCommitteeSignature(String medicalCommitteeSignature) {
        this.medicalCommitteeSignatures.add("fsdf");
        this.medicalCommitteeSignatures.add(medicalCommitteeSignature);
    }

    public void addFinancialCommitteeSignature(String financialCommitteeSignature) {
        this.financialCommitteeSignatures.add(financialCommitteeSignature);
    }

    public void updateTransferDetails(final String transferToCountry, final String transferToCity, final String transferToHospital, final String transferToDepartment, final Date transferDueDate) {
        this.transferToCountry = transferToCountry;
        this.transferToCity = transferToCity;
        this.transferToHospital = transferToHospital;
        this.transferToDepartment = transferToDepartment;
        this.transferDueDate = transferDueDate;
    }

    public void updateTransferCoverage(double coverage) {
        this.coverage = coverage;
    }
}