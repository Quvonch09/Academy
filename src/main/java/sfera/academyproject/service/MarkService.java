package sfera.academyproject.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import sfera.academyproject.dto.ApiResponse;
import sfera.academyproject.dto.MarkDTO;
import sfera.academyproject.dto.request.ReqMark;
import sfera.academyproject.dto.response.ResMark;
import sfera.academyproject.dto.response.ResPageable;
import sfera.academyproject.entity.Mark;
import sfera.academyproject.entity.Student;
import sfera.academyproject.entity.User;
import sfera.academyproject.entity.enums.Level;
import sfera.academyproject.exception.DataNotFoundException;
import sfera.academyproject.mapper.MarkRowMapper;
import sfera.academyproject.repository.MarkRepository;
import sfera.academyproject.repository.StudentRepository;
import sfera.academyproject.repository.UserRepository;
import sfera.academyproject.security.CustomUserDetails;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MarkService {
    private final MarkRepository markRepository;
    private final StudentRepository studentRepository;
    private final MarkRowMapper markRowMapper;
    private final UserRepository userRepository;

    public ApiResponse<String> addMark(CustomUserDetails currentUser, ReqMark reqMark) {
        if (!"TEACHER".equals(currentUser.getRole())) {
            return ApiResponse.error("Only teachers can add marks");
        }

        User teacher = userRepository.findByPhone(currentUser.getUsername())
                .orElseThrow(() -> new DataNotFoundException("Teacher not found"));

        Student student = studentRepository.findById(reqMark.getStudentId())
                .orElseThrow(() -> new DataNotFoundException("Student not found"));

        if (!student.getGroup().getTeacher().equals(teacher)) {
            return ApiResponse.error("This student does not belong to you.");
        }

        int totalScore = (reqMark.getActivityScore() +
                reqMark.getAttendanceScore() +
                reqMark.getHomeworkScore()) / 3;

        Mark mark = Mark.builder()
                .student(student)
                .attendanceScore(reqMark.getAttendanceScore())
                .activityScore(reqMark.getActivityScore())
                .homeworkScore(reqMark.getHomeworkScore())
                .teacher(teacher)
                .date(LocalDate.now())
                .totalScore(totalScore)
                .level(markRowMapper.level(totalScore))
                .build();

        markRepository.save(mark);
        return ApiResponse.success(null, "Success");
    }


    public ApiResponse<String> updateMark(Long markId, CustomUserDetails currentUser, ReqMark reqMark) {
        if (!"TEACHER".equals(currentUser.getRole())) {
            return ApiResponse.error("Only teachers can update marks");
        }

        User teacher = userRepository.findByPhone(currentUser.getUsername())
                .orElseThrow(() -> new DataNotFoundException("Teacher not found"));

        Mark mark = markRepository.findById(markId)
                .orElseThrow(() -> new DataNotFoundException("Mark not found"));

        if (!teacher.equals(mark.getTeacher())) {
            return ApiResponse.error("This mark does not belong to you.");
        }

        int totalScore = (reqMark.getActivityScore() +
                reqMark.getAttendanceScore() +
                reqMark.getHomeworkScore()) / 3;


        mark.setAttendanceScore(reqMark.getAttendanceScore());
        mark.setActivityScore(reqMark.getActivityScore());
        mark.setHomeworkScore(reqMark.getHomeworkScore());
        mark.setTotalScore(totalScore);
        mark.setTeacher(teacher);

        markRepository.save(mark);
        return ApiResponse.success(null, "Success");
    }


    public ApiResponse<String> deleteMark(CustomUserDetails currentUser, Long markId) {
        if (!"TEACHER".equals(currentUser.getRole())) {
            return ApiResponse.error("Only teachers can delete marks");
        }

        User teacher = userRepository.findByPhone(currentUser.getUsername())
                .orElseThrow(() -> new DataNotFoundException("Teacher not found"));

        Mark mark = markRepository.findById(markId)
                .orElseThrow(() -> new DataNotFoundException("Mark not found"));

        if (!teacher.equals(mark.getTeacher())) {
            return ApiResponse.error("This mark does not belong to you.");
        }

        markRepository.deleteById(markId);
        return ApiResponse.success(null, "Success");
    }

    public ApiResponse<List<ResMark>> getMyMarks(CustomUserDetails currentUser) {
        List<Mark> marks;

        if ("TEACHER".equals(currentUser.getRole())) {
            User teacher = userRepository.findByPhone(currentUser.getUsername())
                    .orElseThrow(() -> new DataNotFoundException("Teacher not found"));
            marks = markRepository.findAllByTeacherId(teacher.getId());
        } else if ("PARENT".equals(currentUser.getRole())) {
            User parent = userRepository.findByPhone(currentUser.getPhone()).orElseThrow(
                    () -> new DataNotFoundException("Parent not found")
            );

            Student student = studentRepository.findByParent_Phone(parent.getPhone()).orElseThrow(
                    () -> new DataNotFoundException("Student not found")
            );

            marks = markRepository.findAllByStudentId(student.getId());
        } else {
            Student student = studentRepository.findByPhoneNumber(currentUser.getUsername())
                    .orElseThrow(() -> new DataNotFoundException("Student not found"));
            marks = markRepository.findAllByStudentId(student.getId());
        }

        List<ResMark> list = marks.stream()
                .map(markRowMapper::toMarkDto)
                .toList();

        return ApiResponse.success(list, "Success");
    }




    public ApiResponse<ResPageable> getAllMarks(String studentName, String teacherName, int page, int size){
        Page<Mark> marks = markRepository.searchByMark(studentName, teacherName, PageRequest.of(page, size));
        if (marks.getTotalElements() == 0){
            return ApiResponse.error("No marks found.");
        }

        List<ResMark> list = marks.stream().map(markRowMapper::toMarkDto).toList();
        ResPageable resPageable = ResPageable.builder()
                .page(page)
                .size(size)
                .totalElements(marks.getTotalElements())
                .totalPage(marks.getTotalPages())
                .body(list)
                .build();
        return ApiResponse.success(resPageable, "Success");
    }



    public ApiResponse<MarkDTO> getOneMark(Long id){
        Mark mark = markRepository.findById(id).orElseThrow(
                () -> new DataNotFoundException("Mark not found")
        );

        return ApiResponse.success(markRowMapper.toDto(mark), "Success");
    }
}
