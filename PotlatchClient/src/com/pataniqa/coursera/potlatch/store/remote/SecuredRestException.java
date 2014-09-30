package com.pataniqa.coursera.potlatch.store.remote;

/**
 * A special class made to specify exceptions that are thrown by our
 * SecuredRestBuilder.
 * 
 * A more robust implementation would probably have fields for tracking
 * the type of exception (e.g., bad password, etc.).
 * 
 * @author jules
 *
 */
public class SecuredRestException extends RuntimeException {

    public SecuredRestException() {
        super();
    }

    public SecuredRestException(String message, Throwable cause) {
        super(message, cause);
    }

    public SecuredRestException(String message) {
        super(message);
    }

    public SecuredRestException(Throwable cause) {
        super(cause);
    }

}
