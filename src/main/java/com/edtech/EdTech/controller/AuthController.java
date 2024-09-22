package com.edtech.EdTech.controller;

import com.edtech.EdTech.dto.JwtResponse;
import com.edtech.EdTech.dto.LoginDto;
import com.edtech.EdTech.dto.UserDto;
import com.edtech.EdTech.exception.UserAlreadyExistsException;
import com.edtech.EdTech.model.users.User;
import com.edtech.EdTech.security.jwt.JwtUtils;
import com.edtech.EdTech.security.user.CustomUserDetails;
import com.edtech.EdTech.service.UserService;
import jakarta.validation.Valid;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    @Getter
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
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
            return ResponseEntity.status(HttpStatus.CONFLICT).body("An error occurred: " + e.getMessage());
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginDto loginDto){
        try{
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword()));

            SecurityContextHolder.getContext().setAuthentication(authentication);

            // Generate JWT Token
            String token = jwtUtils.generateToken(authentication);
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

            return ResponseEntity.ok(new JwtResponse(
                    userDetails.getId(),
                    userDetails.getEmail(),
                    token
            ));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage() + ":" + " Invalid email or password.");
        }
    }

}
