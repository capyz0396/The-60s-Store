package org.example.testspring.Repository;

import org.example.testspring.Entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {

    Role getRoleByRoleid(Integer roleId);

    Role getRoleByRolename(String roleName);
}
