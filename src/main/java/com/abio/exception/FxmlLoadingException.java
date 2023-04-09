package com.abio.exception;
public class FxmlLoadingException extends RuntimeException {
    private static final long serialVersionUID = 8737702296922693068L;

    public FxmlLoadingException() {
        super();
    }

    public FxmlLoadingException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public FxmlLoadingException(String message, Throwable cause) {
        super(message, cause);
    }

    public FxmlLoadingException(String message) {
        super(message);
    }

    public FxmlLoadingException(Throwable cause) {
        super(cause);
    }
}
