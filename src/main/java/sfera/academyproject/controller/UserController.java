package sfera.academyproject.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import sfera.academyproject.dto.*;
import sfera.academyproject.dto.response.ResLeaderboard;
import sfera.academyproject.dto.response.ResPageable;
import sfera.academyproject.dto.response.ResStudent;
import sfera.academyproject.dto.response.UserResponse;
import sfera.academyproject.entity.User;
import sfera.academyproject.entity.enums.Role;
import sfera.academyproject.security.CustomUserDetails;
import sfera.academyproject.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping
    @Operation(summary = "O'zini profilini kurish")
    public ResponseEntity<ApiResponse<UserResponse>> getMe(@AuthenticationPrincipal CustomUserDetails user) {
        return ResponseEntity.ok(userService.getProfile(user));
    }


    @PutMapping
    @Operation(summary = "O'zini o'zi tahrilash uchun")
    public ResponseEntity<ApiResponse<String>> update(@AuthenticationPrincipal CustomUserDetails user,
                                                      @RequestBody UserDTO userDTO) {
        return ResponseEntity.ok(userService.updateProfile(user, userDTO));
    }


    @GetMapping("/search")
    @Operation(summary = "admin userlarni barchasini kurish")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    public ResponseEntity<ApiResponse<ResPageable>> searchUsers(@AuthenticationPrincipal CustomUserDetails currentUser,
                                                                @RequestParam(required = false) String name,
                                                                @RequestParam(required = false) String phone,
                                                                @RequestParam Role role,
                                                                @RequestParam(defaultValue = "0") int page,
                                                                @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(userService.searchUsers(currentUser,name, phone, role, page, size));
    }


    @GetMapping("/admin-dashboard")
    @Operation(summary = "admin barcha malumotlarni kurishi uchun")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<AdminDTO>> adminDashboard() {
        return ResponseEntity.ok(userService.getForAdmin());
    }


    @GetMapping("/teacher-dashboard")
    @Operation(summary = "teacher barcha malumotlarni kurishi uchun")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<ApiResponse<TeacherDTO>> teacherDashboard(@AuthenticationPrincipal CustomUserDetails currentUser) {
        return ResponseEntity.ok(userService.teacherDashboard(currentUser));
    }


    @GetMapping("/topStudents")
    @Operation(summary = "admin top 5 ta studentni kurish dashboard uchun")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<ResStudent>>> topStudents() {
        return ResponseEntity.ok(userService.getTop5StudentsForAdmin());
    }


    @GetMapping("/leaderboard")
    @Operation(summary = "leaderboard")
    @PreAuthorize("hasAnyRole('STUDENT', 'PARENT')")
    public ResponseEntity<ApiResponse<List<ResLeaderboard>>> getLeaderboard(@AuthenticationPrincipal CustomUserDetails currentUser) {
        return ResponseEntity.ok(userService.getGroupLeaderboard(currentUser));
    }


    @GetMapping("/user-dashboard")
    @Operation(summary = "User uchun dashboard")
    @PreAuthorize("hasAnyRole('STUDENT', 'PARENT')")
    public ResponseEntity<ApiResponse<UserDashboardDto>> getUserDashboard(@AuthenticationPrincipal CustomUserDetails currentUser) {
        return ResponseEntity.ok(userService.userDashboard(currentUser));
    }


    @GetMapping("/topStudentsForTeacher")
    @Operation(summary = "teacher top 5 ta studentni kurish dashboard uchun")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<ApiResponse<List<ResStudent>>> topStudentsForTeacher(
            @AuthenticationPrincipal CustomUserDetails currentUser) {
        return ResponseEntity.ok(userService.getTop5StudentsForTeacher(currentUser));
    }


}
