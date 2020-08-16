package org.example.app.service;

import org.example.app.dto.RegisterUserDto;
import org.example.app.dto.UserDto;
import org.example.app.entities.User;

import java.util.List;

public interface UserService {
    void register(RegisterUserDto registerUserDto);

    void saveOrUpdate(User user);

    List<UserDto> getAllTeachers();

    UserDto getUserByEmail(String email);

    void createVerificationToken(RegisterUserDto user, String token);

    void confirmRegistration(String token);
}

