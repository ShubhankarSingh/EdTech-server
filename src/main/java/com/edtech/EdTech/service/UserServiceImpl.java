package com.edtech.EdTech.service;

import com.edtech.EdTech.dto.UserDisplayDto;
import com.edtech.EdTech.dto.UserDto;
import com.edtech.EdTech.exception.UserAlreadyExistsException;
import com.edtech.EdTech.model.users.Role;
import com.edtech.EdTech.model.users.User;
import com.edtech.EdTech.repository.RoleRepository;
import com.edtech.EdTech.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository,
                           RoleRepository roleRepository,
                           PasswordEncoder passwordEncoder){
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User saveUser(UserDto userDto) {

        // Check if a user with this email already exists

        if(userRepository.findByEmail(userDto.getEmail()).isPresent()){
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

    }

    @Override
    public UserDisplayDto findUserByEmail(String email) {
        User theUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return mapToUserDto(theUser);
    }

    @Override
    public List<UserDisplayDto> findAllUsers() {
        List<User> users = userRepository.findAll();

        return users.stream()
                .map(this::mapToUserDto)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public void deleteUser(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(()->new UsernameNotFoundException("User with email " + email + " not found."));

        userRepository.delete(user);
    }

    private UserDisplayDto mapToUserDto(User user) {
        UserDisplayDto userDto = new UserDisplayDto();
        userDto.setFirstName(user.getFirstName());
        userDto.setLastName(user.getLastName());
        userDto.setEmail(user.getEmail());
        return userDto;
    }
}
