package sfera.academyproject.dto.response;

import lombok.*;
import sfera.academyproject.entity.enums.Level;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResLeaderboard {
    private Long studentId;
    private String fullName;
    private int totalScore;
    private Level level; // Enum: YASHIL, SARIQ, QIZIL
    private int rank;
}
