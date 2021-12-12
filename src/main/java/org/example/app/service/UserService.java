package org.example.app.service;

import org.example.app.dto.RegisterTeacherDto;
import org.example.app.dto.RegisterUserDto;
import org.example.app.dto.TeacherDto;
import org.example.app.dto.UserDto;
import org.example.app.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UserService {
    Long register(RegisterUserDto registerUserDto);

    Long registerAsTeacher(RegisterTeacherDto registerUserDto);

    void saveOrUpdate(User user);

    void changePhoto(MultipartFile multipartFile, Long id) throws Throwable;

    void subscribe(String studentEmail,Long teacherId) throws Throwable;

    <T extends User> void deletePhoto(String email) throws Throwable;

    <T extends UserDto> T getUserData(Long id);

    Page<TeacherDto> getAllTeachers(Integer page, Integer pageSize);

    UserDto getUserByEmail(String email) throws Throwable;

    void createVerificationToken(RegisterUserDto user, String token) throws Throwable;

    void confirmRegistration(String token) throws Throwable;

    String getRole(Long id);
}

