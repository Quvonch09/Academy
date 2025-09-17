package sfera.academyproject.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import sfera.academyproject.dto.MarkDTO;
import sfera.academyproject.dto.response.ResMark;
import sfera.academyproject.dto.response.ResStudent;
import sfera.academyproject.entity.Mark;
import sfera.academyproject.entity.Student;
import sfera.academyproject.repository.MarkRepository;

@Component
@RequiredArgsConstructor
public class MarkRowMapper {
    private final MarkRepository markRepository;

    public ResMark toMarkDto(Mark mark) {
        return ResMark.builder()
                .id(mark.getId())
                .studentId(mark.getStudent().getId())
                .studentName(mark.getStudent().getFullName())
                .teacherId(mark.getTeacher().getId())
                .teacherName(mark.getTeacher().getFullName())
                .score(markRepository.scoreByUserId(mark.getStudent().getId()))
                .date(mark.getDate())
                .build();
    }

    public MarkDTO toDto(Mark mark){
        return MarkDTO.builder()
                .id(mark.getId())
                .studentId(mark.getStudent().getId())
                .studentName(mark.getStudent().getFullName())
                .teacherId(mark.getTeacher().getId())
                .teacherName(mark.getTeacher().getFullName())
                .homeworkScore(mark.getHomeworkScore())
                .attendanceScore(mark.getAttendanceScore())
                .activityScore(mark.getActivityScore())
                .totalScore(mark.getTotalScore())
                .date(mark.getDate())
                .level(mark.getLevel() != null ? mark.getLevel().name() : null)
                .build();
    }

}
