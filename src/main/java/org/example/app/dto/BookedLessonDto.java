package org.example.app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.app.entities.Teacher;
import org.example.app.entities.User;
import org.example.app.util.validator.IsAvailableTimeRange;
import org.example.app.util.validator.IsValidTimeRange;
import org.springframework.hateoas.RepresentationModel;

import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import java.sql.Time;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@IsValidTimeRange(timeFrom = "timeFrom", timeTo = "timeTo")
@IsAvailableTimeRange(timeFrom = "timeFrom", timeTo = "timeTo",date="date",teacher="teacher")
public class BookedLessonDto extends RepresentationModel<BookedLessonDto> {
    private Long id;
    @NotNull
    private Teacher teacher;
    @NotNull
    @FutureOrPresent
    private LocalDate date;
    @NotNull
    private Time timeFrom;
    @NotNull
    private Time timeTo;


    private boolean aproved;
}
