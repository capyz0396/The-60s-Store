package org.example.the60sstore.Service;

import org.example.the60sstore.Entity.Role;
import org.example.the60sstore.Repository.RoleRepository;
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
