package ps.moh.dopmam.utils;

import org.hyperledger.fabric.shim.ledger.CompositeKey;

import java.text.SimpleDateFormat;
import java.util.Date;

public final class Utils {
    // prohibit instantiating the class
    private Utils() {
    }

    public static Result<Integer> StringToInt(final String s) {
        try {
            int value = Integer.parseInt(s);
            return new Result<Integer>(true, value, "");
        } catch (Exception e) {
            return new Result<Integer>(false, Integer.MIN_VALUE, e.getMessage());
        }
    }

    public static Result<Date> StringToDate(final String s) {
        try {
            Date value = new SimpleDateFormat("dd/MM/yyyy").parse(s);
            return new Result<Date>(true, value, "");
        } catch (Exception e) {
            return new Result<Date>(false, null, e.getMessage());
        }
    }

    public static Boolean IsNotNullOrEmpty(final String s) {
        return s != null && !s.isEmpty();
    }

    public static Result<String> GetCompositeKey(String objectType, String... arguments) {
        try {
            CompositeKey compositeKey = new CompositeKey(objectType, arguments);
            return new Result<String>(true, compositeKey.toString(),"");
        } catch (Exception e) {
            return new Result<String>(false, null, e.getMessage());
        }
    }
}
