package de401t.controller;


import de401t.dto.EquipmentDataDTO;
import de401t.dto.MessageDataDTO;
import de401t.model.Message;
import de401t.service.EquipmentService;
import de401t.service.MessageService;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/api/messages")
@CrossOrigin(origins = "*")
@Api(tags = "messages")
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;

    @PostMapping("/create")
    @PreAuthorize("hasAuthority('client') or hasAuthority('admin') or hasAuthority('executor')")
    @ApiOperation(value = "${MessageController.create}")
    @ApiResponses(value = {//
            @ApiResponse(code = 400, message = "Что-то пошло не так"), //
            @ApiResponse(code = 403, message = "Доступ ограничен"), //
            @ApiResponse(code = 422, message = "Оборудование с таким наименованием уже существует"), //
            @ApiResponse(code = 500, message = "Внутренняя ошибка сервера")
    })
    public String create(@ApiParam("Create message") @RequestBody MessageDataDTO messageDataDTO, HttpServletRequest req) {
        return messageService.create(messageDataDTO, req);
    }

    @GetMapping("/task/{id}")
    @ApiOperation(value = "${EquipmentController.getAll}")
    @PreAuthorize("hasAuthority('admin') or hasAuthority('client') or hasAuthority('executor')")
    @ApiResponses(value = {//
            @ApiResponse(code = 400, message = "Что-то пошло не так"), //
            @ApiResponse(code = 403, message = "Доступ ограничен"), //
            @ApiResponse(code = 500, message = "Внутренняя ошибка сервера")
    })
    public List<MessageDataDTO> allByTaskAndUser(@ApiParam("Get messages") @PathVariable Integer id, HttpServletRequest req) {
        return messageService.getMyByTask(id, req);
    }

    @GetMapping("/task/super/{id}")
    @ApiOperation(value = "${EquipmentController.getAll}")
    @PreAuthorize("hasAuthority('admin')")
    @ApiResponses(value = {//
            @ApiResponse(code = 400, message = "Что-то пошло не так"), //
            @ApiResponse(code = 403, message = "Доступ ограничен"), //
            @ApiResponse(code = 500, message = "Внутренняя ошибка сервера")
    })
    public List<MessageDataDTO> allByTask(@ApiParam("Get messages") @PathVariable Integer id) {
        return messageService.getByTask(id);
    }

}