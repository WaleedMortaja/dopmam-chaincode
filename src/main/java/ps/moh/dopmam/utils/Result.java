package ps.moh.dopmam.utils;

public class Result<T> {
    public final boolean success;
    public final T payload;
    public final String message;

    public Result(final boolean success, final T payload, final String message) {
        this.success = success;
        this.payload = payload;
        this.message = message;
    }
}
