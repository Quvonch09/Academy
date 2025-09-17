package sfera.academyproject.repository;

import sfera.academyproject.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sfera.academyproject.entity.enums.Role;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    Optional<User> findByPhoneAndRole(String phone, Role role);
    Optional<User> findByPhone(String phone);

    boolean existsByPhoneAndRole(String phone, Role role);
    boolean existsByPhone(String phone);


}
