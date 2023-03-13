package org.example.app.event;

import org.example.app.dto.RegisterUserDto;
import org.springframework.context.ApplicationEvent;
import org.springframework.stereotype.Component;


public class OnRegistrationSuccessEvent extends ApplicationEvent {
    private static final long serialVersionUID = 1L;
    private RegisterUserDto user;

    public OnRegistrationSuccessEvent(RegisterUserDto user) {
        super(user);
        this.user = user;
    }


    public RegisterUserDto getUser() {
        return user;
    }

    public void setUser(RegisterUserDto user) {
        this.user = user;
    }
}
