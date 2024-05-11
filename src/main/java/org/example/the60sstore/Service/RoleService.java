package org.example.the60sstore.Service;

import org.example.the60sstore.Entity.Role;
import org.example.the60sstore.Repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/* RoleService returns Role or list of them to Controller. */
@Service
public class RoleService {

    private final RoleRepository roleRepository;

    /* Service always need to create Repository first. */
    @Autowired
    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    /* getRoleByRoleName method gets role object by roleName param. */
    public Role getRoleByRoleName(String roleName) {
        return roleRepository.getRoleByRolename(roleName);
    }
}
