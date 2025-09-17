package sfera.academyproject.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import sfera.academyproject.dto.response.ResStudent;
import sfera.academyproject.entity.enums.WeekDays;

import java.time.LocalTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReqGroup {

    @Schema(hidden = true)
    private Long id;

    private String name;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
    private String startTime;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
    private String endTime;

    private List<WeekDays> weekDays;

    private Long teacherId;

    @Schema(hidden = true)
    private String teacherName;

    private Long roomId;

    @Schema(hidden = true)
    private String roomName;

    @Schema(hidden = true)
    private List<ResStudent> students;
}
