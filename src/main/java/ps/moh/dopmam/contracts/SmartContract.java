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
import org.hyperledger.fabric.shim.ChaincodeStub;
import org.hyperledger.fabric.shim.ledger.KeyValue;
import org.hyperledger.fabric.shim.ledger.QueryResultsIterator;
import ps.moh.dopmam.models.Gender;
import ps.moh.dopmam.models.Patient;
import ps.moh.dopmam.models.Report;
import ps.moh.dopmam.utils.Utils;

import java.io.IOException;
import java.security.cert.CertificateException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static ps.moh.dopmam.utils.Utils.throwChaincodeException;

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
                        email = "ahmedelnemer02@gmail.com, waleedmortaja@protonmail.com, ahmedafifi1500@gmail.com",
                        name = "Ahmed A. El Nemer, Waleed M. Mortaja, Ahmed A. Afifi")))

@Default
public class SmartContract implements ContractInterface {
    private final Genson genson = new Genson();

    /**
     * Creates some initial Patients on the ledger.
     *
     * @param ctx the transaction context
     */
    @Transaction(intent = Transaction.TYPE.SUBMIT)
    public void initLedger(final Context ctx) throws ParseException {
        createPatient(ctx, 123456789, "Ahmed", "Mortaja", Gender.Male, new SimpleDateFormat("dd/MM/yyyy").parse("30-10-1997"), 2020123, new SimpleDateFormat("dd/MM/yyyy").parse("11/11/2011"));
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
    public Patient createPatient(final Context ctx,
                                 final long nationalId,
                                 final String firstName,
                                 final String lastName,
                                 final Gender gender,
                                 final Date dateOfBirth,
                                 final long insuranceNumber,
                                 final Date insuranceDueDate) {

        ChaincodeStub stub = ctx.getStub();

        if (patientExists(ctx, nationalId)) {
            String message = String.format("Patient with national id: %d already exists", nationalId);
            throwChaincodeException(message, Error.AlreadyExists);
        }

        Patient patient = new Patient(nationalId, firstName, lastName, gender, dateOfBirth, insuranceNumber, insuranceDueDate);
        String patientJSON = genson.serialize(patient);
        stub.putStringState(Long.toString(nationalId), patientJSON);
        return patient;
    }

    private boolean hasRole(final Context ctx, final String role) throws IOException {
        try {
            ChaincodeStub stub = ctx.getStub();
            final ClientIdentity identity = new ClientIdentity(stub);
            return identity.assertAttributeValue("role", role);
        } catch (CertificateException e) {
            return false;
        }
    }

    private String getRole(final Context ctx) throws IOException {
        try {
            ChaincodeStub stub = ctx.getStub();
            final ClientIdentity identity = new ClientIdentity(stub);
            return identity.getAttributeValue("role");
        } catch (CertificateException e) {
            // TODO test this exception occurs when field does not exist.
            return null;
        }

    }

    // TODO
    public String getClientId() {
        return "tempId";
    }

    // TODO
    public String getClientDepartment() {
        return "tempDepartment";
    }

    public void sign(final Context ctx, final long reportId) throws IOException {
        // TODO create ReportExist

        ChaincodeStub stub = ctx.getStub();
        String reportState = stub.getStringState(Long.toString(reportId));
        Report report = genson.deserialize(reportState, Report.class);

        // TODO test this
        switch (getRole(ctx)) {
            case "doctor":
                report.setDoctorSignature(getClientId());
                report.setDoctorDepartment(getClientDepartment());
                break;

            case "headOfDepartment":
                report.setHeadOfDepartmentSignature(getClientId());
                break;

            case "DOPMAM_medicalCommittee":
                report.addMedicalCommitteeSignature(getClientId());
                break;

            case "DOPMAM_financialCommittee":
                report.addFinancialCommitteeSignature(getClientId());
                break;

            default:
                throw new IllegalStateException("un expected user role!");
        }
    }

    @Transaction(intent = Transaction.TYPE.EVALUATE)
    private boolean patientExists(final Context ctx, final long nationalId) {
        ChaincodeStub stub = ctx.getStub();
        String patientJSON = stub.getStringState(Long.toString(nationalId));
        return Utils.isNotNullOrEmpty(patientJSON);
    }

    /**
     * Retrieves all patients from the ledger.
     *
     * @param ctx the transaction context
     * @return array of patients found on the ledger
     */
    @Transaction(intent = Transaction.TYPE.EVALUATE)
    public String getAllPatients(final Context ctx) {
        ChaincodeStub stub = ctx.getStub();

        List<Patient> queryResults = new ArrayList<>();

        QueryResultsIterator<KeyValue> results = stub.getStateByRange("", "");

        for (KeyValue result : results) {
            Patient patient = genson.deserialize(result.getStringValue(), Patient.class);
            queryResults.add(patient);
            System.out.println(patient.toString());
        }

        final String response = genson.serialize(queryResults);
        return response;
    }
}
