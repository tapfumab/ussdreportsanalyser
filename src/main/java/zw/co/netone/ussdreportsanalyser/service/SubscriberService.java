package zw.co.netone.ussdreportsanalyser.service;


import org.springframework.transaction.annotation.Transactional;
import zw.co.netone.ussdreportsanalyser.dto.ApiResponse;
import zw.co.netone.ussdreportsanalyser.model.Subscriber;

import java.util.List;


public interface SubscriberService {


    @Transactional
    void resetSubscriber(String msisdn);

    ApiResponse<Subscriber> findByMsisdn(String msisdn);

    ApiResponse<List<Subscriber>> findAllSubscribers();

}
