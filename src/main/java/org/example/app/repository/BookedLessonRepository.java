package org.example.app.repository;

import org.example.app.entities.BookedLesson;
import org.example.app.entities.FreeTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.sql.Time;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface BookedLessonRepository extends JpaRepository<BookedLesson, Long> {
    BookedLesson save(BookedLesson bookedLesson);

    List<BookedLesson> getByStudentId(Long id);

    List<BookedLesson> getByTeacherId(Long id);

    @Override
    Optional<BookedLesson> findById(Long aLong);

    void deleteById(Long id);
    List<BookedLesson> findByStudentIdAndDate(
            Long studentId, LocalDate date);
    List<BookedLesson> findByTeacherIdAndDate(
            Long teacherId, LocalDate date);

}

