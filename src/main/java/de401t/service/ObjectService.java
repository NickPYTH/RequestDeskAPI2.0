package de401t.service;

import de401t.dto.*;
import de401t.exception.CustomException;
import de401t.model.Equipment;
import de401t.model.Filial;
import de401t.model.Obj;
import de401t.model.User;
import de401t.repository.EquipmentRepository;
import de401t.repository.FilialRepository;
import de401t.repository.ObjectRepository;
import de401t.repository.UserRepository;
import de401t.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ObjectService {

    private final ObjectRepository objectRepository;
    private final EquipmentRepository equipmentRepository;
    private final FilialRepository filialRepository;
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;

    public Obj create(ObjectDataDTO objectDataDTO) {
        ModelMapper modelMapper = new ModelMapper();
        Obj object = modelMapper.map(objectDataDTO, Obj.class);
        if (!objectRepository.existsByNameAndFilial(object.getName(), object.getFilial())) {
            objectRepository.save(object);
            return object;
        } else {
            throw new CustomException("Объект с таким наименованием уже существует", HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    public Integer delete(Integer id) {
        if (objectRepository.findById(id).isPresent()) {
            objectRepository.deleteById(id);
            return id;
        } else {
            throw new CustomException("Объект не существует", HttpStatus.NOT_FOUND);
        }
    }

    public ObjectDataDTO update(ObjectDataDTO objectDataDTO) {
        Optional<Obj> objectOpt = objectRepository.findById(objectDataDTO.getId());
        if (objectOpt.isPresent()) {
            Obj object = objectOpt.get();
            object.setName(objectDataDTO.getName());
            ModelMapper modelMapper = new ModelMapper();
            Filial filial = modelMapper.map(objectDataDTO.getFilial(), Filial.class);
            object.setFilial(filial);
            objectRepository.save(object);
            TypeMap<Obj, ObjectDataDTO> propertyMapper = modelMapper.createTypeMap(Obj.class, ObjectDataDTO.class);
            propertyMapper.addMappings(mapper -> mapper.skip(ObjectDataDTO::setEquipments));
            return modelMapper.map(object, ObjectDataDTO.class);
        } else {
            throw new CustomException("Объект с таким id не существует", HttpStatus.NOT_FOUND);
        }
    }

    public ObjectResponseDTO get(Integer id) {
        ModelMapper modelMapper = new ModelMapper();
        Optional<Obj> objectOpt = objectRepository.findById(id);
        if (objectOpt.isPresent()) {
            ObjectResponseDTO responseDTO = new ObjectResponseDTO();
            responseDTO.setId(objectOpt.get().getId());
            responseDTO.setName(objectOpt.get().getName());
            FilialDataDTO filialDataDTO = modelMapper.map(objectOpt.get().getFilial(), FilialDataDTO.class);
            responseDTO.setFilial(filialDataDTO);
            return responseDTO;
        } else {
            throw new CustomException("Объект с таким id не существует", HttpStatus.NOT_FOUND);
        }
    }

    public List<ObjectResponseDTO> getAll() {
        ModelMapper modelMapper = new ModelMapper();
        TypeMap<Equipment, EquipmentDataDTO> propertyMapper = modelMapper.createTypeMap(Equipment.class, EquipmentDataDTO.class);
        propertyMapper.addMappings(mapper -> mapper.skip(EquipmentDataDTO::setObj));
        List<ObjectResponseDTO> responseDTOList = new ArrayList<>();
        for (Obj object: objectRepository.findAll()){
            ObjectResponseDTO tmp = new ObjectResponseDTO();
            tmp.setId(object.getId());
            tmp.setName(object.getName());
            FilialDataDTO filialDataDTO = modelMapper.map(object.getFilial(), FilialDataDTO.class);
            tmp.setFilial(filialDataDTO);

            List<EquipmentDataDTO> equipments = new ArrayList<>();
            for (Equipment equipment: equipmentRepository.findByObj(object)) {
                EquipmentDataDTO equipmentDataDTO = modelMapper.map(equipment, EquipmentDataDTO.class);
                equipmentDataDTO.setObj(object.getId());
                equipments.add(equipmentDataDTO);
            }
            tmp.setEquipments(equipments);
            responseDTOList.add(tmp);
        }
        return responseDTOList;
    }

    public List<ObjectResponseDTO> getAllNoEquipments() {
        ModelMapper modelMapper = new ModelMapper();
        TypeMap<Equipment, EquipmentDataDTO> propertyMapper = modelMapper.createTypeMap(Equipment.class, EquipmentDataDTO.class);
        propertyMapper.addMappings(mapper -> mapper.skip(EquipmentDataDTO::setObj));
        List<ObjectResponseDTO> responseDTOList = new ArrayList<>();
        for (Obj object: objectRepository.findAll()){
            ObjectResponseDTO tmp = new ObjectResponseDTO();
            tmp.setId(object.getId());
            tmp.setName(object.getName());
            FilialDataDTO filialDataDTO = modelMapper.map(object.getFilial(), FilialDataDTO.class);
            tmp.setFilial(filialDataDTO);
            tmp.setFilialString(object.getFilial().getName());
            responseDTOList.add(tmp);
        }
        return responseDTOList;
    }

    public List<ObjectResponseDTO> getAllMyObjects(HttpServletRequest req) {
        User user = userRepository.findByUsername(jwtTokenProvider.getUsername(jwtTokenProvider.resolveToken(req)));
        ModelMapper modelMapper = new ModelMapper();
        TypeMap<Equipment, EquipmentDataDTO> propertyMapper = modelMapper.createTypeMap(Equipment.class, EquipmentDataDTO.class);
        propertyMapper.addMappings(mapper -> mapper.skip(EquipmentDataDTO::setObj));
        List<ObjectResponseDTO> responseDTOList = new ArrayList<>();
        for (Obj object: user.getObjects()){
            ObjectResponseDTO tmp = new ObjectResponseDTO();
            tmp.setId(object.getId());
            tmp.setName(object.getName());
            FilialDataDTO filialDataDTO = modelMapper.map(object.getFilial(), FilialDataDTO.class);
            tmp.setFilial(filialDataDTO);

            List<EquipmentDataDTO> equipments = new ArrayList<>();
            for (Equipment equipment: equipmentRepository.findByObj(object)) {
                EquipmentDataDTO equipmentDataDTO = modelMapper.map(equipment, EquipmentDataDTO.class);
                equipmentDataDTO.setObj(object.getId());
                equipments.add(equipmentDataDTO);
            }
            tmp.setEquipments(equipments);

            responseDTOList.add(tmp);
        }
        return responseDTOList;
    }
}
