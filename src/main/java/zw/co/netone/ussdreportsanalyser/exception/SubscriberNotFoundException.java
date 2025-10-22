package zw.co.netone.ussdreportsanalyser.exception;

public class SubscriberNotFoundException extends RuntimeException {
    public SubscriberNotFoundException(String msisdn) {
        super(String.format("Subscriber with MSISDN %s not found", msisdn));
    }
}

/**
 * Exception thrown when mobile number validation fails
 */
class InvalidMsisdnException extends RuntimeException {
    public InvalidMsisdnException(String message) {
        super(message);
    }

}
