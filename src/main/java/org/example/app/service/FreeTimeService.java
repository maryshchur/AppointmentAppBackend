package org.example.app.service;

import org.example.app.dto.AvailableTeachersHoursDto;
import org.example.app.dto.FreeTimeDto;

import java.time.LocalDate;
import java.util.List;

public interface FreeTimeService {
    void save(FreeTimeDto freeTimeDto, String email) throws Throwable;
    List<AvailableTeachersHoursDto> getTeacherFreeTimes(Long id);
    List<FreeTimeDto> getTeacherFreeTimeByData(Long id, LocalDate date);
    void delete(Long id);
}
