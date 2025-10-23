package zw.co.netone.ussdreportsanalyser.service;


import org.springframework.transaction.annotation.Transactional;
import zw.co.netone.ussdreportsanalyser.dto.ApiResponse;
import zw.co.netone.ussdreportsanalyser.dto.SubscriberDto;

import java.util.List;
import java.util.Optional;


public interface SubscriberService {


    @Transactional
    void resetSubscriber(String msisdn);

    ApiResponse<SubscriberDto> findByMsisdn(String msisdn);

    ApiResponse<Optional<List<SubscriberDto>>> findAllSubscribers();

}
