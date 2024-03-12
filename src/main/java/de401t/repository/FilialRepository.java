package de401t.repository;

import de401t.model.Filial;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FilialRepository extends JpaRepository<Filial, Integer> {
    boolean existsByName(String name);

    boolean existsByCode(Integer code);

    Filial getByCode(Integer code);
}
