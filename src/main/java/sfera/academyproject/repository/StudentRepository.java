package sfera.academyproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
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
}
