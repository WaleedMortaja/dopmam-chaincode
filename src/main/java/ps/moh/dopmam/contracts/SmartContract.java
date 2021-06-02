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

    @Transaction(intent = Transaction.TYPE.EVALUATE)
    private String getDepartment(final Context ctx) throws CertificateException, IOException {
        ChaincodeStub stub = ctx.getStub();
        final ClientIdentity identity = new ClientIdentity(stub);
        String subject = identity.getX509Certificate().getSubjectX500Principal().toString();
        String OU = subject.split(",")[1];
        String departmentOU = OU.split("\\+")[2];
        return departmentOU.trim().substring(3);
    }

    @Transaction(intent = Transaction.TYPE.EVALUATE)
    private String getClientId(final Context ctx) throws CertificateException, IOException {
        ChaincodeStub stub = ctx.getStub();
        final ClientIdentity identity = new ClientIdentity(stub);
        String subject = identity.getX509Certificate().getSubjectX500Principal().toString();
        String CN = subject.split(",")[0];
        return CN.trim().substring(3);
    }

    @Transaction(intent = Transaction.TYPE.EVALUATE)
    private boolean hasRole(final Context ctx, String roleName) throws CertificateException, IOException {
        ChaincodeStub stub = ctx.getStub();
        final ClientIdentity identity = new ClientIdentity(stub);
        List<String> roles = Arrays.asList(identity.getAttributeValue(CERTIFICATE_ATTRIBUTE_NAME_ROLE).split(","));
        return roles.contains(String.valueOf('"') + roleName) || roles.contains(roleName) || roles.contains(roleName + String.valueOf('"')) || roles.contains(String.valueOf('"') + roleName + String.valueOf('"'));
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
                                 final long insuranceDueDate) {

        ChaincodeStub stub = ctx.getStub();

        if (patientExists(ctx, nationalId)) {
            String message = String.format("Patient with national id: '%d' already exists", nationalId);
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
    public void deletePatient(final Context ctx, final long nationalId) {
        ChaincodeStub stub = ctx.getStub();

        if (!patientExists(ctx, nationalId)) {
            String message = String.format("Patient with national id: %d not exists", nationalId);
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
            final String summary,
            final String diagnosis,
            final String procedure
    ) {
        ChaincodeStub stub = ctx.getStub();

        if (!patientExists(ctx, patientNationalId)) {
            String message = String.format("Patient with national id: %d not exists", patientNationalId);
            throw new ChaincodeException(message);
        }

        long reportId = getNewReportId(ctx);
        Report report = new Report(reportId, patientNationalId, new Date(reportDate), summary, diagnosis, procedure);
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
        List<String> reports = new ArrayList<>();
        String client = getClientId(ctx);
        String department = getDepartment(ctx);

        String key = "";
        QueryResultsIterator<KeyValue> results = stub.getStateByRange(key, key);

        for (KeyValue result : results) {
            //Report report = genson.deserialize(result.getStringValue(), Report.class);

//            if(hasRole(ctx, "doctor") && report.getDoctorSignature().equals(client) && report.getDoctorDepartment().equals(department)){
//                reports.add(report);
//            } else if(hasRole(ctx, "head_department") && report.getDoctorDepartment().equals(department)) {
//                reports.add(report);
//            } else if(hasRole(ctx, "dopmam_medical_lead") && report.getMedicalCommitteeSignatures().size() == 0) {
//                reports.add(report);
//            } else if(hasRole(ctx, "dopmam_financial_lead") && report.getFinancialCommitteeSignatures().size() == 0) {
//                reports.add(report);
//            } else if(hasRole(ctx, "dopmam_medical") && report.getMedicalCommitteeSignatures().size() > 0) {
//                reports.add(report);
//            } else if(hasRole(ctx, "dopmam_financial") && report.getFinancialCommitteeSignatures().size() > 0) {
//                reports.add(report);
//            } else if(hasRole(ctx, "hospital_manager")) {
//                reports.add(report);
//            }
            reports.add(result.getStringValue());
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
            final double coverag
    ) {
        ChaincodeStub stub = ctx.getStub();

        if(!reportExists(ctx, reportId)) {
            String message = String.format("Report with id: %d not exists", reportId);
            throw new ChaincodeException(message);
        }

        try {
            String key = stub.createCompositeKey("Report", Long.toString(reportId)).toString();
            String reportJSON = stub.getStringState(key);
            Report report = genson.deserialize(reportJSON, Report.class);
            String client = getClientId(ctx);
            String department = getDepartment(ctx);

            if(report.getDoctorSignature() == null && hasRole(ctx, "doctor")){
                report.setDoctorSignature(client);
                report.setDoctorDepartment(department);

                reportJSON = genson.serialize(report);
                stub.putStringState(key, reportJSON);
            } else if(report.getHeadOfDepartmentSignature() == null && report.getDoctorDepartment().equals(department) && hasRole(ctx, "head_department")) {
                report.setHeadOfDepartmentSignature(client);

                reportJSON = genson.serialize(report);
                stub.putStringState(key, reportJSON);
            } else if(report.getHospitalManagerSignature() == null && hasRole(ctx,"hospital_manager")) {
                report.setHospitalManagerSignature(client);

                reportJSON = genson.serialize(report);
                stub.putStringState(key, reportJSON);
            }

            report.addMedicalCommitteeSignature(client);
            reportJSON = genson.serialize(report);
            stub.putStringState(key, reportJSON);
//
//            else if(report.getMedicalCommitteeSignatures().size() == 0 && hasRole(ctx, "dopmam_medical_lead")) {
//                report.addMedicalCommitteeSignature(client);
//                report.updateTransferDetails(country, city, hospital, dept, new Date(date));
//
//                reportJSON = genson.serialize(report);
//                stub.putStringState(key, reportJSON);
//            } else if(report.getMedicalCommitteeSignatures().size() > 0 && hasRole(ctx, "dopmam_medical")) {
//                report.addMedicalCommitteeSignature(client);
//
//                reportJSON = genson.serialize(report);
//                stub.putStringState(key, reportJSON);
//            } else if(report.getFinancialCommitteeSignatures().size() == 0 && hasRole(ctx, "dopmam_financial_lead")) {
//                report.addFinancialCommitteeSignature(client);
//                report.updateTransferCoverage(coverag);
//
//                reportJSON = genson.serialize(report);
//                stub.putStringState(key, reportJSON);
//            } else if(report.getFinancialCommitteeSignatures().size() > 0 && hasRole(ctx, "dopmam_financial")) {
//                report.addFinancialCommitteeSignature(client);
//
//                reportJSON = genson.serialize(report);
//                stub.putStringState(key, reportJSON);
//            }
        } catch (Exception e) {
            throw new ChaincodeException(e);
        }
    }
}
