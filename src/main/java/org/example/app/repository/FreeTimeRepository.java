package org.example.app.repository;

import org.example.app.entities.BookedLesson;
import org.example.app.entities.FreeTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.sql.Time;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface FreeTimeRepository extends JpaRepository<FreeTime,Long> {
    @Override
    FreeTime  save(FreeTime freeTime);
    List<FreeTime> getByUserId(Long id);
    List<FreeTime> getByUserIdAndDate(Long userId, LocalDate date);
    void deleteById(Long id);
    List<FreeTime> findByUserIdAndDateAndTimeFromLessThanEqualAndTimeToGreaterThanEqual(
            Long teacherId, LocalDate date, Time timeFrom, Time timeTo);
    List<FreeTime> findByUserIdAndDateAndTimeFromEqualsOrTimeToEquals
            (Long teacherId, LocalDate date, Time timeFrom, Time timeTo);
}
