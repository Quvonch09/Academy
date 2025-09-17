package sfera.academyproject.component;

import sfera.academyproject.entity.User;
import sfera.academyproject.entity.enums.Role;
import sfera.academyproject.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {
    private final UserRepository userRepository;
    private final PasswordEncoder encoder;

    @Value("${spring.jpa.hibernate.ddl-auto}")
    private String ddl;

    @Override
    public void run(String... args) throws Exception {
        if (ddl.equals("create")){
            User admin = User.builder()
                    .phone("998900000000")
                    .password(encoder.encode("admin123"))
                    .role(Role.ADMIN)
                    .fullName("Admin Admin")
                    .build();

            userRepository.save(admin);
        }
    }
}
