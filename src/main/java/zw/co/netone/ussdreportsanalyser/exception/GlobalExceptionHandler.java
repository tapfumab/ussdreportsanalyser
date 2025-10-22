package zw.co.netone.ussdreportsanalyser.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import zw.co.netone.ussdreportsanalyser.dto.ApiResponse;

/**
 * Global exception handler for subscriber-related exceptions
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(SubscriberNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleSubscriberNotFound(SubscriberNotFoundException ex) {
        log.error("Subscriber not found: {}", ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.failure(ex.getMessage()));
    }

    @ExceptionHandler(SubscriberResetNotEligibleException.class)
    public ResponseEntity<ApiResponse<Void>> handleResetNotEligible(SubscriberResetNotEligibleException ex) {
        log.error("Subscriber reset not eligible: {}", ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.failure(ex.getMessage()));
    }

    @ExceptionHandler(InvalidMsisdnException.class)
    public ResponseEntity<ApiResponse<Void>> handleInvalidMsisdn(InvalidMsisdnException ex) {
        log.error("Invalid MSISDN: {}", ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.failure(ex.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleGenericException(Exception ex) {
        log.error("Unexpected error occurred", ex);
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.failure("An unexpected error occurred. Please try again later"));
    }
}