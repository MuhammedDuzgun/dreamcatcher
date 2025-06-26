package com.yapai.dreamcatcher.controller;

import com.yapai.dreamcatcher.dto.DreamDto;
import com.yapai.dreamcatcher.dto.UserDto;
import com.yapai.dreamcatcher.service.crud.DreamService;
import com.yapai.dreamcatcher.service.crud.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;
    private final DreamService dreamService;

    public UserController(UserService userService, DreamService dreamService) {
        this.userService = userService;
        this.dreamService = dreamService;
    }

    @GetMapping("/profile")
    public ResponseEntity<UserDto> getUserProfile(Authentication authentication) {
        UserDto userDto = userService.getUserProfile(authentication);
        return ResponseEntity.ok(userDto);
    }

    @GetMapping("/dreams")
    public ResponseEntity<List<DreamDto>> getAllDreamsOfUser(Authentication authentication) {
        List<DreamDto> dreamDtos = dreamService.getAllDreamsOfUser(authentication);
        return ResponseEntity.ok(dreamDtos);
    }
}
