package org.example.app.security;

import org.example.app.dto.LoginedUser;
import org.example.app.dto.RegisterUserDto;
import org.example.app.dto.UserDto;
import org.example.app.entities.User;
import org.example.app.exeptions.BadCredentialException;
import org.example.app.exeptions.NotFoundException;
import org.example.app.repository.UserRepository;
import org.example.app.security.config.WebSecurityConfig;
import org.example.app.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class AuthenticationService {

    private TokenManagementService tokenManagementService;
    private UserRepository userRepository;
    private WebSecurityConfig webSecurityConfig;

    @Autowired
    public AuthenticationService(TokenManagementService tokenManagementService,
                                 UserRepository userRepository,
                                 WebSecurityConfig webSecurityConfig) {
        this.tokenManagementService = tokenManagementService;
        this.userRepository = userRepository;
        this.webSecurityConfig = webSecurityConfig;
    }

    public String loginUser(LoginedUser loginUser) {

        Optional<User> user = userRepository.findUserByEmail(loginUser.getEmail());
        PasswordEncoder passwordEncoder = webSecurityConfig.passwordEncoder();
        if (user. isPresent()  && passwordEncoder.matches(loginUser.getPassword(), user.get().getPassword())) {
            if (!user.get().isEnabled()) {
                throw new DisabledException("Account is not active");
            }
            return tokenManagementService.generateTokenPair(loginUser.getEmail());
        } else {
            throw new BadCredentialException("BAD CREDENTIAL");
        }

    }
}
