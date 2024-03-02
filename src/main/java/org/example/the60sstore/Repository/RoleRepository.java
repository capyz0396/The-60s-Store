package org.example.the60sstore.Repository;

import org.example.the60sstore.Entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {

    Role getRoleByRoleid(Integer roleId);

    Role getRoleByRolename(String roleName);
}
