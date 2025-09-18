package sfera.academyproject.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import sfera.academyproject.dto.response.ResStudent;
import sfera.academyproject.entity.Student;
import sfera.academyproject.repository.MarkRepository;
import sfera.academyproject.service.MarkService;

@Component
@RequiredArgsConstructor
public class StudentMapper {
    private final MarkService markService;
    private final MarkRepository markRepository;

    public ResStudent toStudentDto(Student student) {
        return ResStudent.builder()
                .id(student.getId())
                .name(student.getFullName())
                .phoneNumber(student.getPhoneNumber())
                .parentName(student.getParent().getFullName())
                .level(markService.level(markRepository.scoreByUserId(student.getId()) != null ? markRepository.scoreByUserId(student.getId()) : 0).toString())
                .score(markRepository.scoreByUserId(student.getId()) != null ? markRepository.scoreByUserId(student.getId()) : 0)
                .build();
    }
}
