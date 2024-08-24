package com.edtech.EdTech.service;

import com.edtech.EdTech.dto.UserDto;
import com.edtech.EdTech.model.user.User;

import java.util.List;

public interface UserService {

    User saveUser(UserDto userDto);

    User findUserByEmail(String email);

    List<UserDto> findAllUsers();

    void deleteUser(String email);

}
