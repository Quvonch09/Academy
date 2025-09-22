package sfera.academyproject.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import sfera.academyproject.dto.*;
import sfera.academyproject.dto.response.ResLeaderboard;
import sfera.academyproject.dto.response.ResPageable;
import sfera.academyproject.dto.response.ResStudent;
import sfera.academyproject.dto.response.UserResponse;
import sfera.academyproject.entity.Student;
import sfera.academyproject.entity.User;
import sfera.academyproject.entity.enums.Role;
import sfera.academyproject.exception.DataNotFoundException;
import sfera.academyproject.mapper.MarkRowMapper;
import sfera.academyproject.mapper.StudentMapper;
import sfera.academyproject.mapper.UserMapper;
import sfera.academyproject.repository.*;
import sfera.academyproject.security.CustomUserDetails;
import sfera.academyproject.security.JwtService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper mapper;
    private final JwtService jwtService;
    private final StudentRepository studentRepository;
    private final UserMapper userMapper;
    private final RoomRepository roomRepository;
    private final GroupRepository groupRepository;
    private final StudentMapper studentMapper;
    private final MarkRepository markRepository;
    private final MarkRowMapper markRowMapper;

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


    public ApiResponse<ResPageable> searchUsers(CustomUserDetails currentUser,String name,
                                                String phone, Role role, int page, int size){
        if (role.equals(Role.STUDENT)){
            Page<Student> students;

            if (currentUser.getRole().equals(Role.TEACHER.name())){
                students = studentRepository.searchStudent(name, phone, currentUser.getFullName(), PageRequest.of(page, size));
            } else {
                students = studentRepository.searchStudent(name, phone,null, PageRequest.of(page, size));
            }
            if (students.getTotalElements() == 0) {
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


    public ApiResponse<AdminDTO> getForAdmin(){
        long roomCount = roomRepository.count();
        long groupCount = groupRepository.count();
        long studentCount = studentRepository.count();
        long teacherCount = userRepository.countByRole(Role.TEACHER);
        long parentCount = userRepository.countByRole(Role.PARENT);
        AdminDTO adminDTO = AdminDTO.builder()
                .roomCount(roomCount)
                .groupCount(groupCount)
                .studentCount(studentCount)
                .teacherCount(teacherCount)
                .parentCount(parentCount)
                .build();
        return ApiResponse.success(adminDTO, "Admin");
    }


    public ApiResponse<List<ResStudent>> getTop5StudentsForAdmin(){
        List<ResStudent> students = studentRepository.findTop5StudentsByTotalScore()
                .stream().map(studentMapper::toStudentDto).toList();

        return ApiResponse.success(students, "Top 5 students");
    }



    public ApiResponse<TeacherDTO> teacherDashboard(CustomUserDetails currentUser){
        long l = groupRepository.countByTeacher_FullName(currentUser.getFullName());
        long l1 = studentRepository.countByTeacher(currentUser.getFullName());
        TeacherDTO teacherDTO = TeacherDTO.builder()
                .groupCount(l)
                .studentCount(l1)
                .build();
        return ApiResponse.success(teacherDTO, "Teacher");
    }


    public ApiResponse<List<ResLeaderboard>> getGroupLeaderboard(CustomUserDetails currentUser) {
        List<Map<String, Object>> rows = new ArrayList<>();
        if (currentUser.getRole().equals(Role.STUDENT.name())){
            Student student = studentRepository.findByPhoneNumber(currentUser.getPhone()).orElseThrow(
                    () -> new DataNotFoundException("Student topilmadi")
            );

            rows = studentRepository.getGroupLeaderboard(student.getGroup().getId());
        } else if (currentUser.getRole().equals(Role.PARENT.name())) {
            User parent = userRepository.findByPhone(currentUser.getPhone()).orElseThrow(
                    () -> new DataNotFoundException("Parent not found")
            );

            Student student = studentRepository.findByParent_Phone(parent.getPhone()).orElseThrow(
                    () -> new DataNotFoundException("Student not found")
            );
            rows = studentRepository.getGroupLeaderboard(student.getGroup().getId());
        }


        List<ResLeaderboard> result = rows.stream()
                .map(r -> {
                    int totalScore = ((Number) r.get("total_score")).intValue();
                    return ResLeaderboard.builder()
                            .studentId(((Number) r.get("student_id")).longValue())
                            .fullName((String) r.get("full_name"))
                            .totalScore(totalScore)
                            .level(markRowMapper.level(totalScore)) // sizning metod bilan hisoblanadi
                            .rank(((Number) r.get("rank")).intValue())
                            .build();
                })
                .toList();

        return ApiResponse.success(result, "Leaderboard");
    }



    public ApiResponse<UserDashboardDto> userDashboard(CustomUserDetails currentUser){
        if (currentUser.getRole().equals(Role.STUDENT.name())){
            Student student = studentRepository.findByPhoneNumber(currentUser.getPhone()).orElseThrow(
                    () -> new DataNotFoundException("Student topilmadi")
            );

            return ApiResponse.success(dashboardDto(student), "Success");
        } else if (currentUser.getRole().equals(Role.PARENT.name())){
            User parent = userRepository.findByPhone(currentUser.getPhone()).orElseThrow(
                    () -> new DataNotFoundException("Parent not found")
            );

            Student student = studentRepository.findByParent_Phone(parent.getPhone()).orElseThrow(
                    () -> new DataNotFoundException("Student not found")
            );
            return ApiResponse.success(dashboardDto(student), "Success");
        } else {
            return ApiResponse.error("Kirishga ruhsat yuq");
        }
    }


    public ApiResponse<List<ResStudent>> getTop5StudentsForTeacher(CustomUserDetails currentUser){
        if (currentUser.getRole().equals(Role.TEACHER.name())){
            List<ResStudent> students = studentRepository.findTop5StudentsByTotalScore()
                    .stream().map(studentMapper::toStudentDto).toList();

            return ApiResponse.success(students, "Top 5 students");
        } else {
            return ApiResponse.error("Kirishga ruhsat yuq");
        }
    }



    private UserDashboardDto dashboardDto(Student student){
        return UserDashboardDto.builder()
                .score(markRepository.scoreByUserId(student.getId()) != null ?
                        markRepository.scoreByUserId(student.getId()) : 0)
                .level(markRowMapper.level(markRepository.scoreByUserId(student.getId()) != null ?
                        markRepository.scoreByUserId(student.getId()) : 0).toString())
                .build();
    }


}
