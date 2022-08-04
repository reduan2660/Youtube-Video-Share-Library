package com.ytvideoshare.backend.repo;

import com.ytvideoshare.backend.domain.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepo extends JpaRepository<Role, Long> {
    Role findRoleByName(String name);
}
