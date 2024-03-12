package de401t.controller;


import de401t.dto.FilialDataDTO;
import de401t.dto.FilialResponseDTO;
import de401t.dto.ObjectDataDTO;
import de401t.dto.ObjectResponseDTO;
import de401t.model.Filial;
import de401t.model.Obj;
import de401t.service.FilialService;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/filials")
@Api(tags = "filials")
@RequiredArgsConstructor
public class FilialController {

    private final FilialService filialService;
    private final ModelMapper modelMapper;

    @CrossOrigin(origins = "*")
    @PostMapping("/create")
    @PreAuthorize("hasAuthority('admin')")
    @ApiOperation(value = "${FilialController.create}")
    @ApiResponses(value = {//
            @ApiResponse(code = 400, message = "Что-то пошло не так"), //
            @ApiResponse(code = 403, message = "Доступ ограничен"), //
            @ApiResponse(code = 422, message = "Филиал с таким наименованием/кодом уже существует"), //
            @ApiResponse(code = 500, message = "Внутренняя ошибка сервера")
    })
    public Filial create(@ApiParam("Create filial") @RequestBody FilialDataDTO filial) {
        return filialService.create(modelMapper.map(filial, Filial.class));
    }

    @CrossOrigin(origins = "*")
    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('admin')")
    @ApiOperation(value = "${FilialController.delete}")
    @ApiResponses(value = {//
            @ApiResponse(code = 400, message = "Что-то пошло не так"), //
            @ApiResponse(code = 403, message = "Доступ ограничен"), //
            @ApiResponse(code = 404, message = "Филиал не существует"), //
            @ApiResponse(code = 500, message = "Внутренняя ошибка сервера")})
    public Integer delete(@ApiParam("id") @PathVariable Integer id) {
        filialService.delete(id);
        return id;
    }

    @CrossOrigin(origins = "*")
    @PostMapping("/update")
    @PreAuthorize("hasAuthority('admin')")
    @ApiOperation(value = "${FilialController.update}")
    @ApiResponses(value = {//
            @ApiResponse(code = 400, message = "Что-то пошло не так"), //
            @ApiResponse(code = 403, message = "Доступ ограничен"), //
            @ApiResponse(code = 404, message = "Филиал не существует"), //
            @ApiResponse(code = 500, message = "Внутренняя ошибка сервера")
    })
    public Filial update(@ApiParam("Update filial") @RequestBody FilialDataDTO filial) {
        return filialService.update(modelMapper.map(filial, Filial.class));
    }

    @CrossOrigin(origins = "*")
    @GetMapping("/get/{id}")
    @PreAuthorize("hasAuthority('admin')")
    @ApiOperation(value = "${FilialController.get}")
    @ApiResponses(value = {//
            @ApiResponse(code = 400, message = "Что-то пошло не так"), //
            @ApiResponse(code = 403, message = "Доступ ограничен"), //
            @ApiResponse(code = 404, message = "Филиал не существует"), //
            @ApiResponse(code = 500, message = "Внутренняя ошибка сервера")
    })
    public FilialResponseDTO get(@ApiParam("id") @PathVariable Integer id) {
        return filialService.get(id);
    }

    @CrossOrigin(origins = "*")
    @GetMapping("/all")
    @PreAuthorize("hasAuthority('admin')")
    @ApiOperation(value = "${FilialController.getAll}")
    @ApiResponses(value = {//
            @ApiResponse(code = 400, message = "Что-то пошло не так"), //
            @ApiResponse(code = 403, message = "Доступ ограничен"), //
            @ApiResponse(code = 500, message = "Внутренняя ошибка сервера")
    })
    public List<FilialResponseDTO> getAll() {
        return filialService.getAll();
    }

}
