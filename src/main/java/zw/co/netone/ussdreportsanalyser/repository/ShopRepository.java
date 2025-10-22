package zw.co.netone.ussdreportsanalyser.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import zw.co.netone.ussdreportsanalyser.model.Shop;

import java.util.List;
@Repository
public interface ShopRepository extends JpaRepository<Shop, Long> {

    List<Shop> findAllByActive(boolean active);

    Shop findByNameIgnoreCaseAndIdNot(String name, long id);

    Shop findByShopId(String name);
}
