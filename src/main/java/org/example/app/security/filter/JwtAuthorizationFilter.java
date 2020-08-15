package org.example.app.security.filter;

import io.jsonwebtoken.ExpiredJwtException;
import org.example.app.security.TokenManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtAuthorizationFilter extends OncePerRequestFilter {
    private TokenManagementService tokenManagementService;

    @Autowired
    public JwtAuthorizationFilter(TokenManagementService tokenManagementService) {
        this.tokenManagementService = tokenManagementService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        String accessToken = tokenManagementService.resolveAccessToken(httpServletRequest);

        if (accessToken != null) {
            try {
                if (tokenManagementService.validateToken(accessToken)) {
                    Authentication authentication =
                            tokenManagementService.getAuthentication(accessToken);
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            } catch (ExpiredJwtException e) {
                //LOGGER.info("Token has expired: " + accessToken);

            }
            catch (Exception e) {
                ///LOGGER.info("JWT Authentication failed");
            }

        }
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }
}
