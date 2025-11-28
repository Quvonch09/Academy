package sfera.academyproject.dto.response;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResMark {
    private Long id;
    private Long studentId;
    private String studentName;
    private Long teacherId;
    private String teacherName;
    private Integer score;
    private Integer homeworkScore;
    private Integer activityScore;
    private Integer attendanceScore;
    private String level;
    private LocalDate date;
}
