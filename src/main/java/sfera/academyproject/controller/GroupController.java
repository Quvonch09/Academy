package sfera.academyproject.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import sfera.academyproject.dto.ApiResponse;
import sfera.academyproject.dto.request.ReqGroup;
import sfera.academyproject.dto.response.ResGroup;
import sfera.academyproject.dto.response.ResPageable;
import sfera.academyproject.entity.Group;
import sfera.academyproject.service.GroupService;

import java.util.List;

@RestController
@RequestMapping("/group")
@RequiredArgsConstructor
public class GroupController {
    private final GroupService groupService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Guruh qo'shish uchun",
            description = "MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY")
    public ResponseEntity<ApiResponse<String>> saveGroup(@RequestBody ReqGroup reqGroup){
        return ResponseEntity.ok(groupService.saveGroup(reqGroup));
    }

    @PutMapping("/{groupId}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Guruhni bittasini tahrirlash")
    public ResponseEntity<ApiResponse<String>> updateGroup(@PathVariable Long groupId, @RequestBody ReqGroup reqGroup){
        return ResponseEntity.ok(groupService.updateGroup(groupId, reqGroup));
    }

    @DeleteMapping("/{groupId}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Guruhni bittasini uchirish")
    public ResponseEntity<ApiResponse<String>> deleteGroup(@PathVariable Long groupId){
        return ResponseEntity.ok(groupService.deleteGroup(groupId));
    }


    @GetMapping("/{groupId}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Guruhni bittasini kurish")
    public ResponseEntity<ApiResponse<ReqGroup>> getGroup(@PathVariable Long groupId){
        return ResponseEntity.ok(groupService.getGroupById(groupId));
    }


    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Guruhni filter qilish")
    public ResponseEntity<ApiResponse<ResPageable>> getAllGroup(@RequestParam(required = false) String name,
                                                                      @RequestParam(required = false) String teacherName,
                                                                      @RequestParam(required = false) String roomName,
                                                                      @RequestParam(defaultValue = "0") int page,
                                                                      @RequestParam(defaultValue = "10") int size){
        return ResponseEntity.ok(groupService.searchGroup(name, teacherName, roomName, page, size));
    }
}
