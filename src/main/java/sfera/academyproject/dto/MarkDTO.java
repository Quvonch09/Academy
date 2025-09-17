package sfera.academyproject.dto;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MarkDTO {
    private Long id;
    private String studentName;
    private Long studentId;
    private String teacherName;
    private Long teacherId;
    private int homeworkScore;
    private int activityScore;
    private int attendanceScore;
    private int totalScore;
    private LocalDate date;
    private String level;
}
