package sfera.academyproject.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import sfera.academyproject.dto.ApiResponse;
import sfera.academyproject.dto.request.AuthRegister;
import sfera.academyproject.dto.request.ReqStudent;
import sfera.academyproject.entity.enums.Role;
import sfera.academyproject.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@CrossOrigin
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<String>> adminLogin(
            @RequestParam String phone,
            @RequestParam String password
    ){
        return ResponseEntity.ok(authService.login(phone, password));
    }


    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/saveUser")
    public ResponseEntity<ApiResponse<String>> userLogin(
            @RequestParam Role role,
            @RequestBody AuthRegister register
    ){
        return ResponseEntity.ok(authService.saveUser(register, role));
    }


    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/saveStudent")
    public ResponseEntity<ApiResponse<String>> studentLogin(@RequestBody ReqStudent reqStudent){
        return ResponseEntity.ok(authService.saveStudent(reqStudent));
    }
}
