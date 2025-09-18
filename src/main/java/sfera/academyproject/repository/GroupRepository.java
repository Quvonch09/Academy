package sfera.academyproject.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import sfera.academyproject.entity.Group;

import java.time.LocalTime;
import java.util.List;

@Repository
public interface GroupRepository extends JpaRepository<Group, Long> {

    @Query(value = """
    SELECT CASE WHEN COUNT(*) > 0 THEN TRUE ELSE FALSE END
    FROM groups g
    JOIN groups_week_days w ON g.id = w.groups_id
    WHERE g.room_id = :roomId
      AND w.week_days IN (:weekdays)
      AND (:startTime < g.end_time AND :endTime > g.start_time)
""", nativeQuery = true)
    boolean existsByGroup(@Param("weekdays") List<String> weekdays,
                     @Param("roomId") Long roomId,
                     @Param("startTime") LocalTime startTime,
                     @Param("endTime") LocalTime endTime);


    @Query(value = """
    SELECT CASE WHEN COUNT(*) > 0 THEN TRUE ELSE FALSE END
    FROM groups g
    JOIN groups_week_days w ON g.id = w.groups_id
    WHERE g.room_id = :roomId
      AND w.week_days IN (:weekdays)
      AND (:startTime < g.end_time AND :endTime > g.start_time)
      AND g.id <> :groupId
""", nativeQuery = true)
    boolean existsByGroupForUpdate(@Param("weekdays") List<String> weekdays,
                                   @Param("roomId") Long roomId,
                                   @Param("startTime") LocalTime startTime,
                                   @Param("endTime") LocalTime endTime,
                                   @Param("groupId") Long groupId);


    @Query(value = """
           select g.* from groups g join users u on u.id = g.teacher_id join room r on g.room_id = r.id where
            (:name IS NULL OR LOWER(g.name) LIKE LOWER(CONCAT('%',:name,'%'))) and
            (:teacherName IS NULL OR LOWER(u.full_name)LIKE LOWER(CONCAT('%',:teacherName,'%'))) and
            (:roomName IS NULL OR LOWER(r.name) LIKE LOWER(CONCAT('%',:roomName,'%'))) order by g.created_at desc
    """, nativeQuery = true)
    Page<Group> searchByGroup(@Param("name") String name,
                              @Param("teacherName") String teacherName,
                              @Param("roomName") String roomName, Pageable pageable);








}
