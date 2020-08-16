package org.example.app.controller;


import org.example.app.dto.LoginedUser;
import org.example.app.dto.RegisterUserDto;
import org.example.app.event.OnRegistrationSuccessEvent;
import org.example.app.security.AuthenticationService;
import org.example.app.security.TokenManagementService;
import org.example.app.service.BookedLessonsService;
import org.example.app.service.FreeTimeService;
import org.example.app.service.PrizeService;
import org.example.app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;


@RestController
@Validated
public class UserController {
    private AuthenticationService authenticationService;
    private TokenManagementService tokenManagementService;
    private String AUTHORIZATION_HEADER = "authorization";
    private String AUTH_HEADER_PREFIX = "Bearer ";
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
    public ResponseEntity createUser(@Valid @RequestBody RegisterUserDto registerUserDto) {
        userService.register(registerUserDto);
        eventPublisher.publishEvent(new OnRegistrationSuccessEvent(registerUserDto));
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/registration-confirm")
    public ResponseEntity confirmRegistration(@RequestParam("token") String token) {
        userService.confirmRegistration(token);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PostMapping("/authentication")
    public ResponseEntity<Object> login(@RequestBody @Valid LoginedUser loginUser, HttpServletResponse response) {

        String s = authenticationService.loginUser(loginUser);
        response.setHeader(AUTHORIZATION_HEADER, AUTH_HEADER_PREFIX + s);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
