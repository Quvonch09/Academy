package sfera.academyproject.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sfera.academyproject.dto.ApiResponse;
import sfera.academyproject.dto.RoomDTO;
import sfera.academyproject.entity.Room;
import sfera.academyproject.exception.DataNotFoundException;
import sfera.academyproject.repository.RoomRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoomService {
    private final RoomRepository roomRepository;


    public ApiResponse<String> addRoom(String name) {
        if (roomRepository.existsByName(name)) {
            return ApiResponse.error("Room already exists");
        }


        roomRepository.save(new Room(name));
        return ApiResponse.success(null, "Room added");
    }

    public ApiResponse<String> updateRoom(Long id, String name) {
        if (roomRepository.existsByNameAndIdNot(name, id)){
            return ApiResponse.error("Room already exists");
        }

        Room room = roomRepository.findById(id).orElseThrow(
                () -> new DataNotFoundException("Room not found")
        );

        room.setName(name);
        roomRepository.save(room);
        return ApiResponse.success(null, "Room updated");
    }


    public ApiResponse<String> deleteRoom(Long id) {
        Room room = roomRepository.findById(id).orElseThrow(
                () -> new DataNotFoundException("Room not found")
        );

        roomRepository.delete(room);
        return ApiResponse.success(null, "Room deleted");
    }


    public ApiResponse<List<RoomDTO>> getAllRooms(){
        List<Room> rooms = roomRepository.findAll();
        List<RoomDTO> list = rooms.stream().map(
                room -> RoomDTO.builder().id(room.getId()).name(room.getName()).build()
        ).toList();
        return ApiResponse.success(list, "Rooms");
    }


    public ApiResponse<RoomDTO> getRoomById(Long id) {
        Room room = roomRepository.findById(id).orElseThrow(
                () -> new DataNotFoundException("Room not found")
        );

        RoomDTO roomDTO = RoomDTO.builder().id(room.getId()).name(room.getName()).build();
        return ApiResponse.success(roomDTO, "Room");
    }
}
