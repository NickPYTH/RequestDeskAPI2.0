package de401t.repository;

import de401t.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoleRepository extends JpaRepository<Role, Integer> {
    Role findByCode(Integer role);

    List<Role> findAllByCode(int i);
}