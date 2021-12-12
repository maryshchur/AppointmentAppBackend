package org.example.app.service.impl;

import org.example.app.dto.BookedLessonDto;
import org.example.app.dto.FreeTimeDto;
import org.example.app.entities.BookedLesson;
import org.example.app.exeptions.BrokenTimeRangeException;
import org.example.app.repository.BookedLessonRepository;
import org.example.app.service.CrossTimeRangeValidationService;
import org.example.app.service.FreeTimeService;
import org.example.app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.sql.Time;
import java.util.List;

@Service
public class CrossTimeRangeValidationServiceImpl implements
        CrossTimeRangeValidationService {
    private FreeTimeService freeTimeService;
    private UserService userService;
    private BookedLessonRepository bookedLessonRepository;

    @Autowired
    public CrossTimeRangeValidationServiceImpl(FreeTimeService freeTimeService,
                                               UserService userService, BookedLessonRepository bookedLessonRepository) {
        this.freeTimeService = freeTimeService;
        this.userService = userService;
        this.bookedLessonRepository = bookedLessonRepository;
    }

    /**
     * Method finds all {@link FreeTimeDto} by provided user id and date.
     *
     * @param email
     * @param freeTimeDto {@link FreeTimeDto}
     * @return list of all {@link FreeTimeDto}
     */
    @PreAuthorize("hasRole('ROLE_TEACHER')")
    @Override
    public void checkIfTimeRangeSlotIsUnique(FreeTimeDto freeTimeDto, String email) throws Throwable {
        List<FreeTimeDto> allFreeDates = getAllTeachersFreeHoursOnSelectedDay(email,
                freeTimeDto);
        if ((!allFreeDates.isEmpty()) && allFreeDates.stream().anyMatch(h -> isNotUnique(freeTimeDto.getTimeFrom(),
                freeTimeDto.getTimeTo(), h.getTimeFrom(), h.getTimeTo()))) {
            throw new BrokenTimeRangeException("Time range is crossed with another one");
        }
    }

    @PreAuthorize("hasRole('ROLE_STUDENT')")
    @Override
    public void checkIfTimeRangeSlotIsUnique(BookedLessonDto bookedLessonDto, Long studentId) throws Throwable {
        List<BookedLesson> allBookedLessonsInStudent = bookedLessonRepository.
                findByStudentIdAndDate(studentId,
                        bookedLessonDto.getDate());
        checkTimeRange(allBookedLessonsInStudent, bookedLessonDto, "You have already booked lessons on selected time range");
        List<BookedLesson> allTeacherLessonRequests = bookedLessonRepository.
                findByTeacherIdAndDate(userService.getUserByEmail(bookedLessonDto.getTeacher().getEmail()).getId(),
                        bookedLessonDto.getDate());
        checkTimeRange(allTeacherLessonRequests, bookedLessonDto, "Another student have already send request for this hours");

    }

    private void checkTimeRange(List<BookedLesson> allFreeDates, BookedLessonDto bookedLessonDto, String message) {
        if ((!allFreeDates.isEmpty()) && allFreeDates.stream().anyMatch(h -> isNotUnique(bookedLessonDto.getTimeFrom(),
                bookedLessonDto.getTimeTo(), h.getTimeFrom(), h.getTimeTo()))) {
            throw new BrokenTimeRangeException(message);
        }
    }

    private boolean isNotUnique(Time newTimeFrom, Time newTimeTo, Time existTimeFrom, Time existTimeTo) {
        return (newTimeFrom.before(existTimeFrom) &&
                newTimeTo.after(existTimeFrom))
                ||
                (newTimeFrom.before(existTimeTo)
                        && newTimeTo.after(existTimeTo))
                || (newTimeFrom.after(existTimeFrom) &&
                newTimeTo.before(existTimeTo)) ||
                (newTimeFrom.equals(existTimeFrom)) ||
                (newTimeTo.equals(existTimeTo));
    }

    /**
     * Method finds all {@link FreeTimeDto} by provided user id and date.
     *
     * @param email
     * @param freeTimeDto {@link FreeTimeDto}
     * @return list of all {@link FreeTimeDto}
     */
    private List<FreeTimeDto> getAllTeachersFreeHoursOnSelectedDay(String email, FreeTimeDto freeTimeDto) throws Throwable {
        return freeTimeService.getTeacherFreeTimeByData(userService.getUserByEmail(email).getId(),
                freeTimeDto.getDate());
    }
}
