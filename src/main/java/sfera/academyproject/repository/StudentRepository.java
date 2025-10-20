package sfera.academyproject.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import sfera.academyproject.dto.DashboardDTO;
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

//    @Query(value = """
//    SELECT s.*, top_students.total_score
//    FROM student s
//    JOIN (
//        SELECT m.student_id, SUM(m.total_score) AS total_score
//        FROM mark m
//        GROUP BY m.student_id
//    ) top_students ON s.id = top_students.student_id
//    ORDER BY top_students.total_score DESC
//    LIMIT 5
//    """, nativeQuery = true)
//    List<Student> findTop5StudentsByTotalScore();

    @Query(value = """
    SELECT s.*, latest_marks.total_score
    FROM student s
    JOIN (
        SELECT m.student_id, m.total_score
        FROM mark m
        JOIN (
            SELECT student_id, MAX(created_at) AS last_mark_time
            FROM mark
            GROUP BY student_id
        ) last_marks ON m.student_id = last_marks.student_id AND m.created_at = last_marks.last_mark_time
    ) latest_marks ON s.id = latest_marks.student_id
    ORDER BY latest_marks.total_score DESC
    LIMIT 5
    """, nativeQuery = true)
    List<Student> findTop5StudentsByTotalScore();


    @Query(value = """
    select count(s.*) from student s join groups g on g.id = s.group_id join users u on u.full_name = ?1
    """, nativeQuery = true)
    long countByTeacher(String name);


    long countByGroup_Teacher_Id(Long teacherId);
//    @Query(value = """
//    SELECT
//        s.id as student_id,
//        s.full_name,
//        COALESCE(SUM(m.total_score), 0) as total_score,
//        RANK() OVER (ORDER BY SUM(m.total_score) DESC) as rank
//    FROM student s
//    LEFT JOIN mark m ON s.id = m.student_id
//    WHERE s.group_id = :groupId
//    GROUP BY s.id, s.full_name
//    ORDER BY total_score DESC
//    """, nativeQuery = true)
//    List<Map<String, Object>> getGroupLeaderboard(@Param("groupId") Long groupId);


    @Query(value = """
    SELECT 
        s.id AS student_id,
        s.full_name,
        COALESCE(m.total_score, 0) AS total_score,
        RANK() OVER (ORDER BY m.total_score DESC) AS rank
    FROM student s
    LEFT JOIN (
        SELECT m1.student_id, m1.total_score
        FROM mark m1
        JOIN (
            SELECT student_id, MAX(created_at) AS last_mark_time
            FROM mark
            GROUP BY student_id
        ) last_marks ON m1.student_id = last_marks.student_id AND m1.created_at = last_marks.last_mark_time
    ) m ON s.id = m.student_id
    WHERE s.group_id = :groupId
    ORDER BY total_score DESC
    """, nativeQuery = true)
    List<Map<String, Object>> getGroupLeaderboard(@Param("groupId") Long groupId);


//    @Query(value = """
//        SELECT s.*
//        FROM student s
//        JOIN (
//            SELECT m.student_id, SUM(m.total_score) AS total_score
//            FROM mark m WHERE m.teacher_id = ?1
//            GROUP BY m.student_id
//        ) top_students ON s.id = top_students.student_id
//        ORDER BY top_students.total_score DESC
//        LIMIT 5
//        """, nativeQuery = true)
//    List<Student> findTop5StudentsByTotalScoreByTeacher(Long teacherId);


    @Query(value = """
    SELECT s.*
    FROM student s
    JOIN (
        SELECT m.student_id, m.total_score
        FROM mark m
        JOIN (
            SELECT student_id, MAX(created_at) AS last_mark_time
            FROM mark
            WHERE teacher_id = ?1
            GROUP BY student_id
        ) last_marks ON m.student_id = last_marks.student_id AND m.created_at = last_marks.last_mark_time
        WHERE m.teacher_id = ?1
    ) latest_marks ON s.id = latest_marks.student_id
    ORDER BY latest_marks.total_score DESC
    LIMIT 5
    """, nativeQuery = true)
    List<Student> findTop5StudentsByTotalScoreByTeacher(Long teacherId);



//    @Query(value = """
//    WITH avg_mark AS (
//        SELECT m.student_id,
//               AVG(m.total_score) AS avg_score
//        FROM mark m
//        GROUP BY m.student_id
//    ),
//    classified AS (
//        SELECT student_id,
//               CASE
//                   WHEN avg_score >= 80 THEN 'YASHIL'
//                   WHEN avg_score >= 50 THEN 'SARIQ'
//                   ELSE 'QIZIL'
//               END AS level
//        FROM avg_mark
//    ),
//    total AS (
//        SELECT COUNT(*) AS total_students FROM classified
//    )
//    SELECT
//        c.level AS level,
//        COUNT(c.student_id) AS studentCount,
//        ROUND(COUNT(c.student_id) * 100.0 / t.total_students, 2) AS percentage
//    FROM classified c
//    CROSS JOIN total t
//    GROUP BY c.level, t.total_students
//    """, nativeQuery = true)
//    List<DashboardDTO> getDashboard();



    @Query(value = """
    WITH last_marks AS (
        SELECT m1.student_id, m1.total_score
        FROM mark m1
        JOIN (
            SELECT student_id, MAX(id) AS last_id
            FROM mark
            GROUP BY student_id
        ) lm ON m1.id = lm.last_id
    ),
    classified AS (
        SELECT 
            student_id,
            CASE
                WHEN total_score >= 8 THEN 'YASHIL'
                WHEN total_score >= 5 THEN 'SARIQ'
                ELSE 'QIZIL'
            END AS level
        FROM last_marks
    ),
    total AS (
        SELECT COUNT(*) AS total_students FROM classified
    )
    SELECT
        c.level AS level,
        COUNT(c.student_id) AS studentCount,
        ROUND(COUNT(c.student_id) * 100.0 / t.total_students, 2) AS percentage
    FROM classified c
    CROSS JOIN total t
    GROUP BY c.level, t.total_students
    """, nativeQuery = true)
    List<DashboardDTO> getDashboard();




}
