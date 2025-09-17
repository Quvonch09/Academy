package sfera.academyproject.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sfera.academyproject.dto.ApiResponse;
import sfera.academyproject.dto.UserDTO;
import sfera.academyproject.dto.response.UserResponse;
import sfera.academyproject.entity.User;
import sfera.academyproject.mapper.UserMapper;
import sfera.academyproject.repository.UserRepository;
import sfera.academyproject.security.JwtService;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper mapper;
    private final JwtService jwtService;

    public ApiResponse<UserResponse> getProfile(User user){
        return ApiResponse.success(mapper.toResponse(user), null);
    }

    public ApiResponse<String> updateProfile(User user, UserDTO userDTO){
        user.setPhone(userDTO.getPhone());
        user.setFullName(userDTO.getFullName());
        user.setImageUrl(userDTO.getImageUrl());
        User save = userRepository.save(user);
        if (userDTO.getPhone().equals(user.getPhone())) {
            String token = jwtService.generateToken(save.getPhone());
            return ApiResponse.success(token, save.getRole().name());
        } else {
            return ApiResponse.success(null, "Success");
        }
    }



}
