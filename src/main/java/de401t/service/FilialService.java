package de401t.service;

import de401t.dto.FilialResponseDTO;
import de401t.dto.ObjectResponseDTO;
import de401t.exception.CustomException;
import de401t.model.Filial;
import de401t.model.Obj;
import de401t.repository.FilialRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FilialService {

    private final FilialRepository filialRepository;

    public Filial create(Filial filial) {
        if (!filialRepository.existsByName(filial.getName())) {
            if (!filialRepository.existsByCode(filial.getCode())) {
                filialRepository.save(filial);
                return filial;
            }
            else
                throw new CustomException("Филиал с таким кодом уже существует", HttpStatus.UNPROCESSABLE_ENTITY);
        } else
            throw new CustomException("Филиал с таким наименованием уже существует", HttpStatus.UNPROCESSABLE_ENTITY);
    }

    public Integer delete(Integer id) {
        if (filialRepository.findById(id).isPresent()) {
            try {
                filialRepository.deleteById(id);
                return id;
            }
            catch (Exception e){
                throw new CustomException("Филиал привязан к существующеум объекту", HttpStatus.BAD_REQUEST);
            }

        } else {
            throw new CustomException("Филиал не существует", HttpStatus.NOT_FOUND);
        }
    }

    public Filial update(Filial filial) {
        if (filialRepository.findById(filial.getId()).isPresent()) {
            filialRepository.save(filial);
            return filial;
        } else {
            throw new CustomException("Филиал с таким id не существует", HttpStatus.NOT_FOUND);
        }
    }

    public FilialResponseDTO get(Integer id) {
        Optional<Filial> filialOpt = filialRepository.findById(id);
        if (filialOpt.isPresent()) {
            FilialResponseDTO responseDTO = new FilialResponseDTO();
            responseDTO.setId(filialOpt.get().getId());
            responseDTO.setName(filialOpt.get().getName());
            responseDTO.setCode(filialOpt.get().getCode());
            return responseDTO;
        } else {
            throw new CustomException("Филиал с таким id не существует", HttpStatus.NOT_FOUND);
        }
    }

    public List<FilialResponseDTO> getAll() {
        List<FilialResponseDTO> responseDTOList = new ArrayList<>();
        for (Filial filial: filialRepository.findAll()){
            FilialResponseDTO tmp = new FilialResponseDTO();
            tmp.setId(filial.getId());
            tmp.setName(filial.getName());
            tmp.setCode(filial.getCode());
            responseDTOList.add(tmp);
        }
        return responseDTOList;
    }
}
