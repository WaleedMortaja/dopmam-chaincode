package ps.moh.dopmam.contracts;

import com.owlike.genson.Genson;
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
import ps.moh.dopmam.utils.Utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Contract(
        name = "dopmam_smart_contract",
        info = @Info(
                title = "DOPMAM SmartContract",
                description = "The hyperlegendary asset transfer",
                version = "1.0.0",
                license = @License(
                        name = "Apache 2.0 License",
                        url = "http://www.apache.org/licenses/LICENSE-2.0.html"),
                contact = @Contact(
                        email = "a.transfer@example.com",
                        name = "Adrian Transfer",
                        url = "https://hyperledger.example.com")))
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
        CreatePatient(ctx, "123456789", "Ahmed", "Mortaja", Gender.Male, new SimpleDateFormat("dd/MM/yyyy").parse("30-10-1997"), "2020123", new SimpleDateFormat("dd/MM/yyyy").parse("11/11/2011"));
    }

    /**
     * Creates a new Patient on the ledger.
     *
     * @param ctx the transaction context
     * @param nationalId the nationalId of the new patient
     * @param firstName the firstName of the new patient
     * @param lastName the lastName of the new patient
     * @param gender the gender of the new patient
     * @param dateOfBirth the dateOfBirth of the new patient
     * @param insuranceNumber the insuranceNumber of the new patient
     * @param insuranceDueDate the insuranceDueDate of the new patient
     * @return the created patient
     */
    @Transaction(intent = Transaction.TYPE.SUBMIT)
    public Patient CreatePatient(final Context ctx,
                                 final String nationalId,
                                 final String firstName,
                                 final String lastName,
                                 final Gender gender,
                                 final Date dateOfBirth,
                                 final String insuranceNumber,
                                 final Date insuranceDueDate) {

        ChaincodeStub stub = ctx.getStub();

        if (PatientExists(ctx, nationalId)) {
            //TODO: Edit this peace of code
//            String errorMessage = String.format("Patient %s already exists", assetID);
//            System.out.println(errorMessage);
//            throw new ChaincodeException(errorMessage, AssetTransferErrors.ASSET_ALREADY_EXISTS.toString());
            throw new ChaincodeException(Error.AlreadyExists.toString());
        }

        Patient patient = new Patient(nationalId, firstName, lastName, gender, dateOfBirth, insuranceNumber, insuranceDueDate);
        String patientJSON = genson.serialize(patient);
        stub.putStringState(nationalId, patientJSON);
        return patient;
    }

    /**
     * Deletes patient on the ledger.
     *
     * @param ctx the transaction context
     * @param nationalId the nationalId of the patient being deleted
     */
    @Transaction(intent = Transaction.TYPE.SUBMIT)
    public void DeleteAsset(final Context ctx, final String nationalId) {
        ChaincodeStub stub = ctx.getStub();

        if (!PatientExists(ctx, nationalId)) {
            //TODO: Edit this also
//            String errorMessage = String.format("Patient %s does not exist", nationalId);
//            System.out.println(errorMessage);
//            throw new ChaincodeException(errorMessage, Error.NotFound.toString());
            throw new ChaincodeException(Error.NotFound.toString());
        }

        stub.delState(nationalId);
    }

    @Transaction(intent = Transaction.TYPE.EVALUATE)
    private boolean PatientExists(final Context ctx, final String nationalId) {
        ChaincodeStub stub = ctx.getStub();
        String patientJSON = stub.getStringState(nationalId);
        return Utils.IsNotNullOrEmpty(patientJSON);
    }

    /**
     * Retrieves all patients from the ledger.
     *
     * @param ctx the transaction context
     * @return array of patients found on the ledger
     */
    @Transaction(intent = Transaction.TYPE.EVALUATE)
    public String GetAllPatients(final Context ctx) {
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
