package org.example.app.util.validator;

import org.springframework.beans.BeanWrapperImpl;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PasswordMatchValidator implements ConstraintValidator<PasswordMatch, Object> {
    private String password;
    private String confPassword;

    @Override
    public void initialize(PasswordMatch constraintAnnotation) {
        password = constraintAnnotation.password();
        confPassword = constraintAnnotation.confPassword();
    }

    @Override
    public boolean isValid(Object o, ConstraintValidatorContext constraintValidatorContext) {
        final Object firstObj = new BeanWrapperImpl(o).getPropertyValue(password);
        final Object secondObj = new BeanWrapperImpl(o).getPropertyValue(confPassword);
        return firstObj.equals(secondObj);
    }
}
