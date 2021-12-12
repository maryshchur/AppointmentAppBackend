package org.example.app.service.impl;

import com.sun.security.auth.callback.TextCallbackHandler;
import org.example.app.dto.RegisterTeacherDto;
import org.example.app.dto.RegisterUserDto;
import org.example.app.dto.TeacherDto;
import org.example.app.dto.UserDto;
import org.example.app.entities.*;
import org.example.app.exeptions.InvalidTokenException;
import org.example.app.exeptions.NotFoundException;
import org.example.app.exeptions.NotSavedException;
import org.example.app.repository.TeacherRepository;
import org.example.app.repository.UserRepository;
import org.example.app.repository.VerificationTokenRepository;
import org.example.app.service.FileStorageService;
import org.example.app.service.UserService;
import org.example.app.util.EmailSender;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.lang.reflect.Type;
import java.sql.Timestamp;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.example.app.util.Pagination.validatePage;
import static org.example.app.util.Pagination.validatePageSize;

@Service
public class UserServiceImpl implements UserService {
    private UserRepository userRepository;
    private TeacherRepository teacherRepository;
    private PasswordEncoder passwordEncoder;
    private VerificationTokenRepository verificationTokenRepository;
    private ModelMapper modelMapper;
    private final EmailSender emailSender;
    private FileStorageService fileStorageService;

    @Autowired
    public UserServiceImpl(ModelMapper modelMapper, EmailSender emailSender, UserRepository userRepository, TeacherRepository teacherRepository, PasswordEncoder passwordEncoder,
                           VerificationTokenRepository verificationTokenRepository, FileStorageService fileStorageService) {
        this.modelMapper = modelMapper;
        this.emailSender = emailSender;
        this.userRepository = userRepository;
        this.teacherRepository = teacherRepository;
        this.passwordEncoder = passwordEncoder;
        this.verificationTokenRepository = verificationTokenRepository;
        this.fileStorageService = fileStorageService;
    }

    @Override
    public Long register(RegisterUserDto registerUserDto) {
        User user = modelMapper.map(registerUserDto, User.class);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole( new Role(2L, "ROLE_STUDENT"));
        return userRepository.save(user).getId();
    }

    @Override
    public Long registerAsTeacher(RegisterTeacherDto registerUserDto) {
        Teacher user = modelMapper.map(registerUserDto, Teacher.class);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole( new Role(1L, "ROLE_TEACHER"));
        return userRepository.save(user).getId();
    }


    @Override
    public void saveOrUpdate(User user) {
        if (userRepository.save(user) == null) {
            throw new NotSavedException(String.format("User with %s email was not saved or updated", user.getEmail()));
        }
    }

    @Override
    public void changePhoto(MultipartFile multipartFile, Long id) throws Throwable {
        User user = getUserById(id);
        if (user.getImageUrl() != null) {
            fileStorageService.deleteFile(user.getImageUrl());
        }
        user.setImageUrl(fileStorageService.uploadFile(multipartFile));
        saveOrUpdate(user);
    }

    @Override
    public <T extends User> void deletePhoto(String email) throws Throwable {
        T user = findUserByEmail(email);
        fileStorageService.deleteFile(user.getImageUrl());
        user.setImageUrl(null);
        userRepository.save(user);
    }

    @Override
    public <T extends UserDto> T getUserData(Long id) {
       T tt= (T) userRepository.findById(id).get();
       if(  tt.role.getName().equals("ROLE_TEACHER")){
           return modelMapper.map(tt, (Type) RegisterUserDto.class);
       } else return modelMapper.map(tt, (Type) UserDto.class);
    }

    @PreAuthorize("hasRole('ROLE_STUDENT')")
    @Override
    public Page<TeacherDto> getAllTeachers(Integer page, Integer pageSize) {
        Pageable pageable = PageRequest.of(validatePage(page), validatePageSize(pageSize));
        //todo add some kind of sorting
//        Sort.Direction.DESC, "id");
        Page<TeacherDto> teachersPage = teacherRepository.findAll(pageable).map(teacher -> modelMapper.map(teacher, TeacherDto.class));
        if (teachersPage.isEmpty()) {
            throw new NotFoundException("There is no teachers");
        }
        return teachersPage;
    }

    public UserDto getUserByEmail(String email) throws Throwable {
        return modelMapper.map(findUserByEmail(email), UserDto.class);
    }

    private <T extends User> T findUserByEmail(String email) throws Throwable {
        return (T) userRepository.findUserByEmail(email)
                .orElseThrow(() ->
                        new NotFoundException(String.format("User with %s email was not found", email)));
    }


    @Override
    public void createVerificationToken(RegisterUserDto user, String token) throws Throwable {
        verificationTokenRepository.save(new VerificationToken(token,
                modelMapper.map(getUserByEmail(user.getEmail()), User.class)));
    }


    @Transactional
    @Override
    public void confirmRegistration(String token) throws Throwable {
        VerificationToken tokenData = verificationTokenRepository.findByToken(token).
                orElseThrow(() -> new InvalidTokenException("Invalid link or token"));
        if (new Timestamp(System.currentTimeMillis()).before(tokenData.getExpiryDate())) {
            User user = getUserById(tokenData.getUser().getId());
            user.setEnabled(true);
            saveOrUpdate(user);
            verificationTokenRepository.deleteById(tokenData.getId());
        } else throw new InvalidTokenException("Token is expired");
    }

    private <T extends User> T getUserById(Long id) throws Throwable {
        return (T) userRepository.findById(id)
                .orElseThrow(() ->
                        new NotFoundException(String.format("User with %s id was not found", id)));
    }

    @PreAuthorize("hasRole('ROLE_STUDENT')")
    @Override
    public void subscribe(String studentEmail, Long teacherId) throws Throwable {
        Student newSubscriber = findUserByEmail(studentEmail);
        Teacher teacher = getUserById(teacherId);
        Set<Student> subscribers = teacher.getSubscribers();
        if (subscribers.contains(newSubscriber)) {
            subscribers.remove(newSubscriber);
        } else {
            subscribers.add(newSubscriber);
        }
//        saveOrUpdate(teacher);
        userRepository.save(teacher);
    }

    @Override
    public String getRole(Long id){
        return userRepository.getUserRoleById(id);
    }

}
