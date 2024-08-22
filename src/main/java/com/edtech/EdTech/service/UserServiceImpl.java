package com.edtech.EdTech.service;

import com.edtech.EdTech.dto.UserDto;
import com.edtech.EdTech.exception.UserAlreadyExistsException;
import com.edtech.EdTech.model.user.User;
import com.edtech.EdTech.repository.RoleRepository;
import com.edtech.EdTech.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository,
                           RoleRepository roleRepository,
                           PasswordEncoder passwordEncoder){
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User saveUser(UserDto userDto) {
        try{
            // Check if a user with this email already exists
            if(userRepository.findByEmail(userDto.getEmail()) != null){
                throw new UserAlreadyExistsException("User with email " + userDto.getEmail() + " already exists.");
            }

            // If not, create and save the new user
            User user = new User();
            user.setFirstName(userDto.getFirstName());
            user.setLastName(userDto.getLastName());
            user.setEmail(userDto.getEmail());
            // encrypt the password using spring security
            user.setPassword(passwordEncoder.encode(userDto.getPassword()));

            return userRepository.save(user);
        }catch (Exception e){
            throw new RuntimeException("An error occurred while saving the user: " + e.getMessage());
        }
    }

    @Override
    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public List<UserDto> findAllUsers() {
        List<User> users = userRepository.findAll();

        return users.stream()
                .map((user) -> mapToUserDto(user))
                .collect(Collectors.toList());
    }

    private UserDto mapToUserDto(User user) {
        UserDto userDto = new UserDto();
        userDto.setFirstName(user.getFirstName());
        userDto.setLastName(user.getLastName());
        userDto.setEmail(user.getEmail());
        return userDto;
    }
}
