package com.yapai.dreamcatcher.config;

import com.yapai.dreamcatcher.service.crud.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;

import java.io.IOException;

@Configuration
public class SecurityConfig {

    private final UserService userService;

    public SecurityConfig(UserService userService) {
        this.userService = userService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .cors(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(registry -> {
                    registry.requestMatchers("/api/dream/add-dream").authenticated();
                    registry.requestMatchers("/api/dream/delete-dream/").authenticated();
                    registry.requestMatchers("/api/comment/add-comment").authenticated();
                    registry.requestMatchers("/api/comment/delete-comment/").authenticated();
                    registry.requestMatchers("/api/user/").authenticated();
                    registry.requestMatchers("/api/user/dreams/").authenticated();
                    registry.anyRequest().permitAll();
                }).oauth2Login(oauth2login -> {
                    oauth2login.loginPage("/login");
                    oauth2login.successHandler(new AuthenticationSuccessHandler() {
                        @Override
                        public void onAuthenticationSuccess(HttpServletRequest request,
                                                            HttpServletResponse response,
                                                            Authentication authentication) throws IOException {
                            userService.addUser(authentication);
                            authentication.getCredentials();
                            response.sendRedirect("/home");
                        }
                    });
                })
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
                )
                .build();
    }
}
