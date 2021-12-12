package org.example.app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TeacherDto extends UserDto {
    private String education;

    private String currentWorkPlace;

    private double yearsOfExperiences;

    private int minimalLessonDuration;

    private PrizeInfoDto prize;

//    private List<UserDto> subscribers;

}
