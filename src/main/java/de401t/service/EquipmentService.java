package de401t.service;

import de401t.dto.EquipmentDataDTO;
import de401t.dto.EquipmentUniquesDataDTO;
import de401t.exception.CustomException;
import de401t.model.Equipment;
import de401t.model.Obj;
import de401t.repository.*;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor

public class EquipmentService {

    private final EquipmentRepository equipmentRepository;
    private final ObjectRepository objectRepository;
    private final EquipmentUniqueCodesRepository equipmentUniqueCodesRepository;
    private final EquipmentUniqueNamesRepository equipmentUniqueNamesRepository;

    public EquipmentDataDTO create(EquipmentDataDTO equipmentDataDTO) {
        ModelMapper modelMapper = new ModelMapper();
        Equipment equipment = modelMapper.map(equipmentDataDTO, Equipment.class);
        if (!equipmentRepository.existsByName(equipment.getName())) {
            Optional<Obj> objectOpt = objectRepository.findById(equipmentDataDTO.getObj());
            if (objectOpt.isPresent()){
                Obj object = objectOpt.get();
                equipment.setObj(object);
                equipmentRepository.save(equipment);
                return equipmentDataDTO;
            }
            else throw new CustomException("Объект не найден", HttpStatus.NOT_FOUND);
        } else {
            throw new CustomException("Оборудование с таким наименованием уже существует", HttpStatus.NOT_FOUND);
        }
    }

    public Integer delete(Integer id) {
        if (equipmentRepository.findById(id).isPresent()) {
            equipmentRepository.deleteById(id);
            return id;
        } else {
            throw new CustomException("Оборудование не существует", HttpStatus.NOT_FOUND);
        }
    }

    public EquipmentDataDTO update(EquipmentDataDTO equipmentDataDTO) {
        Optional<Equipment> equipmentOpt = equipmentRepository.findById(equipmentDataDTO.getId());
        if (equipmentOpt.isPresent()) {
            Equipment equipment = equipmentOpt.get();
            equipment.setName(equipmentDataDTO.getName());
            equipment.setCode(equipmentDataDTO.getCode());
            Optional<Obj> objectOpt = objectRepository.findById(equipmentDataDTO.getObj());
            if (objectOpt.isPresent()){
                Obj object = objectOpt.get();
                equipment.setObj(object);
                equipmentRepository.save(equipment);
                return equipmentDataDTO;
            }
            throw new CustomException("Объект с таким id не существует", HttpStatus.NOT_FOUND);
        } else {
            throw new CustomException("Оборудование с таким id не существует", HttpStatus.NOT_FOUND);
        }
    }

    public EquipmentDataDTO get(Integer id) {
        Optional<Equipment> equipmentOpt = equipmentRepository.findById(id);
        if (equipmentOpt.isPresent()) {
            EquipmentDataDTO responseDTO = new EquipmentDataDTO();
            responseDTO.setId(equipmentOpt.get().getId());
            responseDTO.setName(equipmentOpt.get().getName());
            responseDTO.setCode(equipmentOpt.get().getCode());
            return responseDTO;
        } else {
            throw new CustomException("Оборудование с таким id не существует", HttpStatus.NOT_FOUND);
        }
    }

    public List<EquipmentDataDTO> getAll() {
        List<EquipmentDataDTO> responseDTOList = new ArrayList<>();
        for (Equipment equipment: equipmentRepository.findAll()){
            EquipmentDataDTO tmp = new EquipmentDataDTO();
            tmp.setId(equipment.getId());
            tmp.setName(equipment.getName());
            tmp.setCode(equipment.getCode());
            tmp.setObj(equipment.getObj().getId());
            tmp.setObjName(equipment.getObj().getName());
            tmp.setFilialAndObj(equipment.getObj().getFilial().getName() + ' ' + equipment.getObj().getName());
            responseDTOList.add(tmp);
        }
        return responseDTOList;
    }

    public EquipmentUniquesDataDTO getAllUniqValues() {
        EquipmentUniquesDataDTO uniques = new EquipmentUniquesDataDTO();
        uniques.setNames(equipmentUniqueNamesRepository.findAll().stream().map(s -> s.getName()).collect(Collectors.toList()));
        uniques.setCodes(equipmentUniqueCodesRepository.findAll().stream().map(s -> s.getCode()).collect(Collectors.toList()));
        uniques.setObjects(objectRepository.findAll().stream().map(o -> o.getFilial().getName() + ' ' + o.getName()).collect(Collectors.toList()));
        return uniques;
    }

    public List<EquipmentDataDTO> getAllByObjectId(Integer id) {
        Optional<Obj> objOpt = objectRepository.findById(id);
        if (objOpt.isPresent()) {
            List<EquipmentDataDTO> responseDTOList = new ArrayList<>();
            for (Equipment equipment : equipmentRepository.findByObj(objOpt.get())) {
                EquipmentDataDTO tmp = new EquipmentDataDTO();
                tmp.setId(equipment.getId());
                tmp.setName(equipment.getName());
                tmp.setCode(equipment.getCode());
                tmp.setObj(equipment.getObj().getId());
                tmp.setObjName(equipment.getObj().getName());
                tmp.setFilialAndObj(equipment.getObj().getFilial().getName() + ' ' + equipment.getObj().getName());
                responseDTOList.add(tmp);
            }
            return responseDTOList;
        }
        else {
            throw new CustomException("Объекта с таким id не существует", HttpStatus.BAD_REQUEST);
        }
    }
}
