package org.example.app.util.validator;

import org.example.app.dto.UserDto;
import org.example.app.entities.User;
import org.example.app.repository.BookedLessonRepository;
import org.example.app.repository.FreeTimeRepository;
import org.example.app.service.UserService;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.sql.Time;
import java.time.LocalDate;

public class AvailableTimeRangeValidator implements ConstraintValidator<IsAvailableTimeRange, Object> {
    private String timeTo;
    private String timeFrom;
    private String date;
    private String teacher;
    private FreeTimeRepository freeTimeRepository;
    private UserService userService;

    @Autowired
    public AvailableTimeRangeValidator(FreeTimeRepository freeTimeRepository,UserService userService) {
        this.freeTimeRepository = freeTimeRepository;
        this.userService=userService;
    }

    @Override
    public void initialize(IsAvailableTimeRange isAvailableTimeRange) {
        timeFrom = isAvailableTimeRange.timeFrom();
        timeTo = isAvailableTimeRange.timeTo();
        date = isAvailableTimeRange.date();
        teacher = isAvailableTimeRange.teacher();
    }

    @Override
    public boolean isValid(Object o, ConstraintValidatorContext constraintValidatorContext) {
        try {
            final Object firstObj = new BeanWrapperImpl(o).getPropertyValue( timeFrom);
            final Object secondObj = new BeanWrapperImpl(o).getPropertyValue(timeTo);
            final Object thirdObj = new BeanWrapperImpl(o).getPropertyValue(date);
            final Object fourObj = new BeanWrapperImpl(o).getPropertyValue(teacher);
            User u=(User) fourObj;
            UserDto user = userService.getUserByEmail(u.getEmail());
            return !(freeTimeRepository.
                    findByUserIdAndDateAndTimeFromLessThanEqualAndTimeToGreaterThanEqual
                            (user.getId(), LocalDate.parse(thirdObj.toString()),
                                    Time.valueOf(firstObj.toString()),
                                    Time.valueOf(secondObj.toString())).isEmpty());
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
