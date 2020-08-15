package org.example.app.util.validator;


import org.apache.commons.beanutils.BeanUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.sql.Time;

public class TimeValidator implements ConstraintValidator<IsValidTimeRange, Object> {

    private String timeTo;
    private String timeFrom;
    @Override
    public void initialize(IsValidTimeRange constraintAnnotation) {
        timeFrom=constraintAnnotation.timeFrom();
        timeTo=constraintAnnotation.timeTo();
    }

    @Override
    public boolean isValid(Object o, ConstraintValidatorContext constraintValidatorContext) {
        try {
            final Object firstObj = BeanUtils.getProperty(o, timeFrom);
            final Object secondObj = BeanUtils.getProperty(o, timeTo);
            return Time.valueOf(firstObj.toString()).before(Time.valueOf(secondObj.toString()));
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
