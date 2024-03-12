package de401t.controller;


import de401t.dto.EquipmentDataDTO;
import de401t.dto.EquipmentUniquesDataDTO;
import de401t.service.EquipmentService;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/equipments")
@CrossOrigin(origins = "*")
@Api(tags = "equipments")
@RequiredArgsConstructor
public class EquipmentController {

    private final EquipmentService equipmentService;

    @CrossOrigin(origins = "*")
    @PostMapping("/create")
    @PreAuthorize("hasAuthority('admin')")
    @ApiOperation(value = "${EquipmentController.create}")
    @ApiResponses(value = {//
            @ApiResponse(code = 400, message = "Что-то пошло не так"), //
            @ApiResponse(code = 403, message = "Доступ ограничен"), //
            @ApiResponse(code = 422, message = "Оборудование с таким наименованием уже существует"), //
            @ApiResponse(code = 500, message = "Внутренняя ошибка сервера")
    })
    public EquipmentDataDTO create(@ApiParam("Create equipment") @RequestBody EquipmentDataDTO equipmentDataDTO) {
        return equipmentService.create(equipmentDataDTO);
    }

    @CrossOrigin(origins = "*")
    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('admin')")
    @ApiOperation(value = "${EquipmentController.delete}")
    @ApiResponses(value = {//
            @ApiResponse(code = 400, message = "Что-то пошло не так"), //
            @ApiResponse(code = 403, message = "Доступ ограничен"), //
            @ApiResponse(code = 404, message = "Оборудование не существует"), //
            @ApiResponse(code = 500, message = "Внутренняя ошибка сервера")})
    public Integer delete(@ApiParam("id") @PathVariable Integer id) {
        return equipmentService.delete(id);
    }

    @CrossOrigin(origins = "*")
    @PostMapping("/update")
    @PreAuthorize("hasAuthority('admin')")
    @ApiOperation(value = "${EquipmentController.update}")
    @ApiResponses(value = {//
            @ApiResponse(code = 400, message = "Что-то пошло не так"), //
            @ApiResponse(code = 403, message = "Доступ ограничен"), //
            @ApiResponse(code = 404, message = "Оборудование не существует"), //
            @ApiResponse(code = 500, message = "Внутренняя ошибка сервера")
    })
    public EquipmentDataDTO update(@ApiParam("Equipment object") @RequestBody EquipmentDataDTO equipmentDataDTO) {
        return equipmentService.update(equipmentDataDTO);
    }

    @CrossOrigin(origins = "*")
    @GetMapping("/get/{id}")
    @ApiOperation(value = "${EquipmentController.get}")
    @ApiResponses(value = {//
            @ApiResponse(code = 400, message = "Что-то пошло не так"), //
            @ApiResponse(code = 403, message = "Доступ ограничен"), //
            @ApiResponse(code = 404, message = "Оборудование не существует"), //
            @ApiResponse(code = 500, message = "Внутренняя ошибка сервера")
    })
    public EquipmentDataDTO get(@ApiParam("id") @PathVariable Integer id) {
        return equipmentService.get(id);
    }

    @CrossOrigin(origins = "*")
    @GetMapping("/all")
    @ApiOperation(value = "${EquipmentController.getAll}")
    @PreAuthorize("hasAuthority('admin')")
    @ApiResponses(value = {//
            @ApiResponse(code = 400, message = "Что-то пошло не так"), //
            @ApiResponse(code = 403, message = "Доступ ограничен"), //
            @ApiResponse(code = 500, message = "Внутренняя ошибка сервера")
    })
    public List<EquipmentDataDTO> getAll() {
        return equipmentService.getAll();
    }

    @CrossOrigin(origins = "*")
    @GetMapping("/allUniqValues")
    @ApiOperation(value = "${EquipmentController.getAllUniqValues}")
    @PreAuthorize("hasAuthority('admin')")
    @ApiResponses(value = {//
            @ApiResponse(code = 400, message = "Что-то пошло не так"), //
            @ApiResponse(code = 403, message = "Доступ ограничен"), //
            @ApiResponse(code = 500, message = "Внутренняя ошибка сервера")
    })
    public EquipmentUniquesDataDTO getAllUniqValues() {
        return equipmentService.getAllUniqValues();
    }

    @CrossOrigin(origins = "*")
    @GetMapping("/allByObjectId/{id}")
    @ApiOperation(value = "${EquipmentController.allByObjectId}")
    @PreAuthorize("hasAuthority('admin')")
    @ApiResponses(value = {//
            @ApiResponse(code = 400, message = "Что-то пошло не так"), //
            @ApiResponse(code = 403, message = "Доступ ограничен"), //
            @ApiResponse(code = 500, message = "Внутренняя ошибка сервера")
    })
    public List<EquipmentDataDTO> getAllByObjectId(@ApiParam("id") @PathVariable Integer id) {
        return equipmentService.getAllByObjectId(id);
    }

}