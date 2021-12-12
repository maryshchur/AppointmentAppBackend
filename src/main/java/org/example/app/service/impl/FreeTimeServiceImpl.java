package org.example.app.service.impl;

import org.example.app.dto.AvailableTeachersHoursDto;
import org.example.app.dto.FreeTimeDto;
import org.example.app.entities.FreeTime;
import org.example.app.entities.User;
import org.example.app.exeptions.NotFoundException;
import org.example.app.repository.FreeTimeRepository;
import org.example.app.service.FreeTimeService;
import org.example.app.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FreeTimeServiceImpl implements FreeTimeService {
    private FreeTimeRepository freeTimeRepository;
    private UserService userService;
    private final ModelMapper modelMapper;

    @Autowired
    public FreeTimeServiceImpl(FreeTimeRepository freeTimeRepository, UserService userService, ModelMapper modelMapper) {
        this.freeTimeRepository = freeTimeRepository;
        this.userService = userService;
        this.modelMapper = modelMapper;
    }

    //@PreAuthorize("hasRole('ROLE_TEACHER')")
    public void save(FreeTimeDto freeTimeDto, String email) throws Throwable {
        FreeTime freeTime = modelMapper.map(freeTimeDto, FreeTime.class);
        freeTime.setTeacher(modelMapper.map(userService.getUserByEmail(email), User.class));
        freeTimeRepository.save(freeTime);
    }

    @Secured({"ROLE_TEACHER", "ROLE_STUDENT"})
    public List<AvailableTeachersHoursDto> getTeacherFreeTimes(Long teacherId) {
        List<AvailableTeachersHoursDto> freeTimeDtos = freeTimeRepository.getByTeacherId(teacherId).stream().
                sorted(Comparator.comparing(FreeTime::getDate)).
                map(x -> modelMapper.map(x, AvailableTeachersHoursDto.class)).
                collect(Collectors.toList());
        if (freeTimeDtos.isEmpty()) {
            throw new NotFoundException(String.format("Teacher with id %s has no free hours", teacherId));
        }
        return freeTimeDtos;
    }

    public List<FreeTimeDto> getTeacherFreeTimeByData(Long id, LocalDate date) {
        return freeTimeRepository.getByTeacherIdAndDate(id, date).stream().
                map(x -> modelMapper.map(x, FreeTimeDto.class)).
                collect(Collectors.toList());
    }

    @Override
    public void delete(Long id) {
        freeTimeRepository.deleteById(id);
    }


}

