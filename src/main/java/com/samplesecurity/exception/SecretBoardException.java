package com.samplesecurity.exception;

public class SecretBoardException extends RuntimeException{
    public SecretBoardException() {

    }

    public SecretBoardException(String message) {
        super(message);
    }

    public SecretBoardException(String message, Throwable cause) {
        super(message, cause);
    }

    public SecretBoardException(Throwable cause) {
        super(cause);
    }

    protected SecretBoardException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
