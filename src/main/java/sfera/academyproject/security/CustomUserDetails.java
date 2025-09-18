package sfera.academyproject.security;

import org.springframework.stereotype.Component;
import sfera.academyproject.entity.Student;
import sfera.academyproject.entity.User;
import sfera.academyproject.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Getter
public class CustomUserDetails implements UserDetails {

    private final String fullName;
    private final String phone;
    private final String password;
    private final String role;
    private final String imgUrl;
    private final boolean enabled;

    public CustomUserDetails(User user) {
        this.fullName = user.getFullName();
        this.phone = user.getPhone();
        this.password = user.getPassword();
        this.role = user.getRole().name();
        this.imgUrl = user.getImageUrl();
        this.enabled = user.isEnabled();
    }

    public CustomUserDetails(Student student) {
        this.fullName = student.getFullName();
        this.phone = student.getPhoneNumber();
        this.password = student.getPassword();
        this.role = "STUDENT"; // Student uchun default role
        this.imgUrl = student.getImgUrl();
        this.enabled = student.isDeleted();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of((GrantedAuthority) () -> "ROLE_" + role);
    }

    @Override
    public String getPassword() { return password; }

    @Override
    public String getUsername() { return phone; }

    @Override
    public boolean isAccountNonExpired() { return true; }

    @Override
    public boolean isAccountNonLocked() { return true; }

    @Override
    public boolean isCredentialsNonExpired() { return true; }

    @Override
    public boolean isEnabled() { return enabled; }

    public static CustomUserDetails fromUser(User user) {
        return new CustomUserDetails(user);
    }

    public static CustomUserDetails fromStudent(Student student) {
        return new CustomUserDetails(student);
    }
}

