package org.example.app.service.impl;

import org.example.app.dto.UserDto;
import org.example.app.entities.User;
import org.example.app.entities.VerificationToken;
import org.example.app.exeptions.InvalidTokenException;
import org.example.app.exeptions.NotFoundException;
import org.example.app.exeptions.NotSavedException;
import org.example.app.repository.UserRepository;
import org.example.app.repository.VerificationTokenRepository;
import org.example.app.service.UserService;
import org.example.app.util.EmailSender;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private VerificationTokenRepository verificationTokenRepository;
    private ModelMapper modelMapper = new ModelMapper();
    private final EmailSender emailSender;

    @Autowired
    public UserServiceImpl(EmailSender emailSender, UserRepository userRepository, PasswordEncoder passwordEncoder, VerificationTokenRepository verificationTokenRepository) {
        this.emailSender = emailSender;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.verificationTokenRepository = verificationTokenRepository;
    }

    @Override
    public void register(UserDto userDto) {
        User user = modelMapper.map(userDto, User.class);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setEnabled(false);
        saveOrUpdate(user);
    }

    @Override
    public void saveOrUpdate(User user) {
        if (userRepository.save(user) == null) {
            throw new NotSavedException(String.format("User with %s email was not saved or updated", user.getEmail()));
        }
    }

    @PreAuthorize("hasRole('ROLE_STUDENT')")
    @Override
    public List<UserDto> getAllTeachers() {
        List<UserDto> userDtos= userRepository.findAllByRoleId(1L).stream().map(teacher -> modelMapper.map(teacher, UserDto.class)).
                collect(Collectors.toList());
        if(userDtos.isEmpty()){
            throw new NotFoundException("There is no teachers");
        }
        return userDtos;
    }

    public UserDto getUserByEmail(String email) {
        return modelMapper.map(userRepository.findUserByEmail(email).
                orElseThrow(() -> new NotFoundException(String.format("User with %s email was not found", email))), UserDto.class);
    }

    @Override
    public void createVerificationToken(UserDto user, String token) {
        verificationTokenRepository.save(new VerificationToken(token,
                modelMapper.map(getUserByEmail(user.getEmail()), User.class)));
    }


    @Transactional
    @Override
    public void confirmRegistration(String token) {
        VerificationToken tokenData = verificationTokenRepository.findByToken(token).
                orElseThrow(() -> new InvalidTokenException("Invalid link or token"));
        if (new Timestamp(System.currentTimeMillis()).before(tokenData.getExpiryDate())) {
            User user = getUserById(tokenData.getUser().getId());
            user.setEnabled(true);
            saveOrUpdate(user);
            verificationTokenRepository.deleteById(tokenData.getId());
        } else throw new InvalidTokenException("Token is expired");
    }

    private User getUserById(Long id) {
        return userRepository.findById(id).orElseThrow(() ->
                new NotFoundException(String.format("User with %s id was not found", id)));
    }


}
