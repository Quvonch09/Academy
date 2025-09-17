package sfera.academyproject.dto.request;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthRegister {
    private String fullName;
    private String phoneNumber;
    private String password;

}
