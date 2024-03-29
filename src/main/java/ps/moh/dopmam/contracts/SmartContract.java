package ps.moh.dopmam.contracts;

import com.owlike.genson.Genson;
import org.hyperledger.fabric.contract.ClientIdentity;
import org.hyperledger.fabric.contract.Context;
import org.hyperledger.fabric.contract.ContractInterface;
import org.hyperledger.fabric.contract.annotation.Contact;
import org.hyperledger.fabric.contract.annotation.Contract;
import org.hyperledger.fabric.contract.annotation.Default;
import org.hyperledger.fabric.contract.annotation.Info;
import org.hyperledger.fabric.contract.annotation.License;
import org.hyperledger.fabric.contract.annotation.Transaction;
import org.hyperledger.fabric.shim.ChaincodeException;
import org.hyperledger.fabric.shim.ChaincodeStub;
import org.hyperledger.fabric.shim.ledger.KeyValue;
import org.hyperledger.fabric.shim.ledger.QueryResultsIterator;
import ps.moh.dopmam.models.Gender;
import ps.moh.dopmam.models.Patient;
import ps.moh.dopmam.models.Report;
import ps.moh.dopmam.utils.Utils;

import java.io.IOException;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Contract(
        name = "dopmam_smart_contract",
        info = @Info(
                title = "DOPMAM SmartContract",
                description = "The DOPMAM referral system",
                version = "1.0.0",
                license = @License(
                        name = "Apache 2.0 License",
                        url = "http://www.apache.org/licenses/LICENSE-2.0.html"),
                contact = @Contact(
                        email = "ahmedelnemer02@gmail.com",
                        name = "Ahmed A. El Nemer, Waleed M. Mortaja")))

@Default
public class SmartContract implements ContractInterface {
    private static final String CERTIFICATE_ATTRIBUTE_NAME_ROLE = "roles";
    private final Genson genson = new Genson();

    /**
     * Initialize the ledger.
     *
     * @param ctx the transaction context
     */
    @Transaction(intent = Transaction.TYPE.SUBMIT)
    public void initLedger(final Context ctx) {
    }

    private String getDepartment(final Context ctx) throws CertificateException, IOException {
        ChaincodeStub stub = ctx.getStub();
        final ClientIdentity identity = new ClientIdentity(stub);
        String subject = identity.getX509Certificate().getSubjectX500Principal().toString();
        String OU = subject.split(",")[1];
        String departmentOU = OU.split("\\+")[2];
        return departmentOU.trim().substring(3);
    }

    private String getClientId(final Context ctx) throws CertificateException, IOException {
        ChaincodeStub stub = ctx.getStub();
        final ClientIdentity identity = new ClientIdentity(stub);
        String subject = identity.getX509Certificate().getSubjectX500Principal().toString();
        String CN = subject.split(",")[0];
        return CN.trim().substring(3);
    }

    private boolean hasRole(final Context ctx, String roleName) throws CertificateException, IOException {
        ChaincodeStub stub = ctx.getStub();
        final ClientIdentity identity = new ClientIdentity(stub);
        List<String> roles = Arrays.asList(identity.getAttributeValue(CERTIFICATE_ATTRIBUTE_NAME_ROLE).split(","));
        return roles.contains(String.valueOf('"') + roleName) || roles.contains(roleName) || roles.contains(roleName + String.valueOf('"')) || roles.contains(String.valueOf('"') + roleName + String.valueOf('"'));
    }

    private boolean canViewReport(final  Context ctx, long reportId) {
        ChaincodeStub stub = ctx.getStub();
        try {
            String key = stub.createCompositeKey("Report", Long.toString(reportId)).toString();
            String reportJSON = stub.getStringState(key);
            Report report = genson.deserialize(reportJSON, Report.class);
            String client = getClientId(ctx);
            String department = getDepartment(ctx);

            if(hasRole(ctx, "doctor") && report.getDoctorSignature().equals(client) && report.getDoctorDepartment().equals(department)){
                return true;
            } else if(hasRole(ctx, "head_department") && report.getDoctorDepartment().equals(department)) {
                return true;
            } else if(hasRole(ctx, "hospital_manager") && report.getHeadOfDepartmentSignature() != null) {
                return true;
            } else if(hasRole(ctx, "dopmam_medical_lead") && report.getHospitalManagerSignature() != null) {
                return true;
            } else if(hasRole(ctx, "dopmam_medical") && report.getMedicalCommitteeSignatures().size() > 0) {
                return true;
            } else if(hasRole(ctx, "dopmam_financial_lead") && report.getMedicalCommitteeSignatures().size() == 4) {
                return true;
            } else if(hasRole(ctx, "dopmam_financial") && report.getFinancialCommitteeSignatures().size() > 0) {
                return true;
            }
            return false;
        } catch (Exception e) {
            return  false;
        }
    }

    /**
     * Creates a new Patient on the ledger.
     *
     * @param ctx              the transaction context
     * @param nationalId       the nationalId of the new patient
     * @param firstName        the firstName of the new patient
     * @param lastName         the lastName of the new patient
     * @param gender           the gender of the new patient
     * @param dateOfBirth      the dateOfBirth of the new patient
     * @param insuranceNumber  the insuranceNumber of the new patient
     * @param insuranceDueDate the insuranceDueDate of the new patient
     * @return the created patient
     */
    @Transaction(intent = Transaction.TYPE.SUBMIT)
    public String createPatient(final Context ctx,
                                 final long nationalId,
                                 final String firstName,
                                 final String lastName,
                                 final String gender,
                                 final long dateOfBirth,
                                 final long insuranceNumber,
                                 final long insuranceDueDate) throws CertificateException, IOException {

        ChaincodeStub stub = ctx.getStub();

        if (patientExists(ctx, nationalId)) {
            String message = String.format("Patient with national id: '%d' already exists", nationalId);
            throw new ChaincodeException(message);
        }

        if(!hasRole(ctx, "doctor")){
            String message = String.format("Unauthorized access");
            throw new ChaincodeException(message);
        }

        Patient patient = new Patient(nationalId, firstName, lastName, Gender.valueOf(gender), new Date(dateOfBirth), insuranceNumber, new Date(insuranceDueDate));
        String patientJSON = genson.serialize(patient);

        String key = stub.createCompositeKey("Patient", Long.toString(nationalId)).toString();
        stub.putStringState(key, patientJSON);

        return String.format("Patient with national id: '%d' successfully added", nationalId);
    }

    @Transaction(intent = Transaction.TYPE.EVALUATE)
    private boolean patientExists(final Context ctx, final long nationalId) {
        ChaincodeStub stub = ctx.getStub();
        String key = stub.createCompositeKey("Patient", Long.toString(nationalId)).toString();
        String patientJSON = stub.getStringState(key);
        return Utils.isNotNullOrEmpty(patientJSON);
    }

    @Transaction(intent = Transaction.TYPE.SUBMIT)
    public void deletePatient(final Context ctx, final long nationalId) throws CertificateException, IOException {
        ChaincodeStub stub = ctx.getStub();

        if (!patientExists(ctx, nationalId)) {
            String message = String.format("Patient with national id: %d not exists", nationalId);
            throw new ChaincodeException(message);
        }


        if(!hasRole(ctx, "doctor")){
            String message = String.format("Unauthorized access");
            throw new ChaincodeException(message);
        }

        String key = stub.createCompositeKey("Patient", Long.toString(nationalId)).toString();
        stub.delState(key);
    }

    @Transaction(intent = Transaction.TYPE.EVALUATE)
    public Patient getPatient(final Context ctx, final long nationalId) {
        ChaincodeStub stub = ctx.getStub();

        if (!patientExists(ctx, nationalId)) {
            String message = String.format("Patient with national id: %d not exists", nationalId);
            throw new ChaincodeException(message);
        }

        String key = stub.createCompositeKey("Patient", Long.toString(nationalId)).toString();
        String patientJSON = stub.getStringState(key);
        return genson.deserialize(patientJSON, Patient.class);
    }

    private Long getNewReportId(final Context ctx) {
        ChaincodeStub stub = ctx.getStub();
        String key = stub.createCompositeKey("Report", "Next", "Id").toString();
        String result = stub.getStringState(key);
        long reportId;
        try {
            reportId = Long.parseLong(result) + 1;
        } catch (Exception e) {
            reportId = 1;
        }
        stub.putStringState(key, String.valueOf(reportId));
        return reportId;
    }

    @Transaction(intent = Transaction.TYPE.SUBMIT)
    public long createReport(
            final Context ctx,
            final long patientNationalId,
            final long reportDate,
            final String medicalHistoryAndClinicalFindings,
            final String diagnosis,
            final String recommendation
    ) throws CertificateException, IOException {
        ChaincodeStub stub = ctx.getStub();

        if (!patientExists(ctx, patientNationalId)) {
            String message = String.format("Patient with national id: %d not exists", patientNationalId);
            throw new ChaincodeException(message);
        }

        if(!hasRole(ctx, "doctor")){
            String message = String.format("Unauthorized access");
            throw new ChaincodeException(message);
        }

        long reportId = getNewReportId(ctx);

        Report report = new Report(reportId, patientNationalId, new Date(reportDate), medicalHistoryAndClinicalFindings, diagnosis, recommendation);

        String client = getClientId(ctx);
        String department = getDepartment(ctx);

        report.setDoctorSignature(client);
        report.setDoctorDepartment(department);

        String reportJSON = genson.serialize(report);

        String key = stub.createCompositeKey("Report", Long.toString(reportId)).toString();
        stub.putStringState(key, reportJSON);

        return reportId;
    }

    @Transaction(intent = Transaction.TYPE.EVALUATE)
    public Report getReport(final Context ctx, final long reportId) {
        ChaincodeStub stub = ctx.getStub();

        if (!reportExists(ctx, reportId)) {
            String message = String.format("Report with id: %d not exists", reportId);
            throw new ChaincodeException(message);
        }

        if(!canViewReport(ctx, reportId)){
            String message = String.format("Unauthorized access");
            throw new ChaincodeException(message);
        }

        String key = stub.createCompositeKey("Report", Long.toString(reportId)).toString();
        String reportJSON = stub.getStringState(key);
        return genson.deserialize(reportJSON, Report.class);
    }

    private boolean reportExists(Context ctx, long reportId) {
        ChaincodeStub stub = ctx.getStub();
        String key = stub.createCompositeKey("Report", Long.toString(reportId)).toString();
        String patientJSON = stub.getStringState(key);
        return Utils.isNotNullOrEmpty(patientJSON);
    }

    @Transaction(intent = Transaction.TYPE.EVALUATE)
    public String getReports(final Context ctx) throws CertificateException, IOException {
        ChaincodeStub stub = ctx.getStub();
        List<Report> reports = new ArrayList<>();

        QueryResultsIterator<KeyValue> results = stub.getStateByPartialCompositeKey("Report");

        for (KeyValue result : results) {
            try {
                Report report = genson.deserialize(result.getStringValue(), Report.class);
                System.out.println("Id: " + report.getReportId());
                if(canViewReport(ctx, report.getReportId())){
                    reports.add(report);
                }
            } catch (Exception e) {

            }
        }
        return genson.serialize(reports);
    }

    @Transaction(intent = Transaction.TYPE.SUBMIT)
    public void signReport(
            final Context ctx,
            final long reportId,
            final String country,
            final String city,
            final String hospital,
            final String dept,
            final long date,
            final double coverage
    ) throws CertificateException, IOException {
        ChaincodeStub stub = ctx.getStub();

        if (!reportExists(ctx, reportId)) {
            String message = String.format("Report with id: %d not exists", reportId);
            throw new ChaincodeException(message);
        }

        if (!canViewReport(ctx, reportId)) {
            String message = String.format("Unauthorized access");
            throw new ChaincodeException(message);
        }

        String key = stub.createCompositeKey("Report", Long.toString(reportId)).toString();
        String reportJSON = stub.getStringState(key);
        Report report = genson.deserialize(reportJSON, Report.class);
        if (!report.isRejected()) {
            try {

                String client = getClientId(ctx);
                String department = getDepartment(ctx);

                if (report.getDoctorSignature() == null && hasRole(ctx, "doctor")) {
                    report.setDoctorSignature(client);
                    report.setDoctorDepartment(department);
                    reportJSON = genson.serialize(report);
                    stub.putStringState(key, reportJSON);
                } else if (report.getHeadOfDepartmentSignature() == null && report.getDoctorDepartment().equals(department) && hasRole(ctx, "head_department")) {
                    report.setHeadOfDepartmentSignature(client);
                    reportJSON = genson.serialize(report);
                    stub.putStringState(key, reportJSON);
                } else if (report.getHospitalManagerSignature() == null && hasRole(ctx, "hospital_manager")) {
                    report.setHospitalManagerSignature(client);
                    reportJSON = genson.serialize(report);
                    stub.putStringState(key, reportJSON);
                } else if (report.getMedicalCommitteeSignatures().size() == 0 && hasRole(ctx, "dopmam_medical_lead")) {
                    List<String> signatures = report.getMedicalCommitteeSignatures();
                    if (!signatures.contains(client)) {
                        signatures.add(client);
                    }
                    report.setMedicalCommitteeSignatures(signatures);
                    report.setTransferToCountry(country);
                    report.setTransferToCity(city);
                    report.setTransferToHospital(hospital);
                    report.setTransferToDepartment(department);
                    report.setTransferDueDate(new Date(date));
                    reportJSON = genson.serialize(report);
                    stub.putStringState(key, reportJSON);
                } else if (report.getMedicalCommitteeSignatures().size() > 0 && hasRole(ctx, "dopmam_medical")) {
                    List<String> signatures = report.getMedicalCommitteeSignatures();
                    if (!signatures.contains(client)) {
                        signatures.add(client);
                    }
                    reportJSON = genson.serialize(report);
                    stub.putStringState(key, reportJSON);
                } else if (report.getFinancialCommitteeSignatures().size() == 0 && report.getMedicalCommitteeSignatures().size() ==4 && hasRole(ctx, "dopmam_financial_lead")) {
                    List<String> signatures = report.getFinancialCommitteeSignatures();
                    if (!signatures.contains(client)) {
                        signatures.add(client);
                    }
                    report.setCoverage(coverage);
                    System.out.println("Debug Coverage: " + String.valueOf(coverage));
                    reportJSON = genson.serialize(report);
                    stub.putStringState(key, reportJSON);
                } else if (report.getFinancialCommitteeSignatures().size() > 0 && hasRole(ctx, "dopmam_financial")) {
                    List<String> signatures = report.getFinancialCommitteeSignatures();
                    if (!signatures.contains(client)) {
                        signatures.add(client);
                    }
                    reportJSON = genson.serialize(report);
                    stub.putStringState(key, reportJSON);
                }
            } catch (Exception e) {
            }
        }
    }

    @Transaction(intent = Transaction.TYPE.SUBMIT)
    public void rejectReport(
            final Context ctx,
            final long reportId) throws CertificateException, IOException {
        ChaincodeStub stub = ctx.getStub();

        if (!reportExists(ctx, reportId)) {
            String message = String.format("Report with id: %d not exists", reportId);
            throw new ChaincodeException(message);
        }

        if (!canViewReport(ctx, reportId)) {
            String message = String.format("Unauthorized access");
            throw new ChaincodeException(message);
        }

        String key = stub.createCompositeKey("Report", Long.toString(reportId)).toString();
        String reportJSON = stub.getStringState(key);
        Report report = genson.deserialize(reportJSON, Report.class);
        if (report.isRejected()) {
            String message = String.format("Report with id: %d is already rejected", reportId);
            throw new ChaincodeException(message);
        }
        try {

            String client = getClientId(ctx);
            String department = getDepartment(ctx);

            if (report.getHeadOfDepartmentSignature() == null && report.getDoctorDepartment().equals(department) && hasRole(ctx, "head_department")) {
                report.setHeadOfDepartmentSignature(client);
                report.setRejected(true);
                reportJSON = genson.serialize(report);
                stub.putStringState(key, reportJSON);
            } else if (report.getHospitalManagerSignature() == null && hasRole(ctx, "hospital_manager")) {
                report.setHospitalManagerSignature(client);
                report.setRejected(true);
                reportJSON = genson.serialize(report);
                stub.putStringState(key, reportJSON);
            } else if (report.getMedicalCommitteeSignatures().size() == 0 && hasRole(ctx, "dopmam_medical_lead")) {
                List<String> signatures = report.getMedicalCommitteeSignatures();
                if (!signatures.contains(client)) {
                    signatures.add(client);
                }
                report.setMedicalCommitteeSignatures(signatures);
                report.setRejected(true);
                reportJSON = genson.serialize(report);
                stub.putStringState(key, reportJSON);
            } else if (report.getMedicalCommitteeSignatures().size() > 0 && hasRole(ctx, "dopmam_medical")) {
                List<String> signatures = report.getMedicalCommitteeSignatures();
                if (!signatures.contains(client)) {
                    signatures.add(client);
                }
                report.setRejected(true);
                reportJSON = genson.serialize(report);
                stub.putStringState(key, reportJSON);
            } else if (report.getFinancialCommitteeSignatures().size() == 0 && report.getMedicalCommitteeSignatures().size() == 4 && hasRole(ctx, "dopmam_financial_lead")) {
                List<String> signatures = report.getFinancialCommitteeSignatures();
                if (!signatures.contains(client)) {
                    signatures.add(client);
                }
                report.setRejected(true);
                reportJSON = genson.serialize(report);
                stub.putStringState(key, reportJSON);
            } else if (report.getFinancialCommitteeSignatures().size() > 0 && hasRole(ctx, "dopmam_financial")) {
                List<String> signatures = report.getFinancialCommitteeSignatures();
                if (!signatures.contains(client)) {
                    signatures.add(client);
                }
                report.setRejected(true);
                reportJSON = genson.serialize(report);
                stub.putStringState(key, reportJSON);
            }
        } catch (Exception e) {
        }
    }
}
