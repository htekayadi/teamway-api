package io.teamway.exceptions;

public class ShiftAlreadyExistsException extends Exception {

    public ShiftAlreadyExistsException() {
        super();
    }

    public ShiftAlreadyExistsException(String message) {
        super(message);
    }

    public ShiftAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }

    public ShiftAlreadyExistsException(Throwable cause) {
        super(cause);
    }

    protected ShiftAlreadyExistsException(String message, Throwable cause,
                                          boolean enableSuppression,
                                          boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
