package com.edtech.EdTech.service;

import com.edtech.EdTech.dto.UserDisplayDto;
import com.edtech.EdTech.dto.UserDto;
import com.edtech.EdTech.model.users.User;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public interface UserService {

    User saveUser(UserDto userDto);

    UserDisplayDto findUserByEmail(String email);

    UserDisplayDto findUserById(Long userId);

    List<UserDisplayDto> findAllUsers();

    User updateUserProfile(Long userId, MultipartFile profilePicture) throws IOException, SQLException;

    byte[] getProfilePictureByUserId(Long userId) throws SQLException;
    void deleteUser(String email);

}
