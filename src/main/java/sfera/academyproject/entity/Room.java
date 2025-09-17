package sfera.academyproject.entity;

import jakarta.persistence.Entity;
import lombok.*;
import sfera.academyproject.entity.base.BaseEntity;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Room extends BaseEntity {
    private String name;
}
