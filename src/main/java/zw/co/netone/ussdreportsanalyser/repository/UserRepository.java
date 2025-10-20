package zw.co.netone.ussdreportsanalyser.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import zw.co.netone.ussdreportsanalyser.model.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    Optional<User> findByCellNumber(String cellNumber);

    List<User> findAllByActiveStatusTrue();

    List<User> findAllByActiveStatusTrueAndRole_Id(Long roleId);
}
