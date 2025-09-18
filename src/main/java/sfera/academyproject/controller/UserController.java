package sfera.academyproject.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import sfera.academyproject.dto.ApiResponse;
import sfera.academyproject.dto.UserDTO;
import sfera.academyproject.dto.response.ResPageable;
import sfera.academyproject.dto.response.UserResponse;
import sfera.academyproject.entity.User;
import sfera.academyproject.entity.enums.Role;
import sfera.academyproject.security.CustomUserDetails;
import sfera.academyproject.service.UserService;

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
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<ResPageable>> searchUsers(@RequestParam(required = false) String name,
                                                                @RequestParam(required = false) String phone,
                                                                @RequestParam Role role,
                                                                @RequestParam(defaultValue = "0") int page,
                                                                @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(userService.searchUsers(name, phone, role, page, size));
    }
}
