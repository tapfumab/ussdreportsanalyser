package zw.co.netone.ussdreportsanalyser.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import zw.co.netone.ussdreportsanalyser.model.Role;


public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByName(String name);
}
