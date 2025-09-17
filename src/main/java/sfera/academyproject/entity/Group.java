package sfera.academyproject.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import sfera.academyproject.entity.base.BaseEntity;
import sfera.academyproject.entity.enums.WeekDays;

import java.time.LocalTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity(name = "groups")
public class Group extends BaseEntity {

    private String name;

    @Column(name = "start_time")
    @DateTimeFormat(pattern = "HH:mm")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
    private LocalTime startTime;

    @Column(name = "end_time")
    @DateTimeFormat(pattern = "HH:mm")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
    private LocalTime endTime;

    @ElementCollection
    @Enumerated(EnumType.STRING)
    private List<WeekDays> weekDays;

    @ManyToOne
    private User teacher;

    @ManyToOne
    private Room room;

}
