package sfera.academyproject.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import sfera.academyproject.dto.ApiResponse;
import sfera.academyproject.dto.UserDTO;
import sfera.academyproject.dto.response.ResPageable;
import sfera.academyproject.dto.response.UserResponse;
import sfera.academyproject.entity.Student;
import sfera.academyproject.entity.User;
import sfera.academyproject.entity.enums.Role;
import sfera.academyproject.mapper.UserMapper;
import sfera.academyproject.repository.StudentRepository;
import sfera.academyproject.repository.UserRepository;
import sfera.academyproject.security.CustomUserDetails;
import sfera.academyproject.security.JwtService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper mapper;
    private final JwtService jwtService;
    private final StudentRepository studentRepository;
    private final UserMapper userMapper;

    public ApiResponse<UserResponse> getProfile(CustomUserDetails currentUser) {
        if ("STUDENT".equals(currentUser.getRole())) {
            Student student = studentRepository.findByPhoneNumber(currentUser.getUsername())
                    .orElseThrow(() -> new RuntimeException("Student topilmadi"));
            return ApiResponse.success(mapper.toResponseStudent(student), "Success");
        } else {
            User user = userRepository.findByPhone(currentUser.getUsername())
                    .orElseThrow(() -> new RuntimeException("User topilmadi"));
            return ApiResponse.success(mapper.toResponse(user), "Success");
        }
    }


    public ApiResponse<String> updateProfile(CustomUserDetails user, UserDTO userDTO){
        if ("STUDENT".equals(user.getRole())) {
            Student student = studentRepository.findByPhoneNumber(user.getUsername())
                    .orElseThrow(() -> new RuntimeException("Student topilmadi"));
            student.setPhoneNumber(userDTO.getPhone());
            student.setFullName(userDTO.getFullName());
            student.setImgUrl(userDTO.getImageUrl());
            Student save = studentRepository.save(student);
            if (userDTO.getPhone().equals(user.getPhone())) {
                CustomUserDetails userDetails = CustomUserDetails.fromStudent(save);
                String token = jwtService.generateToken(
                        userDetails.getUsername(),
                        userDetails.getRole()
                );
                return ApiResponse.success(token, "STUDENT");
            } else {
                return ApiResponse.success(null, "Success");
            }
        } else {
            User user1 = userRepository.findByPhone(user.getUsername())
                    .orElseThrow(() -> new RuntimeException("User topilmadi"));
            user1.setPhone(userDTO.getPhone());
            user1.setFullName(userDTO.getFullName());
            user1.setImageUrl(userDTO.getImageUrl());
            User save = userRepository.save(user1);
            if (userDTO.getPhone().equals(user.getPhone())) {
                String token = jwtService.generateToken(save.getPhone(), save.getRole().name());
                return ApiResponse.success(token, save.getRole().name());
            } else {
                return ApiResponse.success(null, "Success");
            }
        }
    }


    public ApiResponse<ResPageable> searchUsers(String name, String phone, Role role, int page, int size){
        if (role.equals(Role.STUDENT)){
            Page<Student> students = studentRepository.searchStudent(name, phone, PageRequest.of(page, size));
            if (students.getTotalElements() == 0){
                return ApiResponse.error("Studentlar topilmadi");
            }

            List<UserResponse> list = students.getContent().stream().map(userMapper::toResponseStudent).toList();

            ResPageable resPageable = ResPageable.builder()
                    .page(page)
                    .size(size)
                    .totalElements(students.getTotalElements())
                    .totalPage(students.getTotalPages())
                    .body(list)
                    .build();
            return ApiResponse.success(resPageable, null);

        } else {
            Page<User> users = userRepository.searchUser(
                    name, phone, role.name(), PageRequest.of(page, size));
            if (users.getTotalElements() == 0){
                return ApiResponse.error("Userlar topilmadi");
            }

            List<UserResponse> list = users.getContent().stream().map(userMapper::toResponse).toList();

            ResPageable resPageable = ResPageable.builder()
                    .page(page)
                    .size(size)
                    .totalElements(users.getTotalElements())
                    .totalPage(users.getTotalPages())
                    .body(list)
                    .build();
            return ApiResponse.success(resPageable, null);
        }
    }



}
