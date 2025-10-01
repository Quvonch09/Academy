package sfera.academyproject.dto.response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResStudent {
    private Long id;
    private String name;
    private String phoneNumber;
    private String parentName;
    private String groupName;
    private String teacherName;
    private String level;
    private Integer score;
}
