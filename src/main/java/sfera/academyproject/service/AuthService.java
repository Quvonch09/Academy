package sfera.academyproject.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import sfera.academyproject.dto.ApiResponse;
import sfera.academyproject.dto.request.AuthRegister;
import sfera.academyproject.dto.request.ReqStudent;
import sfera.academyproject.entity.Group;
import sfera.academyproject.entity.Student;
import sfera.academyproject.entity.User;
import sfera.academyproject.entity.enums.Role;
import sfera.academyproject.exception.DataNotFoundException;
import sfera.academyproject.repository.GroupRepository;
import sfera.academyproject.repository.StudentRepository;
import sfera.academyproject.repository.UserRepository;
import sfera.academyproject.security.CustomUserDetails;
import sfera.academyproject.security.JwtService;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final GroupRepository groupRepository;
    private final StudentRepository studentRepository;

    public ApiResponse<String> login(String phone, String password) {
        Optional<User> optionalUser = userRepository.findByPhone(phone);

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            if (!passwordEncoder.matches(password, user.getPassword())) {
                return ApiResponse.error("Invalid credentials");
            }

            CustomUserDetails userDetails = CustomUserDetails.fromUser(user);
            String token = jwtService.generateToken(
                    userDetails.getUsername(),
                    userDetails.getRole()
            );

            return ApiResponse.success(token, userDetails.getRole());
        }

        Optional<Student> optionalStudent = studentRepository.findByPhoneNumber(phone);

        if (optionalStudent.isPresent()) {
            Student student = optionalStudent.get();
            if (!passwordEncoder.matches(password, student.getPassword())) {
                return ApiResponse.error("Invalid credentials");
            }

            CustomUserDetails userDetails = CustomUserDetails.fromStudent(student);
            String token = jwtService.generateToken(
                    userDetails.getUsername(),
                    userDetails.getRole()
            );

            return ApiResponse.success(token, userDetails.getRole());
        }

        return ApiResponse.error("User topilmadi");
    }






    public ApiResponse<String> saveUser(AuthRegister authRegister, Role role){

        boolean b = userRepository.existsByPhoneAndRole(authRegister.getPhoneNumber(), role);
        if (b){
            return ApiResponse.error("Teacher already exists");
        }

        User teacher = User.builder()
                .phone(authRegister.getPhoneNumber())
                .fullName(authRegister.getFullName())
                .password(passwordEncoder.encode(authRegister.getPassword()))
                .role(role)
                .build();
        userRepository.save(teacher);
        return ApiResponse.success(null, "Successfully added user");
    }


    public ApiResponse<String> saveStudent(ReqStudent reqStudent){

        boolean b = studentRepository.existsByPhoneNumber(reqStudent.getPhone());
        boolean b1 = userRepository.existsByPhone(reqStudent.getPhone());

        if (b || b1){
            return ApiResponse.error("User already exists");
        }

        User parent = userRepository.findByPhoneAndRole(reqStudent.getParentPhone(), Role.PARENT).orElseThrow(
                () -> new DataNotFoundException("Parent not found")
        );

        Group group = groupRepository.findById(reqStudent.getGroupId()).orElseThrow(
                () -> new DataNotFoundException("Group not found")
        );

        Student student = Student.builder()
                .fullName(reqStudent.getFullName())
                .parent(parent)
                .phoneNumber(reqStudent.getPhone())
                .password(passwordEncoder.encode(reqStudent.getPassword()))
                .group(group)
                .imgUrl(reqStudent.getImgUrl())
                .build();
        studentRepository.save(student);
        return ApiResponse.success(null, "Successfully saved student");
    }
}
