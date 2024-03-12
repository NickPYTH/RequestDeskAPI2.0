package de401t.repository;

import de401t.model.Filial;
import de401t.model.Status;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StatusRepository extends JpaRepository<Status, Integer> {
    Status getByCode(int i);

    Status getByName(String status);
}
