package org.example.app.service;

import org.example.app.dto.UserDto;
import org.example.app.entities.User;

import java.util.List;

public interface UserService {
    void register(UserDto userDto);

    void saveOrUpdate(User user);

    List<UserDto> getAllTeachers();

    UserDto getUserByEmail(String email);

    void createVerificationToken(UserDto user, String token);

    void confirmRegistration(String token);
}

