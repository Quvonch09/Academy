package sfera.academyproject.mapper;

import sfera.academyproject.dto.response.UserResponse;
import sfera.academyproject.entity.User;
import org.springframework.stereotype.Component;

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
}
