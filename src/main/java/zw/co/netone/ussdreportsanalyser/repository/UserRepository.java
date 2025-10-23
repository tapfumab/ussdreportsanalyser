package zw.co.netone.ussdreportsanalyser.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import zw.co.netone.ussdreportsanalyser.model.Shop;
import zw.co.netone.ussdreportsanalyser.model.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    Optional<User> findByUsernameAndShop(String username, Shop shop);

    Optional<User> findByEmail(String email);

    Optional<User> findByCellNumber(String cellNumber);

    List<User> findAllByActiveStatusTrue();

}
