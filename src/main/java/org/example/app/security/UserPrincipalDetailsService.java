package org.example.app.security;

import lombok.SneakyThrows;
import org.example.app.entities.User;
import org.example.app.exeptions.NotFoundException;
import org.example.app.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
public class UserPrincipalDetailsService implements UserDetailsService {
    private UserRepository userRepository;

    public UserPrincipalDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @SneakyThrows
    @Override
    public UserDetails loadUserByUsername(String username) {
        User user = (User) userRepository.findUserByEmail(username)
                .orElseThrow(
                () -> new NotFoundException("User does not exist"));
        return UserPrincipal.create(user);
    }
}
