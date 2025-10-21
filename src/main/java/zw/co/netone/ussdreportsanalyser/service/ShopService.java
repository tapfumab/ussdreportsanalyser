package zw.co.netone.ussdreportsanalyser.service;


import zw.co.netone.ussdreportsanalyser.model.Shop;

import java.util.List;

public interface ShopService {

    Shop findByIdOrThrow(Long id) throws Exception;

    List<Shop> findAllActive();

    List<Shop> findAll();

    Shop save(Shop shop) throws Exception;
}
