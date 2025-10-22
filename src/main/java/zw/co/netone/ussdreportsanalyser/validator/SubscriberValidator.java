package zw.co.netone.ussdreportsanalyser.validator;

import org.springframework.stereotype.Component;

@Component
public class SubscriberValidator {

    private static final int MIN_MSISDN_LENGTH = 9;
    private static final int MAX_MSISDN_LENGTH = 13;
    private static final String NUMERIC_PATTERN = "[0-9]+";


    public ValidationResult validateMsisdn(String msisdn) {
        if (msisdn == null || msisdn.trim().isEmpty()) {
            return ValidationResult.invalid("Mobile number cannot be empty");
        }

        String cleanedNumber = msisdn.trim();

        if (!cleanedNumber.matches(NUMERIC_PATTERN)) {
            return ValidationResult.invalid("Mobile number must contain only digits");
        }

        if (cleanedNumber.length() < MIN_MSISDN_LENGTH) {
            return ValidationResult.invalid("Mobile number is too short");
        }

        if (cleanedNumber.length() > MAX_MSISDN_LENGTH) {
            return ValidationResult.invalid("Mobile number is too long");
        }

        return ValidationResult.valid();
    }

    public static class ValidationResult {
        private final boolean valid;
        private final String message;

        private ValidationResult(boolean valid, String message) {
            this.valid = valid;
            this.message = message;
        }

        public static ValidationResult valid() {
            return new ValidationResult(true, "Valid");
        }

        public static ValidationResult invalid(String message) {
            return new ValidationResult(false, message);
        }

        public boolean isValid() {
            return valid;
        }

        public String getMessage() {
            return message;
        }
    }
}