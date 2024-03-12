package de401t.repository;

import de401t.model.Message;
import de401t.model.Task;
import de401t.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Integer> {
    List<Message> getAllByUserFromAndTask(User user, Task task);

    List<Message> getAllByTask(Task task);
}