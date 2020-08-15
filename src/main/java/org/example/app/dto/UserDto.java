package org.example.app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.app.constants.ValidationErrorConstants;
import org.example.app.entities.Prize;
import org.example.app.entities.Role;
import org.example.app.util.validator.EmailExist;

import javax.validation.constraints.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {

    private Long id;

    @NotBlank(message = ValidationErrorConstants.EMPTY_FIRSTNAME)
    private String firstName;

    @NotBlank(message = ValidationErrorConstants.EMPTY_LASTNAME)
    private String lastName;

    @NotBlank(message = ValidationErrorConstants.EMPTY_EMAIL)
    @Email(message = ValidationErrorConstants.INVALID_EMAIL)
    @EmailExist
    private String email;

    private boolean enabled;

    //@NotNull(message = ValidationErrorConstants.EMPTY_PASSWORD)
    @Size(min = 6, max = 30)
    @NotBlank

    private String password;

    @NotNull
    private Role role;

    private Prize prize;
}
