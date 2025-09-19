package sfera.academyproject.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AdminDTO {
    private long roomCount;
    private long groupCount;
    private long studentCount;
    private long teacherCount;
    private long parentCount;
}
