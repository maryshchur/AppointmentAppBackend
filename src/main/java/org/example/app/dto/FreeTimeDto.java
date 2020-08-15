package org.example.app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.app.util.validator.IsValidTimeRange;

import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import java.sql.Time;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@IsValidTimeRange(timeFrom = "timeFrom", timeTo = "timeTo")
public class FreeTimeDto {

    private Long id;
    @NotNull
    @FutureOrPresent
    private LocalDate date;
    @NotNull(message ="TimeTo field may not be null")
    private Time timeFrom;
    @NotNull(message ="TimeTo field may not be null")
    private Time timeTo;

    public FreeTimeDto(LocalDate date, Time timeFrom, Time timeTo) {
        this.date = date;
        this.timeFrom = timeFrom;
        this.timeTo = timeTo;
    }
}
