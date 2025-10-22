package zw.co.netone.ussdreportsanalyser.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import zw.co.netone.ussdreportsanalyser.dto.ApiResponse;
import zw.co.netone.ussdreportsanalyser.exception.SubscriberNotFoundException;
import zw.co.netone.ussdreportsanalyser.exception.SubscriberResetNotEligibleException;
import zw.co.netone.ussdreportsanalyser.model.Subscriber;
import zw.co.netone.ussdreportsanalyser.repository.SubscriberRepository;
import zw.co.netone.ussdreportsanalyser.security.CurrentAuditor;
import zw.co.netone.ussdreportsanalyser.validator.SubscriberResetEligibilityChecker;
import zw.co.netone.ussdreportsanalyser.validator.SubscriberValidator;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;

/**
 * Service implementation for subscriber operations
 * Following Single Responsibility Principle and Dependency Inversion
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class SubscriberServiceImpl implements SubscriberService {

    private final SubscriberRepository subscriberRepository;
    private final CurrentAuditor currentAuditor;
    private final SubscriberValidator subscriberValidator;
    private final SubscriberResetEligibilityChecker eligibilityChecker;
    private final MsisdnNormalizer msisdnNormalizer;

    @Override
    @Transactional(readOnly = true)
    public ApiResponse<List<Subscriber>> findAllSubscribers() {
        log.debug("Fetching all subscribers");
        List<Subscriber> subscribers = subscriberRepository.findAll();
        log.info("Successfully fetched {} subscribers", subscribers.size());
        return ApiResponse.success("Fetched all subscribers", subscribers);
    }

    @Transactional
    @Override
    public void resetSubscriber(String msisdn, Locale locale) {
        log.info("Initiating reset for mobile number: {}", msisdn);

        // Normalize and validate MSISDN
        String normalizedMsisdn = msisdnNormalizer.normalize(msisdn);
        validateMsisdn(normalizedMsisdn);

        // Find subscriber
        Subscriber subscriber = findSubscriberByMsisdn(normalizedMsisdn);
        log.debug("Found subscriber for reset: ID={}, MSISDN={}", subscriber.getId(), normalizedMsisdn);

        // Check eligibility
        checkResetEligibility(subscriber);

        // Perform reset
        performReset(subscriber);

        log.info("Successfully reset subscriber with MSISDN: {}", normalizedMsisdn);
    }

    @Override
    @Transactional(readOnly = true)
    public ApiResponse<Subscriber> findByMsisdn(String msisdn) {
        log.debug("Searching for subscriber by MSISDN: {}", msisdn);

        // Normalize MSISDN
        String normalizedMsisdn = msisdnNormalizer.normalize(msisdn);

        // Validate MSISDN format
        SubscriberValidator.ValidationResult validationResult =
                subscriberValidator.validateMsisdn(normalizedMsisdn);

        if (!validationResult.isValid()) {
            log.warn("Invalid MSISDN format: {}", validationResult.getMessage());
            return ApiResponse.failure(validationResult.getMessage(), null);
        }

        // Search for subscriber
        return subscriberRepository.findByMsisdn(normalizedMsisdn)
                .map(subscriber -> {
                    log.info("Subscriber found with MSISDN: {}", normalizedMsisdn);
                    return ApiResponse.success("Mobile number found", subscriber);
                })
                .orElseGet(() -> {
                    log.info("Subscriber not found with MSISDN: {}", normalizedMsisdn);
                    return ApiResponse.failure("Mobile number not found", null);
                });
    }

    /**
     * Validates MSISDN format
     */
    private void validateMsisdn(String msisdn) {
        SubscriberValidator.ValidationResult validationResult =
                subscriberValidator.validateMsisdn(msisdn);

        if (!validationResult.isValid()) {
            log.error("MSISDN validation failed: {}", validationResult.getMessage());
            throw new SubscriberNotFoundException(validationResult.getMessage());
        }
    }

    /**
     * Finds subscriber or throws exception
     */
    private Subscriber findSubscriberByMsisdn(String msisdn) {
        return subscriberRepository.findByMsisdn(msisdn)
                .orElseThrow(() -> {
                    log.error("Subscriber not found with MSISDN: {}", msisdn);
                    return new SubscriberNotFoundException(msisdn);
                });
    }

    /**
     * Checks if subscriber is eligible for reset
     */
    private void checkResetEligibility(Subscriber subscriber) {
        SubscriberResetEligibilityChecker.EligibilityResult eligibilityResult =
                eligibilityChecker.checkEligibility(subscriber);

        if (!eligibilityResult.isEligible()) {
            log.error("Subscriber reset eligibility check failed: {}", eligibilityResult.getReason());
            throw new SubscriberResetNotEligibleException(eligibilityResult.getReason());
        }

        log.debug("Subscriber eligible for reset: {}", eligibilityResult.getReason());
    }

    /**
     * Performs the actual reset operation
     */
    private void performReset(Subscriber subscriber) {
        try {
            // Update audit fields
            subscriber.setLastModifiedDate(LocalDateTime.now());
            subscriber.setLastModifiedBy(currentAuditor.getUsernameOrThrow());

            log.debug("Deleting subscriber with ID: {}", subscriber.getId());
            subscriberRepository.deleteById(subscriber.getId());

        } catch (Exception e) {
            log.error("Error during subscriber reset for ID: {}", subscriber.getId(), e);
            throw new RuntimeException("Failed to reset subscriber. Please try again later", e);
        }
    }
}