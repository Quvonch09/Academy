package sfera.academyproject.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ManyToOne;
import lombok.*;
import sfera.academyproject.entity.base.BaseEntity;
import sfera.academyproject.entity.enums.Level;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Student extends BaseEntity {
    private String fullName;

    private String phoneNumber;

    private String password;

    @ManyToOne
    private User parent;

    @Enumerated(EnumType.STRING)
    private Level level;

    @ManyToOne
    private Group group;

}
