package de401t.repository;

import javax.transaction.Transactional;

import de401t.model.Role;
import de401t.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Set;

public interface UserRepository extends JpaRepository<User, Integer> {

  boolean existsByUsername(String username);

  User findByUsername(String username);

  @Transactional
  void deleteByUsername(String username);

  User findByRolesIn(Set<Role> roles);
}
