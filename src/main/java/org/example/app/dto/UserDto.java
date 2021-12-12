package org.example.app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.app.constants.ValidationErrorConstants;
import org.example.app.entities.Prize;
import org.example.app.entities.Role;
import org.example.app.util.validator.EmailExist;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto  {
    private Long id;
    private String image;
    public String firstName;

    public String lastName;

    @NotBlank(message = ValidationErrorConstants.EMPTY_EMAIL)
    @Email(message = ValidationErrorConstants.INVALID_EMAIL)
    @EmailExist
    public String email;
    private boolean enabled;

    public Role role;

    private String description;

}
