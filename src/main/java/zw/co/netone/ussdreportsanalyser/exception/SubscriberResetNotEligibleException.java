package zw.co.netone.ussdreportsanalyser.exception;

/**
 * Exception thrown when subscriber is not eligible for reset
 */
public class SubscriberResetNotEligibleException extends RuntimeException {
    public SubscriberResetNotEligibleException(String reason) {
        super(String.format("Subscriber reset not allowed: %s", reason));
    }
}
