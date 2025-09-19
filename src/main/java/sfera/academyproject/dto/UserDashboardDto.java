package sfera.academyproject.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDashboardDto {
    private String level;
    private int score;
}
