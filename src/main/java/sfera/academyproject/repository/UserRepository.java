package sfera.academyproject.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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


    @Query(value = """
    select u.* from users u where
        (:name IS NULL OR LOWER(u.full_name) LIKE LOWER(CONCAT('%',:name,'%'))) and
        (:phone IS NULL OR LOWER(u.phone) LIKE LOWER(CONCAT('%',:phone,'%'))) and
        (:role IS NULL OR u.role = :role)  and u.role <> 'ADMIN' ORDER BY u.created_at desc
    """, nativeQuery = true)
    Page<User> searchUser(@Param("name") String name,
                          @Param("phone") String phone,
                          @Param("role") String role, Pageable pageable);

    long countByRole(Role role);


}
