package org.example.app.service;

import org.example.app.dto.BookedLessonDto;
import org.example.app.dto.FreeTimeDto;

public interface CrossTimeRangeValidationService {
    void checkIfTimeRangeSlotIsUnique(FreeTimeDto freeTimeDto, String email) throws Throwable;
    void checkIfTimeRangeSlotIsUnique(BookedLessonDto bookedLessonDto, Long studentId) throws Throwable;
}
