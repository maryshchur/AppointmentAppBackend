package org.example.app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterTeacherDto extends RegisterUserDto {
    private String education;

    private String currentWorkPlace;

    private double yearsOfExperiences;

    private int minimalLessonDuration;
}
