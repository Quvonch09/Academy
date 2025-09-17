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
import sfera.academyproject.entity.enums.Role;
import sfera.academyproject.exception.DataNotFoundException;
import sfera.academyproject.mapper.MarkRowMapper;
import sfera.academyproject.repository.MarkRepository;
import sfera.academyproject.repository.StudentRepository;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MarkService {
    private final MarkRepository markRepository;
    private final StudentRepository studentRepository;
    private final MarkRowMapper markRowMapper;

    public ApiResponse<String> addMark(User teacher, ReqMark reqMark){
        Student student = studentRepository.findById(reqMark.getStudentId()).orElseThrow(
                () -> new DataNotFoundException("Student not found")
        );

        if (!student.getGroup().getTeacher().equals(teacher)){
            return ApiResponse.error("This student does not belong to you.");
        }

        int totalScore = (reqMark.getActivityScore() + reqMark.getAttendanceScore() + reqMark.getHomeworkScore()) / 3;
        Mark mark = Mark.builder()
                .student(student)
                .attendanceScore(reqMark.getAttendanceScore())
                .activityScore(reqMark.getActivityScore())
                .homeworkScore(reqMark.getHomeworkScore())
                .teacher(teacher)
                .date(LocalDate.now())
                .totalScore(totalScore)
                .level(level(totalScore))
                .build();
        markRepository.save(mark);
        return ApiResponse.success(null, "Success");
    }


    public ApiResponse<String> updateMark(Long markId,User teacher, ReqMark reqMark){
        Mark mark = markRepository.findById(markId).orElseThrow(
                () -> new DataNotFoundException("Mark not found")
        );

        if (!teacher.equals(mark.getTeacher())){
            return ApiResponse.error("This mark does not belong to you.");
        }

        mark.setAttendanceScore(reqMark.getAttendanceScore());
        mark.setActivityScore(reqMark.getActivityScore());
        mark.setHomeworkScore(reqMark.getHomeworkScore());
        mark.setTeacher(teacher);
        markRepository.save(mark);
        return ApiResponse.success(null, "Success");
    }


    public ApiResponse<String> deleteMark(User teacher,Long markId){
        Mark mark = markRepository.findById(markId).orElseThrow(
                () -> new DataNotFoundException("Mark not found")
        );

        if (!teacher.equals(mark.getTeacher())){
            return ApiResponse.error("This mark does not belong to you.");
        }

        markRepository.deleteById(markId);
        return ApiResponse.success(null, "Success");
    }


    public ApiResponse<List<ResMark>> getMyMarks(User user){
        List<Mark> marks;
        if (user.getRole().equals(Role.TEACHER)){
            marks = markRepository.findAllByTeacherId(user.getId());
        } else {
            marks = markRepository.findAllByStudentId(user.getId());
        }

        List<ResMark> list = marks.stream().map(markRowMapper::toMarkDto).toList();
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



    public Level level(int totalScore){
        if (totalScore >=8 && totalScore <=10){
            return Level.YASHIL;
        } else if (totalScore >=5 && totalScore <=7){
            return Level.SARIQ;
        } else {
            return Level.QIZIL;
        }
    }
}
