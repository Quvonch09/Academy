package sfera.academyproject.dto.response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResGroup {
    private Long id;
    private String name;
    private String teacherName;
    private Long studentCount;
}
