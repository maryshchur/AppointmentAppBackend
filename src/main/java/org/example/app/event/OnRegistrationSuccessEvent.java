package org.example.app.event;

import org.example.app.dto.UserDto;
import org.example.app.entities.User;
import org.springframework.context.ApplicationEvent;

public class OnRegistrationSuccessEvent extends ApplicationEvent {
    private static final long serialVersionUID = 1L;
    private UserDto user;

    public OnRegistrationSuccessEvent(UserDto user) {
        super(user);
        this.user = user;
    }


    public UserDto getUser() {
        return user;
    }

    public void setUser(UserDto user) {
        this.user = user;
    }
}
