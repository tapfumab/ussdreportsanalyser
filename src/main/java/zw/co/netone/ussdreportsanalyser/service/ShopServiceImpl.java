package zw.co.netone.ussdreportsanalyser.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import zw.co.netone.ussdreportsanalyser.model.Shop;
import zw.co.netone.ussdreportsanalyser.repository.ShopRepository;

import java.util.List;

@Slf4j
@Service
@Transactional
public class ShopServiceImpl implements ShopService {

    private final ShopRepository shopRepository;

    public ShopServiceImpl(ShopRepository shopRepository) {
        this.shopRepository = shopRepository;
    }

    @Override
    public Shop findByIdOrThrow(Long id) throws Exception {
        return shopRepository.findById(id).orElseThrow(() -> new Exception("Shop could not be found"));
    }

    @Override
    public List<Shop> findAllActive() {
        return shopRepository.findAllByActiveStatusTrue();
    }

    @Override
    public List<Shop> findAll() {
        return shopRepository.findAll();
    }

    @Override
    public Shop save(Shop shop) throws Exception {
        Shop existingShop = shopRepository.findByOfficeId(shop.getShopId());
        if (existingShop != null) {
            throw new Exception("Shop  already exists");
        }
        return shopRepository.save(shop);
    }
}



