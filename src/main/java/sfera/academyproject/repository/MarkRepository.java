package sfera.academyproject.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import sfera.academyproject.entity.Mark;

import java.util.List;

@Repository
public interface MarkRepository extends JpaRepository<Mark, Long> {

    @Query(value = """
        select avg(total_score) from mark where student_id = ?1
    """, nativeQuery = true)
    Integer scoreByUserId(Long userId);

    List<Mark> findAllByTeacherId(Long teacherId);
    List<Mark> findAllByStudentId(Long studentId);

    @Query(value = """
    select m.* from mark m  join users u on u.id = m.teacher_id join student s on s.id = m.student_id where
    (:studentName IS NULL OR LOWER(s.full_name) LIKE  LOWER(CONCAT('%', :studentName, '%'))) and
    (:teacherName IS NULL OR LOWER(u.full_name) LIKE  LOWER(CONCAT('%', :teacherName, '%'))) order by m.created_at desc
    """, nativeQuery = true)
    Page<Mark> searchByMark(@Param("studentName") String studentName,
                            @Param("teacherName") String teacherName,
                            Pageable pageable);
}
