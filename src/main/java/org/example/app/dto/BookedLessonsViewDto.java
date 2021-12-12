package org.example.app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.app.entities.Teacher;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookedLessonsViewDto extends AvailableTeachersHoursDto {
    private boolean approved;
    private Long studentId;
}
