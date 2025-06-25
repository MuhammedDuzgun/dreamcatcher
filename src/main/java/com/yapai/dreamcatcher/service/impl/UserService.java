package com.yapai.dreamcatcher.service.impl;

import com.yapai.dreamcatcher.dto.UserDto;
import com.yapai.dreamcatcher.entity.User;
import com.yapai.dreamcatcher.exception.ResourceNotFoundException;
import com.yapai.dreamcatcher.repository.IUserRepository;
import com.yapai.dreamcatcher.service.IUserService;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
public class UserService implements IUserService {

    private final IUserRepository userRepository;

    public UserService(IUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void addUser(Authentication authentication) {
        OAuth2AuthenticationToken token = (OAuth2AuthenticationToken) authentication;
        OAuth2User oAuth2User = token.getPrincipal();
        String email = oAuth2User.getAttribute("email");
        String firstName = oAuth2User.getAttribute("given_name");
        String lastName = oAuth2User.getAttribute("family_name");
        String picture = oAuth2User.getAttribute("picture");

        userRepository.findByEmail(email)
                .orElseGet(() -> {
                    User user = new User();
                    user.setEmail(email);
                    user.setFirstName(firstName);
                    user.setLastName(lastName);
                    user.setPicture(picture);
                    return userRepository.save(user);
                });
    }

    @Override
    public UserDto getUserProfile(Authentication authentication) {
        if (authentication == null) {
            throw new AuthenticationCredentialsNotFoundException("User not authenticated");
        }

        OAuth2AuthenticationToken token = (OAuth2AuthenticationToken) authentication;
        OAuth2User oAuth2User = token.getPrincipal();
        String email = oAuth2User.getAttribute("email");

        User user = userRepository.findByEmail(email)
                .orElseThrow(()-> new ResourceNotFoundException("user not found"));

        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setEmail(email);
        userDto.setFirstName(user.getFirstName());
        userDto.setLastName(user.getLastName());
        userDto.setPicture(user.getPicture());
        return userDto;
    }

}
