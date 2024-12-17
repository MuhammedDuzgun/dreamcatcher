package com.yapai.dreamcatcher.mapper;

import com.yapai.dreamcatcher.dto.UserDto;
import com.yapai.dreamcatcher.entity.User;

public class UserMapper {

    public static UserDto mapToUserDto(User user) {
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setEmail(user.getEmail());
        userDto.setFirstName(user.getFirstName());
        userDto.setLastName(user.getLastName());
        userDto.setPicture(user.getPicture());
        return userDto;
    }

}
