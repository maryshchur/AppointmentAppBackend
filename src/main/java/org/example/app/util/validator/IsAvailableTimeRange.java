package org.example.app.util.validator;

import org.example.app.util.validator.AvailableTimeRangeValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({TYPE})
@Retention(RUNTIME)
@Constraint(validatedBy = AvailableTimeRangeValidator.class)
@Documented
public @interface IsAvailableTimeRange {
    String message() default "Selected teacher don't have free time in selected dates";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
    String timeFrom();
    String timeTo();
    String date();
    String teacher();
}
