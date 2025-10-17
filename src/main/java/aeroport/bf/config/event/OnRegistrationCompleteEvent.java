package aeroport.bf.config.event;

import java.util.UUID;

import org.springframework.context.ApplicationEvent;

import aeroport.bf.domain.User;

public class OnRegistrationCompleteEvent extends ApplicationEvent {
    private final User user;

    public OnRegistrationCompleteEvent(User user) {
        super(user);
        this.user = user;
    }

    public User getUser() {
        return user;
    }
}
