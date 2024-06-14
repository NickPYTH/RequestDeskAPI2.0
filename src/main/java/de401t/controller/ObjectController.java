package de401t.controller;


import de401t.dto.ObjectDataDTO;
import de401t.dto.ObjectResponseDTO;
import de401t.dto.UserDataDTO;
import de401t.model.Obj;
import de401t.service.ObjectService;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/objects")
@Api(tags = "objects")
@RequiredArgsConstructor
public class ObjectController {

    private final ObjectService objectService;

    @PostMapping("/create")
    @ApiOperation(value = "${ObjectController.create}")
    @ApiResponses(value = {//
            @ApiResponse(code = 400, message = "Что-то пошло не так"), //
            @ApiResponse(code = 403, message = "Доступ ограничен"), //
            @ApiResponse(code = 422, message = "Объект с таким наименованием уже существует"), //
            @ApiResponse(code = 500, message = "Внутренняя ошибка сервера")
            })
    public Obj create(@ApiParam("Create object") @RequestBody ObjectDataDTO objectDataDTO) {
        return objectService.create(objectDataDTO);
    }

    @DeleteMapping("/delete/{id}")
    @ApiOperation(value = "${ObjectController.delete}")
    @ApiResponses(value = {//
            @ApiResponse(code = 400, message = "Что-то пошло не так"), //
            @ApiResponse(code = 403, message = "Доступ ограничен"), //
            @ApiResponse(code = 404, message = "Объект не существует"), //
            @ApiResponse(code = 500, message = "Внутренняя ошибка сервера")})
    public Integer delete(@ApiParam("id") @PathVariable Integer id) {
        return objectService.delete(id);
    }

    @PostMapping("/update")
    @ApiOperation(value = "${ObjectController.update}")
    @ApiResponses(value = {//
            @ApiResponse(code = 400, message = "Что-то пошло не так"), //
            @ApiResponse(code = 403, message = "Доступ ограничен"), //
            @ApiResponse(code = 404, message = "Объект не существует"), //
            @ApiResponse(code = 500, message = "Внутренняя ошибка сервера")
    })
    public ObjectDataDTO update(@ApiParam("Update object") @RequestBody ObjectDataDTO objectDataDTO) {
        return objectService.update(objectDataDTO);
    }

    @GetMapping("/get/{id}")
    @ApiOperation(value = "${ObjectController.get}")
    @ApiResponses(value = {//
            @ApiResponse(code = 400, message = "Что-то пошло не так"), //
            @ApiResponse(code = 403, message = "Доступ ограничен"), //
            @ApiResponse(code = 404, message = "Объект не существует"), //
            @ApiResponse(code = 500, message = "Внутренняя ошибка сервера")
    })
    public ObjectResponseDTO get(@ApiParam("id") @PathVariable Integer id) {
        return objectService.get(id);
    }

    @CrossOrigin(origins = "*")
    @GetMapping("/all")
    @ApiOperation(value = "${ObjectController.getAll}")
    @ApiResponses(value = {//
            @ApiResponse(code = 400, message = "Что-то пошло не так"), //
            @ApiResponse(code = 403, message = "Доступ ограничен"), //
            @ApiResponse(code = 500, message = "Внутренняя ошибка сервера")
    })
    public List<ObjectResponseDTO> getAll() {
        return objectService.getAll();
    }

    @CrossOrigin(origins = "*")
    @GetMapping("/allNoEquipments")
    @ApiOperation(value = "${ObjectController.getAll}")
    @ApiResponses(value = {//
            @ApiResponse(code = 400, message = "Что-то пошло не так"), //
            @ApiResponse(code = 403, message = "Доступ ограничен"), //
            @ApiResponse(code = 500, message = "Внутренняя ошибка сервера")
    })
    public List<ObjectResponseDTO> getAllNoEquipments() {
        return objectService.getAllNoEquipments();
    }

    @CrossOrigin(origins = "*")
    @GetMapping(value = "/my")
    @ApiResponses(value = {//
            @ApiResponse(code = 400, message = "Что-то пошло не так"),
            @ApiResponse(code = 403, message = "Доступ ограничен"),
            @ApiResponse(code = 500, message = "Внутренняя ошибка сервера")})
    public List<ObjectResponseDTO> getAllMyObjects(HttpServletRequest req) {
        return objectService.getAllMyObjects(req);
    }
}
