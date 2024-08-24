package com.edtech.EdTech.controller;

import com.edtech.EdTech.dto.UserDto;
import com.edtech.EdTech.exception.UserAlreadyExistsException;
import com.edtech.EdTech.model.user.User;
import com.edtech.EdTech.service.UserServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private UserServiceImpl userService;

    /*public AuthController(UserService userService){
        this.userService = userService;
    }*/

    @GetMapping("/index")
    public String home(){
        return "index";
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody UserDto userDto){
        try{
            User user = userService.saveUser(userDto);
            return ResponseEntity.ok("Registration Successful");
        }catch(UserAlreadyExistsException e){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + e.getMessage());
        }
    }



}
