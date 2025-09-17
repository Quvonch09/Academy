package sfera.academyproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sfera.academyproject.entity.Room;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {
    boolean existsByName(String name);
    boolean existsByNameAndIdNot(String name, Long id);
}
