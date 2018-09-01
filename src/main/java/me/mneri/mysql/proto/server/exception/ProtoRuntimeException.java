package me.mneri.mysql.proto.server.exception;

public class ProtoRuntimeException extends RuntimeException {
    public ProtoRuntimeException() {
    }

    public ProtoRuntimeException(String message) {
        super(message);
    }

    public ProtoRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public ProtoRuntimeException(Throwable cause) {
        super(cause);
    }
}
