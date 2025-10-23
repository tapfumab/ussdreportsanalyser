package zw.co.netone.ussdreportsanalyser.dto;

import zw.co.netone.ussdreportsanalyser.enums.RegistrationStatus;

public record SubscriberDto(Long id,
                            String msisdn,
                            String passphrase,
                            String pin,
                            String source,
                            boolean isAccountLocked,
                            RegistrationStatus status,
                            int attempts
) {
}
