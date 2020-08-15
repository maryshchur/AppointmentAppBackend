package org.example.app.security;

import org.example.app.dto.LoginedUser;
import org.example.app.dto.UserDto;
import org.example.app.exeptions.BadCredentialException;
import org.example.app.security.config.WebSecurityConfig;
import org.example.app.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationService {

    private TokenManagementService tokenManagementService;
    private UserServiceImpl userService;
    private WebSecurityConfig webSecurityConfig;

    /**
     * constructor
     *
     * @param tokenManagementService {@link TokenManagementService}
     * @param userService            {@link UserServiceImpl}
     */
    @Autowired
    public AuthenticationService(TokenManagementService tokenManagementService,
                                 UserServiceImpl userService,
                                 WebSecurityConfig webSecurityConfig) {
        this.tokenManagementService = tokenManagementService;
        this.userService = userService;
        this.webSecurityConfig = webSecurityConfig;
    }

    /**
     * authentication user. Method verify user credential. If user is in DB than generating access and refresh token, if no thrown
     * BadCredentialException exception
     *
     * @param loginUser {@link LoginedUser}
     * @return JwtDto
     * @throws BadCredentialException
     */
    public String loginUser(LoginedUser loginUser) {

        UserDto user = userService.getUserByEmail(loginUser.getEmail());

        PasswordEncoder passwordEncoder = webSecurityConfig.passwordEncoder();

        if (passwordEncoder.matches(loginUser.getPassword(), user.getPassword())) {
            if (!user.isEnabled()) {
                throw new DisabledException("Account is not active");
            }
            return tokenManagementService.generateTokenPair(loginUser.getEmail());

        } else {
            throw new BadCredentialException("BAD CREDENTIAL");
        }
    }
}
