package com.yapai.dreamcatcher.service;

import com.yapai.dreamcatcher.dto.UserDto;
import org.springframework.security.core.Authentication;

public interface IUserService {
    void addUser(Authentication authentication);
    UserDto getUserProfile(Authentication authentication);
}
