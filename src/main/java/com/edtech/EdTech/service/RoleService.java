package com.edtech.EdTech.service;

import com.edtech.EdTech.model.user.Role;

import java.util.List;

public interface RoleService {

    List<Role> getRoles();
    Role saveRole(Role theRole);

    Role findRoleByName(String name);
    void deleteRole(Long id);


}
