package zw.co.netone.ussdreportsanalyser.service;

import org.springframework.stereotype.Component;
import zw.co.netone.utils.MobileNumberUtils;

/**
 * Normalizes MSISDN to consistent format
 */
@Component
public class MsisdnNormalizer {

    /**
     * Normalizes the MSISDN by trimming and converting to 263 format
     *
     * @param msisdn The mobile number to normalize
     * @return Normalized MSISDN
     */
    public String normalize(String msisdn) {
        if (msisdn == null) {
            return null;
        }

        String cleaned = msisdn.trim();
        return MobileNumberUtils.withTwoSixThree(cleaned);
    }
}