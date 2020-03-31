package com.yatseniuk.taskmanager.security;

import io.jsonwebtoken.ExpiredJwtException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private static final Logger LOG = LoggerFactory.getLogger(JwtAuthorizationFilter.class);
    private TokenManagement tokenManagement;

    @Autowired
    public JwtAuthorizationFilter(TokenManagement tokenManagement) {
        this.tokenManagement = tokenManagement;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String accessToken = tokenManagement.resolveAccessToken(request);

        if (accessToken != null) {
            try {
                if (tokenManagement.validateRefreshToken(accessToken)) {
                    Authentication authentication = tokenManagement.getAuthentication(accessToken);
                    LOG.info("User successfully authenticate - {}", authentication.getPrincipal());
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            } catch (ExpiredJwtException e) {
                LOG.info("Token has expired: " + accessToken);
                throw new RuntimeException("Expired token");
            } catch (Exception e) {
                LOG.info("JWT Authentication failed");
                throw new RuntimeException("Authentication failed");
            }
        }
        filterChain.doFilter(request, response);
    }
}