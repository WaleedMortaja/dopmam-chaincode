package ps.moh.dopmam.utils;

public final class Utils {
    // prohibit instantiating the class
    private Utils() {
    }

    public static Result<Integer> StringToInt(final String s) {
        try {
            int value = Integer.parseInt(s);
            return new Result<>(true, value, "");
        } catch (Exception e) {
            return new Result<>(false, Integer.MIN_VALUE, e.getMessage());
        }
    }
}
