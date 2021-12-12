package org.example.app.entities;

import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@DynamicUpdate
@PrimaryKeyJoinColumn(name = "teacherId")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Teacher extends User {

    private String education;

    private String currentWorkPlace;

    private double yearsOfExperiences;

    private int minimalLessonDuration;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "prize_id")
    private Prize prize;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "teachers_subscribers",
            joinColumns = {@JoinColumn(name = "teacher_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "subscriber_id", referencedColumnName = "studentId")}
    )
    private Set<Student> subscribers = new HashSet<>();
}
