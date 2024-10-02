package com.edtech.EdTech.service;

import com.edtech.EdTech.dto.CourseDto;
import com.edtech.EdTech.dto.UserDisplayDto;
import com.edtech.EdTech.dto.UserDto;
import com.edtech.EdTech.exception.UserAlreadyExistsException;
import com.edtech.EdTech.model.courses.Course;
import com.edtech.EdTech.model.users.Role;
import com.edtech.EdTech.model.users.User;
import com.edtech.EdTech.repository.RoleRepository;
import com.edtech.EdTech.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.apache.tomcat.util.codec.binary.Base64;
import org.hibernate.annotations.processing.SQL;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.rowset.serial.SerialBlob;
import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service

public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final CourseService courseService;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository,
                           CourseService courseService,
                           PasswordEncoder passwordEncoder){
        this.userRepository = userRepository;
        this.courseService = courseService;
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
        user.setName(userDto.getName());
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
    public UserDisplayDto findUserById(Long userId) {
        User theUser = userRepository.findById(userId)
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

    @Override
    public User updateUserProfile(Long userId, MultipartFile profilePicture) throws IOException, SQLException {

        User theUser = userRepository.findById(userId)
                .orElseThrow(()-> new UsernameNotFoundException("User not found"));

        if(!profilePicture.isEmpty()){
            byte[] photoBytes = profilePicture.getBytes();
            Blob photoBlob = new SerialBlob(photoBytes);
            theUser.setProfilePicture(photoBlob);
        }
        return userRepository.save(theUser);
    }

    @Override
    public byte[] getProfilePictureByUserId(Long userId) throws SQLException {
        User theUser = userRepository.findById(userId)
                .orElseThrow(()-> new UsernameNotFoundException("User not found"));

        Blob photoBlob = theUser.getProfilePicture();
        if(photoBlob != null){
            return photoBlob.getBytes(1, (int) photoBlob.length());
        }
        return null;
    }

    @Transactional
    @Override
    public void deleteUser(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(()->new UsernameNotFoundException("User with email " + email + " not found."));

        userRepository.delete(user);
    }

    public UserDisplayDto mapToUserDto(User user) {
        UserDisplayDto userDto = new UserDisplayDto();
        userDto.setId(user.getId());
        userDto.setName(user.getName());
        userDto.setEmail(user.getEmail());

        List<CourseDto> courses = user.getCourses().stream()
                        .map(this::mapToCourseDto).collect(Collectors.toList());

        userDto.setCourses(courses);
        try {
            byte[] photoBytes = this.getProfilePictureByUserId(user.getId());
            if (photoBytes != null && photoBytes.length > 0) {
                String base64Photo = Base64.encodeBase64String(photoBytes);
                userDto.setProfilePicture(base64Photo);
            }
        }catch(SQLException e){
            System.err.println("Error retrieving profile picture: " + e.getMessage());
        }
        return userDto;
    }

    private CourseDto mapToCourseDto(Course course){
        CourseDto courseDto = new CourseDto();
        courseDto.setCourseId(course.getId());
        courseDto.setTitle(course.getTitle());
        try{
            byte[] photoBytes = courseService.getThumbnailByCourseId(course.getId());
            if (photoBytes != null && photoBytes.length > 0) {
                String base64Photo = Base64.encodeBase64String(photoBytes);
               courseDto.setThumbnail(base64Photo);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return courseDto;
    }
}
