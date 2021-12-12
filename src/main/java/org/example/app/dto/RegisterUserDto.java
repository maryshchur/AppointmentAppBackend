package org.example.app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.app.constants.ValidationErrorConstants;
import org.example.app.entities.Role;
import org.example.app.util.validator.EmailExist;
import org.example.app.util.validator.PasswordMatch;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
//@PasswordMatch(password = "password",confPassword = "confPassword")
public class RegisterUserDto {

    @NotBlank(message = ValidationErrorConstants.EMPTY_FIRSTNAME)
    @Size(min = 3, max = 20)
    public String firstName;

    @NotBlank(message = ValidationErrorConstants.EMPTY_LASTNAME)
    @Size(min = 3, max = 30)
    public String lastName;

    @NotBlank(message = ValidationErrorConstants.EMPTY_EMAIL)
    @Email(message = ValidationErrorConstants.INVALID_EMAIL)
    @EmailExist
    public String email;

    @Size(min = 6, max = 30)
    @NotBlank

    private String password;
//    @NotBlank
//    private String confPassword;

//    @NotNull
//    public Role role;

}
