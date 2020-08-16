package org.example.app.event;

import org.example.app.controller.UserController;
import org.example.app.dto.RegisterUserDto;
import org.example.app.service.UserService;
import org.example.app.util.EmailSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.UUID;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class RegistrationEmailListener implements ApplicationListener<OnRegistrationSuccessEvent> {

    private UserService userService;
    private final EmailSender emailSender;

    @Autowired
    public RegistrationEmailListener(UserService userService,EmailSender emailSender) {
        this.userService = userService;
        this.emailSender=emailSender;
    }

    @Override
    public void onApplicationEvent(OnRegistrationSuccessEvent onRegistrationSuccessEvent) {
        confirmRegistration(onRegistrationSuccessEvent);
    }

    private void confirmRegistration(OnRegistrationSuccessEvent event){
        RegisterUserDto user = event.getUser();
        String token = UUID.randomUUID().toString();
        userService.createVerificationToken(user,token);
        emailSender.sendEmail("Registration Confirmation",
                "Thank you for registration. " +
                        "Please click on the below link to activate your account."
                        +linkTo(methodOn(UserController.class).confirmRegistration(token)),
                event.getUser().getEmail());

    }
}
