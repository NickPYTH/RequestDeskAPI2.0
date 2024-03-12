package de401t.repository;

import de401t.model.Role;
import de401t.model.Task;
import de401t.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Integer> {

    List<Task> getByCreator(User user);
}