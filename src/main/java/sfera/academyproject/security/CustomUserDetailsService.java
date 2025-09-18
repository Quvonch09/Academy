package sfera.academyproject.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import sfera.academyproject.entity.Student;
import sfera.academyproject.entity.User;
import sfera.academyproject.repository.StudentRepository;
import sfera.academyproject.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    private final StudentRepository studentRepository;

    @Override
    public UserDetails loadUserByUsername(String phone) throws UsernameNotFoundException {
        // Avval User ni tekshiramiz
        User user = userRepository.findByPhone(phone).orElse(null);
        if (user != null) {
            return CustomUserDetails.fromUser(user);
        }

        // Agar User topilmasa → Student ni tekshiramiz
        Student student = studentRepository.findByPhoneNumber(phone).orElse(null);
        if (student != null) {
            return CustomUserDetails.fromStudent(student);
        }

        // Ikkalasida ham topilmasa → exception
        throw new UsernameNotFoundException("User yoki Student topilmadi: " + phone);
    }
}
