package org.example.app.controller;


import lombok.RequiredArgsConstructor;
import org.example.app.dto.LoginedUser;
import org.example.app.dto.RegisterTeacherDto;
import org.example.app.dto.RegisterUserDto;
import org.example.app.dto.UserDto;
import org.example.app.entities.User;
import org.example.app.event.OnRegistrationSuccessEvent;
import org.example.app.security.AuthenticationService;
import org.example.app.security.TokenManagementService;
import org.example.app.security.UserPrincipal;
import org.example.app.service.BookedLessonsService;
import org.example.app.service.FreeTimeService;
import org.example.app.service.PrizeService;
import org.example.app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;


@RestController
@Validated
@CrossOrigin(origins = "http://localhost:3000")
//@RequiredArgsConstructor
public class UserController<T extends UserDto> {
    private AuthenticationService authenticationService;
    private TokenManagementService tokenManagementService;
    private UserService userService;
    private FreeTimeService freeTimeService;
    private ApplicationEventPublisher eventPublisher;
    private BookedLessonsService bookedLessonsService;
    private PrizeService prizeService;

    @Autowired
    public UserController(AuthenticationService authenticationService, TokenManagementService tokenManagementService, UserService userService,
                          FreeTimeService freeTimeService, ApplicationEventPublisher eventPublisher, BookedLessonsService bookedLessonsService) {
        this.authenticationService = authenticationService;
        this.tokenManagementService = tokenManagementService;
        this.userService = userService;
        this.freeTimeService = freeTimeService;
        this.eventPublisher = eventPublisher;
        this.bookedLessonsService = bookedLessonsService;
    }

    @PostMapping("/registration")
    public ResponseEntity<Long> createUser(@Valid @RequestBody RegisterUserDto registerUserDto) {
        Long id = userService.register(registerUserDto);
        eventPublisher.publishEvent(new OnRegistrationSuccessEvent(registerUserDto));
        return ResponseEntity.status(HttpStatus.CREATED).body(id);
    }

    @PostMapping("/registration-teacher")
    public ResponseEntity<Long> registrationAsTeacher(@Valid @RequestBody RegisterTeacherDto registerTeacherDto) {
        Long id = userService.registerAsTeacher(registerTeacherDto);
        eventPublisher.publishEvent(new OnRegistrationSuccessEvent(registerTeacherDto));
        return ResponseEntity.status(HttpStatus.CREATED).body(id);
    }

//    @PutMapping("/upload-photo/{id}")
//    public ResponseEntity changePhoto(@RequestPart(value = "file") MultipartFile file,
//                                      @PathVariable Long id) throws Throwable {
//        userService.changePhoto(file, id);
//        return ResponseEntity.status(HttpStatus.OK).build();
//    }

    @GetMapping("/registration-confirm")
    public ResponseEntity confirmRegistration(@RequestParam("token") String token) throws Throwable {
        userService.confirmRegistration(token);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PostMapping("/authentication")
    public ResponseEntity<String> login(@RequestBody @Valid LoginedUser loginUser) {

        return ResponseEntity.status(HttpStatus.OK).body(authenticationService.loginUser(loginUser));
    }

    @PutMapping("/profile/updatePhoto")
    public ResponseEntity changePhoto(@RequestPart(value = "file") MultipartFile file,
                                      @ApiIgnore @AuthenticationPrincipal UserPrincipal principal) throws Throwable {
        userService.changePhoto(file, principal.getUser().getId());
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping("/user-role")
    public ResponseEntity<String> getCurrentUserRole(@ApiIgnore @AuthenticationPrincipal UserPrincipal principal) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.getRole(principal.getUser().getId()));
    }

    @GetMapping("/profile")
    public ResponseEntity<T> getProfileData(@ApiIgnore @AuthenticationPrincipal UserPrincipal principal) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.getUserData(principal.getUser().getId()));
    }

}
