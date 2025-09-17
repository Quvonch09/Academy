package sfera.academyproject.dto.request;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReqMark {
    private Long studentId;
    private int homeworkScore;
    private int activityScore;
    private int attendanceScore;
}
