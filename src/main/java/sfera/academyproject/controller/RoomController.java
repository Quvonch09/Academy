package sfera.academyproject.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import sfera.academyproject.dto.ApiResponse;
import sfera.academyproject.dto.RoomDTO;
import sfera.academyproject.service.RoomService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/room")
public class RoomController {
    private final RoomService roomService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    @Operation(summary = "ADMIN room qo'shish")
    public ResponseEntity<ApiResponse<String>> saveRoom(@RequestParam String name){
        return ResponseEntity.ok(roomService.addRoom(name));
    }


    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    @Operation(summary = "ADMIN roomni tahrirlash")
    public ResponseEntity<ApiResponse<String>> updateRoom(@PathVariable Long id, @RequestParam String name){
        return ResponseEntity.ok(roomService.updateRoom(id, name));
    }


    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    @Operation(summary = "ADMIN roomni uchirish")
    public ResponseEntity<ApiResponse<String>> deleteRoom(@PathVariable Long id){
        return ResponseEntity.ok(roomService.deleteRoom(id));
    }


    @GetMapping
    @Operation(summary = "Roomni hammasini kurish")
    public ResponseEntity<ApiResponse<List<RoomDTO>>> getAllRooms(){
        return ResponseEntity.ok(roomService.getAllRooms());
    }


    @GetMapping("/{id}")
    @Operation(summary = "Roomni bittasini kurish")
    public ResponseEntity<ApiResponse<RoomDTO>> getRoomById(@PathVariable Long id){
        return ResponseEntity.ok(roomService.getRoomById(id));
    }

}
