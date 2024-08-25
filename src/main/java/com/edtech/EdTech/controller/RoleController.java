package com.edtech.EdTech.controller;


import com.edtech.EdTech.exception.RoleAlreadyExistsException;
import com.edtech.EdTech.model.user.Role;
import com.edtech.EdTech.service.RoleService;
import com.edtech.EdTech.service.RoleServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/roles")
public class RoleController {

    private RoleService roleService;

    @PostMapping
    public ResponseEntity<?> createRole(@RequestBody Role role){
        try{
            roleService.saveRole(role);
            return ResponseEntity.ok("New role created successfully.");
        }catch(RoleAlreadyExistsException e){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    @GetMapping("/all")
    public ResponseEntity<List<Role>> getAllRoles(){
        List<Role> roles = roleService.getRoles();
        return ResponseEntity.ok(roles);
    }

    @GetMapping("/{name}")
    public ResponseEntity<?> getRoleByName(@PathVariable String name){
        try{
            Role theRole = roleService.findRoleByName(name);
            return ResponseEntity.ok(theRole);
        }catch(RuntimeException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteRole(@PathVariable Long id){
        try {
            roleService.deleteRole(id);
            return ResponseEntity.ok("Deleted role successfully");
        }catch (RuntimeException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error deleting user: " + e.getMessage());
        }
    }

}
