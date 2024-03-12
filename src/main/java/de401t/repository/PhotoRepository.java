package de401t.repository;

import de401t.model.Photo;
import de401t.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PhotoRepository extends JpaRepository<Photo, Integer> {

}