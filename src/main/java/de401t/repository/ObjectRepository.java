package de401t.repository;

import de401t.model.Filial;
import de401t.model.Obj;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ObjectRepository extends JpaRepository<Obj, Integer> {
    boolean existsByName(String name);

    Obj getByName(String objectName);

    boolean existsByNameAndFilial(String objectName, Filial filial);

    Obj getByNameAndFilial(String objectName, Filial filial);
}
