package sfera.academyproject.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import sfera.academyproject.entity.Student;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    boolean existsByPhoneNumber(String phoneNumber);
    Optional<Student> findByPhoneNumber(String phoneNumber);

    List<Student> findAllByGroup_id(Long group_id);

    long countByGroup_Id(Long groupId);

    @Query(value = """
    select s.* from student s where
    (:name IS NULL OR LOWER(s.full_name) LIKE LOWER(CONCAT('%',:name,'%'))) and
    (:phone IS null OR LOWER(s.phone_number) LIKE LOWER(CONCAT('%',:phone,'%'))) order by s.created_at desc
    """, nativeQuery = true)
    Page<Student> searchStudent(@Param("name") String name,
                                @Param("phone") String phone, Pageable pageable);
}
