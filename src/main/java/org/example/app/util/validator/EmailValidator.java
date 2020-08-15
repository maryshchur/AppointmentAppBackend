package org.example.app.util.validator;

import org.example.app.repository.UserRepository;
import org.example.app.util.validator.EmailExist;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class EmailValidator implements ConstraintValidator<EmailExist, String> {

    private final UserRepository userRepository;

    @Autowired
    public EmailValidator(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public boolean isValid(String email, ConstraintValidatorContext context) {
        return userRepository.existsUserByEmail(email.trim().toLowerCase()) ? false : true;
    }
}
