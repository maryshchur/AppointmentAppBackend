package org.example.app.service;

import org.example.app.dto.BookedLessonDto;

import java.util.List;

public interface BookedLessonsService {
    void bookLesson(BookedLessonDto bookedLessonDto, String email);
    void approveLessonsBooking(Long lessonId);
    void declineLessonsBooking(Long lessonId);
    void cancelBookedLesson(Long lessonId, Long studentId);
    void approveCancelLessonOperation(Long lessonId);
    List<BookedLessonDto> getByStudentId(Long id);
    List<BookedLessonDto> getByTeacherId(Long id);
}
