package sfera.academyproject.mapper;

import sfera.academyproject.dto.response.UserResponse;
import sfera.academyproject.entity.Student;
import sfera.academyproject.entity.User;
import org.springframework.stereotype.Component;
import sfera.academyproject.entity.enums.Role;

@Component
public class UserMapper {
    public UserResponse toResponse(User user){
        return new UserResponse(
                user.getFullName(),
                user.getPhone(),
                user.getImageUrl(),
                user.getRole().name()
        );
    }


    public UserResponse toResponseStudent(Student user){
        return new UserResponse(
                user.getFullName(),
                user.getPhoneNumber(),
                user.getImgUrl(),
                Role.STUDENT.name()
        );
    }
}
