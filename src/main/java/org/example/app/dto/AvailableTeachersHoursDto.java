package org.example.app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AvailableTeachersHoursDto {
    private Long id;
    private String startDate;
    private String endDate;
    private String title;
}
