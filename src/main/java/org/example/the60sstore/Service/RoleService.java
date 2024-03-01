package org.example.testspring.Service;

import org.example.testspring.Entity.Role;
import org.example.testspring.Repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleService {

    private final RoleRepository roleRepository;

    @Autowired
    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public Role getRoleByRoleName(String roleName) {
        return roleRepository.getRoleByRolename(roleName);
    }
}
