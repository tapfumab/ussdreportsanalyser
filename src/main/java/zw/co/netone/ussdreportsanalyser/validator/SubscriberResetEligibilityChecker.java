package zw.co.netone.ussdreportsanalyser.validator;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import zw.co.netone.ussdreportsanalyser.dto.SubscriberDto;
import zw.co.netone.ussdreportsanalyser.enums.RegistrationStatus;

@Slf4j
@Component
public class SubscriberResetEligibilityChecker {

    private static final String USSD_SOURCE = "USSD";
    private static final int MAX_ATTEMPTS_THRESHOLD = 3;

    public EligibilityResult checkEligibility(SubscriberDto subscriber) {
        if (subscriber == null) {
            return EligibilityResult.ineligible("Subscriber not found");
        }

        if (!USSD_SOURCE.equalsIgnoreCase(subscriber.source())) {
            log.warn("Reset attempted on non-USSD subscriber. Source: {}", subscriber.source());
            return EligibilityResult.ineligible("Only USSD subscribers can be reset");
        }

        if (!RegistrationStatus.INACTIVE.equals(subscriber.status())) {
            log.warn("Reset attempted on active subscriber. Status: {}", subscriber.status());
            return EligibilityResult.ineligible("Only inactive subscribers can be reset");
        }

        if (!subscriber.isAccountLocked()) {
            return EligibilityResult.ineligible("Account must be locked for reset");
        }

        if (subscriber.attempts() <= MAX_ATTEMPTS_THRESHOLD) {
            return EligibilityResult.ineligible(
                    String.format("Attempts (%d) must exceed threshold (%d)",
                            subscriber.attempts(), MAX_ATTEMPTS_THRESHOLD)
            );
        }

        return EligibilityResult.eligible();
    }

    public static class EligibilityResult {
        private final boolean eligible;
        private final String reason;

        private EligibilityResult(boolean eligible, String reason) {
            this.eligible = eligible;
            this.reason = reason;
        }

        public static EligibilityResult eligible() {
            return new EligibilityResult(true, "Eligible for reset");
        }

        public static EligibilityResult ineligible(String reason) {
            return new EligibilityResult(false, reason);
        }

        public boolean isEligible() {
            return eligible;
        }

        public String getReason() {
            return reason;
        }
    }
}