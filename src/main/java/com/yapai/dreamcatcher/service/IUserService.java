package com.yapai.dreamcatcher.service;

import org.springframework.security.core.Authentication;

public interface IUserService {
    void addUser(Authentication authentication);
}
