package sfera.academyproject.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import sfera.academyproject.entity.Student;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    boolean existsByPhoneNumber(String phoneNumber);
    Optional<Student> findByPhoneNumber(String phoneNumber);

    List<Student> findAllByGroup_id(Long group_id);

    Optional<Student> findByParent_Phone(String phone);

    long countByGroup_Id(Long groupId);

    @Query(value = """
    select s.* from student s join groups g on g.id = s.group_id join users u on u.id = g.teacher_id where
    (:teacherName IS NULL OR u.full_name = :teacherName) and
    (:name IS NULL OR LOWER(s.full_name) LIKE LOWER(CONCAT('%',:name,'%'))) and
    (:phone IS null OR LOWER(s.phone_number) LIKE LOWER(CONCAT('%',:phone,'%'))) order by s.created_at desc
    """, nativeQuery = true)
    Page<Student> searchStudent(@Param("name") String name,
                                @Param("phone") String phone,
                                @Param("teacherName") String teacherName, Pageable pageable);

    @Query(value = """
        SELECT s.*
        FROM student s
        JOIN (
            SELECT m.student_id, SUM(m.total_score) AS total_score
            FROM mark m
            GROUP BY m.student_id
            ORDER BY total_score DESC
            LIMIT 5
        ) top_students ON s.id = top_students.student_id
        """, nativeQuery = true)
    List<Student> findTop5StudentsByTotalScore();

    @Query(value = """
    select count(s.*) from student s join groups g on g.id = s.group_id join users u on u.full_name = ?1
    """, nativeQuery = true)
    long countByTeacher(String name);

    @Query(value = """
    SELECT 
        s.id as student_id,
        s.full_name,
        COALESCE(SUM(m.total_score), 0) as total_score,
        RANK() OVER (ORDER BY SUM(m.total_score) DESC) as rank
    FROM student s
    LEFT JOIN mark m ON s.id = m.student_id
    WHERE s.group_id = :groupId
    GROUP BY s.id, s.full_name
    ORDER BY total_score DESC
    """, nativeQuery = true)
    List<Map<String, Object>> getGroupLeaderboard(@Param("groupId") Long groupId);


}
