package zw.co.netone.ussdreportsanalyser.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import zw.co.netone.ussdreportsanalyser.dto.ApiResponse;
import zw.co.netone.ussdreportsanalyser.dto.SubscriberDto;
import zw.co.netone.ussdreportsanalyser.exception.SubscriberNotFoundException;
import zw.co.netone.ussdreportsanalyser.exception.SubscriberResetNotEligibleException;
import zw.co.netone.ussdreportsanalyser.repository.SubscriberRepository;
import zw.co.netone.ussdreportsanalyser.security.CurrentAuditor;
import zw.co.netone.ussdreportsanalyser.validator.SubscriberResetEligibilityChecker;
import zw.co.netone.ussdreportsanalyser.validator.SubscriberValidator;

import java.util.List;
import java.util.Optional;

/**
 * Service implementation for subscriber operations
 * Following Single Responsibility Principle and Dependency Inversion
 * @author btapfuma Oct2025
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
    public ApiResponse<Optional<List<SubscriberDto>>> findAllSubscribers() {
        log.debug("Fetching all subscribers");
        Optional<List<SubscriberDto>> subscribers = subscriberRepository.findAll();
        log.info("Successfully fetched {} subscribers", subscribers.orElse(List.of()).size());
        return ApiResponse.success("Fetched all subscribers", subscribers);
    }

    @Transactional
    @Override
    public void resetSubscriber(String msisdn) {
        log.info("Initiating reset for mobile number: {}", msisdn);

        String normalizedMsisdn = msisdnNormalizer.normalize(msisdn);
        validateMsisdn(normalizedMsisdn);

        SubscriberDto subscriber = findSubscriberByMsisdn(normalizedMsisdn);
        log.debug("Found subscriber for reset: ID={}, MSISDN={}", subscriber.id(),
                normalizedMsisdn);

        checkResetEligibility(subscriber);

        performReset(subscriber);

        log.info("Successfully reset subscriber with MSISDN: {}", normalizedMsisdn);
    }

    @Override
    @Transactional(readOnly = true)
    public ApiResponse<SubscriberDto> findByMsisdn(String msisdn) {
        log.debug("Searching for subscriber by MSISDN: {}", msisdn);

        String normalizedMsisdn = msisdnNormalizer.normalize(msisdn);

        SubscriberValidator.ValidationResult validationResult =
                subscriberValidator.validateMsisdn(normalizedMsisdn);

        if (!validationResult.isValid()) {
            log.warn("Invalid MSISDN format: {}", validationResult.getMessage());
            return ApiResponse.failure(validationResult.getMessage(), null);
        }

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

    private void validateMsisdn(String msisdn) {
        SubscriberValidator.ValidationResult validationResult =
                subscriberValidator.validateMsisdn(msisdn);

        if (!validationResult.isValid()) {
            log.error("MSISDN validation failed: {}", validationResult.getMessage());
            throw new SubscriberNotFoundException(validationResult.getMessage());
        }
    }

    private SubscriberDto findSubscriberByMsisdn(String msisdn) {
        return subscriberRepository.findByMsisdn(msisdn)
                .orElseThrow(() -> {
                    log.error("Subscriber not found with MSISDN: {}", msisdn);
                    return new SubscriberNotFoundException(msisdn);
                });
    }

    private void checkResetEligibility(SubscriberDto subscriber) {
        SubscriberResetEligibilityChecker.EligibilityResult eligibilityResult =
                eligibilityChecker.checkEligibility(subscriber);

        if (!eligibilityResult.isEligible()) {
            log.error("Subscriber reset eligibility check failed: {}", eligibilityResult.getReason());
            throw new SubscriberResetNotEligibleException(eligibilityResult.getReason());
        }

        log.debug("Subscriber eligible for reset: {}", eligibilityResult.getReason());
    }

    private void performReset(SubscriberDto subscriber) {
        try {
            log.debug("Deleting subscriber with ID: {}- details {}", subscriber.id(),subscriber);
            subscriberRepository.deleteById(subscriber.id());

        } catch (Exception e) {
            log.error("Error during subscriber reset for ID: {}", subscriber.id(), e);
            throw new RuntimeException("Failed to reset subscriber. Please try again later", e);
        }
    }
}