package zw.co.netone.ussdreportsanalyser.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import zw.co.netone.ussdreportsanalyser.dto.ApiResponse;
import zw.co.netone.ussdreportsanalyser.model.Subscriber;
import zw.co.netone.ussdreportsanalyser.service.SubscriberService;

import java.util.List;
import java.util.Locale;

/**
 * REST Controller for subscriber operations
 * Provides endpoints for subscriber management including search and reset
 * @author btapfuma Oct2025
 */
@RestController
@RequestMapping("/api/v1/subscribers")
@RequiredArgsConstructor
@Slf4j
public class SubscriberController {

    private final SubscriberService subscriberService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<Subscriber>>> getAllSubscribers() {
        log.info("Request received to fetch all subscribers");
        ApiResponse<List<Subscriber>> response = subscriberService.findAllSubscribers();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<Subscriber>> findByMsisdn(
            @RequestParam String msisdn) {
        log.info("Request received to find subscriber by MSISDN: {}", msisdn);
        ApiResponse<Subscriber> response = subscriberService.findByMsisdn(msisdn);

        if (response.isSuccess()) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @DeleteMapping("/{msisdn}/reset")
    public ResponseEntity<ApiResponse<Void>> resetSubscriber(
            @PathVariable String msisdn) {
        log.info("Request received to reset subscriber with MSISDN: {}", msisdn);
        subscriberService.resetSubscriber(msisdn);

        ApiResponse<Void> response = ApiResponse.success(
                "Subscriber reset successfully", null);
        return ResponseEntity.ok(response);
    }


    @PostMapping("/reset")
    public ResponseEntity<ApiResponse<Void>> resetSubscriberByQuery(
            @RequestParam String msisdn,
            Locale locale) {
        log.info("Reset Request received to reset subscriber with MSISDN: {}", msisdn);
        subscriberService.resetSubscriber(msisdn);

        ApiResponse<Void> response = ApiResponse.success(
                "Subscriber reset successfully", null);
        return ResponseEntity.ok(response);
    }
}