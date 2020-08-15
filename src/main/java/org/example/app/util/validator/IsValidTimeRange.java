


package org.example.app.util.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({TYPE})
@Retention(RUNTIME)
@Constraint(validatedBy = TimeValidator.class)
@Documented
public @interface IsValidTimeRange {
    String message() default "broken time validation";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
    String timeFrom();
    String timeTo();
}
