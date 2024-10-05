package com.edtech.EdTech.controller;

import com.edtech.EdTech.dto.UserDisplayDto;
import com.edtech.EdTech.dto.UserDto;
import com.edtech.EdTech.model.users.User;
import com.edtech.EdTech.service.UserService;
import lombok.AllArgsConstructor;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping("/api/users")
@AllArgsConstructor
public class UserController {

    private UserService userService;

    // API to fetch a single user from the DB using id or email
//    @GetMapping("/user/{email}")
//    public ResponseEntity<?> findUserByEmail(@PathVariable("email") String email){
//
//        try {
//            UserDisplayDto theUser = userService.findUserByEmail(email);
//            return ResponseEntity.ok(theUser);
//        }catch (UsernameNotFoundException e){
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
//        }catch (Exception e){
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error fetching user: " + e.getMessage());
//        }
//    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<?> findUserById(@PathVariable("userId") Long userId){
        try {

            UserDisplayDto theUser = userService.findUserById(userId);
            byte[] photoBytes = userService.getProfilePictureByUserId(userId);

            if(photoBytes != null && photoBytes.length > 0){
                String base64Photo = Base64.encodeBase64String(photoBytes);
                theUser.setProfilePicture(base64Photo);
            }

            return ResponseEntity.ok(theUser);
        }catch (UsernameNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error fetching user: " + e.getMessage());
        }
    }

    // API fetch all the users from the DB, use UserDto instead of User for data privacy
    @GetMapping("/all-users")
    public ResponseEntity<List<UserDisplayDto>> getAllUsers(){
        List<UserDisplayDto> allUsers = userService.findAllUsers();
        return ResponseEntity.ok(allUsers);
    }

    @PutMapping("/{userId}/update-profile")
    public ResponseEntity<?> updateUserProfile(@PathVariable("userId") Long userId,
                                               @RequestParam("profilePicture") MultipartFile profilePicture) throws IOException, SQLException {

        try {
            User theUser = userService.updateUserProfile(userId, profilePicture);
            return ResponseEntity.ok(theUser);
        }catch (UsernameNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating profile: " + e.getMessage());
        }
    }

    @DeleteMapping("/delete/{email}")
    public ResponseEntity<String> deleteUser(@PathVariable("email") String email){
        try {
            userService.deleteUser(email);
            return ResponseEntity.ok("Deleted user successfully");
        }catch (UsernameNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error deleting user: " + e.getMessage());
        }
    }

}
