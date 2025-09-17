package sfera.academyproject.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import sfera.academyproject.dto.response.ResStudent;
import sfera.academyproject.entity.Student;

@Component
@RequiredArgsConstructor
public class StudentMapper {
    public ResStudent toStudentDto(Student student) {
        return ResStudent.builder()
                .id(student.getId())
                .name(student.getFullName())
                .phoneNumber(student.getPhoneNumber())
                .parentName(student.getParent().getFullName())
                .level(student.getLevel() != null ? student.getLevel().name() : null)
                .build();
    }
}
