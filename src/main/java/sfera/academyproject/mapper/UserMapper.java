package sfera.academyproject.mapper;

import lombok.RequiredArgsConstructor;
import sfera.academyproject.dto.response.UserResponse;
import sfera.academyproject.entity.Student;
import sfera.academyproject.entity.User;
import org.springframework.stereotype.Component;
import sfera.academyproject.entity.enums.Role;
import sfera.academyproject.repository.MarkRepository;

@Component
@RequiredArgsConstructor
public class UserMapper {
    private final MarkRowMapper markRowMapper;
    private final MarkRepository markRepository;
    public UserResponse toResponse(User user){
       return UserResponse.builder()
               .id(user.getId())
                .fullName(user.getFullName())
                .phone(user.getPhone())
                .imageUrl(user.getImageUrl())
                .role(user.getRole().name())
                .build();
    }


    public UserResponse toResponseStudent(Student user){
        return new UserResponse(
                user.getId(),
                user.getFullName(),
                user.getPhoneNumber(),
                user.getGroup().getName(),
                user.getParent().getFullName(),
                markRowMapper.level(markRepository.scoreByUserId(user.getId()) != null ?
                        markRepository.scoreByUserId(user.getId()) : 0).toString(),
                user.getGroup().getId(),
                user.getImgUrl(),
                Role.STUDENT.name()
        );
    }
}
