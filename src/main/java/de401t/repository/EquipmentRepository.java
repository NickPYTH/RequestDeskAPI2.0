package de401t.repository;

import de401t.model.Equipment;
import de401t.model.Obj;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EquipmentRepository extends JpaRepository<Equipment, Integer> {
    boolean existsByName(String name);
    List<Equipment> findByObj(Obj obj);

    Equipment findByCode(String code);

    List<Equipment> findByNameAndCodeAndObj(String name, String code, Obj obj);

    List<Equipment> findByNameAndCode(String name, String code);
}
