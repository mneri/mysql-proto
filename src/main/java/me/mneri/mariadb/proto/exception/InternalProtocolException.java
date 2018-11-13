package me.mneri.mariadb.proto.exception;

public class InternalProtocolException extends RuntimeException {
    public InternalProtocolException() {
    }

    public InternalProtocolException(String message) {
        super(message);
    }

    public InternalProtocolException(String message, Throwable cause) {
        super(message, cause);
    }

    public InternalProtocolException(Throwable cause) {
        super(cause);
    }
}
