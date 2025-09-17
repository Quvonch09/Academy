package sfera.academyproject.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import sfera.academyproject.dto.request.ReqGroup;
import sfera.academyproject.dto.response.ResGroup;
import sfera.academyproject.dto.response.ResStudent;
import sfera.academyproject.entity.Group;
import sfera.academyproject.repository.StudentRepository;

import java.util.List;

@Component
@RequiredArgsConstructor
public class GroupMapper {

    private final StudentRepository studentRepository;
    private final StudentMapper studentMapper;

    public ReqGroup toDto(Group group) {
        return ReqGroup.builder()
                .id(group.getId())
                .name(group.getName())
                .startTime(group.getStartTime().toString())
                .endTime(group.getEndTime().toString())
                .roomName(group.getRoom().getName())
                .teacherName(group.getTeacher().getFullName())
                .roomId(group.getRoom().getId())
                .teacherId(group.getTeacher().getId())
                .weekDays(group.getWeekDays())
                .students(studentRepository.findAllByGroup_id(group.getId()).stream()
                        .map(studentMapper::toStudentDto).toList())
                .build();
    }



    public ResGroup toDtoRes(Group group) {
        return ResGroup.builder()
                .id(group.getId())
                .name(group.getName())
                .teacherName(group.getTeacher().getFullName())
                .studentCount(studentRepository.countByGroup_Id(group.getId()))
                .build();
    }
}
