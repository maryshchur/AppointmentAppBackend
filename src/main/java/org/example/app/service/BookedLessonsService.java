package org.example.app.service;

import org.example.app.dto.BookedLessonDto;
import org.example.app.dto.BookedLessonsViewDto;

import java.util.List;

public interface BookedLessonsService {
    void bookLesson(BookedLessonDto bookedLessonDto, String email) throws Throwable;
    void approveLessonsBooking(Long lessonId) throws Throwable;
    void declineLessonsBooking(Long lessonId);
    void cancelBookedLesson(Long lessonId, Long studentId) throws Throwable;
    void approveCancelLessonOperation(Long lessonId) throws Throwable;
//    int getFullPrizeForBookedLesson(Long lessonId, Long studentId);
    List<BookedLessonsViewDto> getByStudentId(Long id);
    List<BookedLessonsViewDto> getLessonsByTeacherId(Long id,boolean isApproved);
}
