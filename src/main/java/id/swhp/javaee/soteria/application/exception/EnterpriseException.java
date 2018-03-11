package id.swhp.javaee.soteria.application.exception;

import javax.ejb.ApplicationException;

/**
 *
 * @author Sukma Wardana
 * @author Werner Keil
 * @since 1.1
 */
@ApplicationException(rollback = true)
public abstract class EnterpriseException extends RuntimeException {

    public EnterpriseException() {
        super();
    }

    public EnterpriseException(String message) {
        super(message);
    }

    public EnterpriseException(Throwable cause) {
        super(cause);
    }

    public EnterpriseException(String message, Throwable cause) {
        super(message, cause);
    }
}
