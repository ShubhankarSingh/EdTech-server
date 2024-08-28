package com.edtech.EdTech.service;

import com.edtech.EdTech.dto.UserDisplayDto;
import com.edtech.EdTech.dto.UserDto;
import com.edtech.EdTech.model.users.User;

import java.util.List;

public interface UserService {

    User saveUser(UserDto userDto);

    UserDisplayDto findUserByEmail(String email);

    List<UserDisplayDto> findAllUsers();

    void deleteUser(String email);

}
