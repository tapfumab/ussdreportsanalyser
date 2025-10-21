package zw.co.netone.ussdreportsanalyser.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import zw.co.netone.ussdreportsanalyser.model.Shop;

import java.util.List;

public interface ShopRepository extends JpaRepository<Shop, Long> {

    List<Shop> findAllByActiveStatusTrue();

    Shop findByNameIgnoreCaseAndIdNot(String name, long id);

    Shop findByOfficeId(String name);
}
