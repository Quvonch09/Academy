package sfera.academyproject.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import sfera.academyproject.dto.MarkDTO;
import sfera.academyproject.dto.response.ResMark;
import sfera.academyproject.entity.Mark;
import sfera.academyproject.entity.enums.Level;
import sfera.academyproject.exception.BadRequestException;
import sfera.academyproject.repository.MarkRepository;
import sfera.academyproject.service.MarkService;

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
                .score(mark.getTotalScore())
                .level(mark.getLevel().name())
                .date(mark.getDate())
                .activityScore(mark.getActivityScore())
                .attendanceScore(mark.getAttendanceScore())
                .homeworkScore(mark.getHomeworkScore())
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


    public Level level(int totalScore) {
        if (totalScore <= 10) { // 10 ballik tizim
            if (totalScore >= 8) {
                return Level.YASHIL;
            } else if (totalScore >= 5) {
                return Level.SARIQ;
            } else {
                return Level.QIZIL;
            }
        } else if (totalScore <= 100) { // 100 ballik tizim
            if (totalScore >= 80) {
                return Level.YASHIL;
            } else if (totalScore >= 50) {
                return Level.SARIQ;
            } else {
                return Level.QIZIL;
            }
        } else {
            // noto‘g‘ri qiymat kiritilgan (10 dan katta yoki 100 dan katta)
            throw new BadRequestException("Noto‘g‘ri ball kiritildi: " + totalScore);
        }
    }


}
