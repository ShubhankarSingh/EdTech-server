package com.edtech.EdTech.service;

import com.edtech.EdTech.exception.RoleAlreadyExistsException;
import com.edtech.EdTech.model.user.Role;
import com.edtech.EdTech.repository.RoleRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.management.relation.RoleNotFoundException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService{

    private RoleRepository roleRepository;

    @Override
    public List<Role> getRoles() {
        return roleRepository.findAll();
    }

    @Override
    public Role saveRole(Role theRole) {
        String roleName = "ROLE_" + theRole.getName().toUpperCase();
        Role role = new Role(roleName);
        if(roleRepository.existsByName(roleName)){
            throw new RoleAlreadyExistsException(theRole.getName() + " role already exists.");
        }
        return roleRepository.save(role);
    }

    @Override
    public Role findRoleByName(String name) {
        return roleRepository.findByName(name)
                .orElseThrow(()-> new RuntimeException("Role not found."));
    }

    @Override
    public void deleteRole(Long id) {
        roleRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("Role not found."));
        roleRepository.deleteById(id);
    }
}
