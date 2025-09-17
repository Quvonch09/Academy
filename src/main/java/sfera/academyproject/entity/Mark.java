package sfera.academyproject.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.*;
import sfera.academyproject.entity.base.BaseEntity;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Mark extends BaseEntity {

    @ManyToOne
    private Student student;

    @ManyToOne
    private User teacher;

    private int homeworkScore;

    private int activityScore;

    private int attendanceScore;

    private int totalScore;

    private LocalDate date;
}
