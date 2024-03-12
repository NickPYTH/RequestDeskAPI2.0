package de401t.controller;


import de401t.dto.LoadEquipmentsDTO;
import de401t.dto.ReportDataDTO;
import de401t.dto.TaskDataDTO;
import de401t.dto.TaskGridDataDTO;
import de401t.service.TaskService;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/api/tasks")
@Api(tags = "tasks")
@RequiredArgsConstructor
@Slf4j
public class TaskController {

    private final TaskService taskService;

    @CrossOrigin(origins = "*")
    @PostMapping("/create")
    @PreAuthorize("hasAuthority('admin') or hasAuthority('client') or hasAuthority('executor')")
    @ApiOperation(value = "${FilialController.create}")
    @ApiResponses(value = {//
            @ApiResponse(code = 400, message = "Что-то пошло не так"), //
            @ApiResponse(code = 403, message = "Доступ ограничен"), //
            @ApiResponse(code = 422, message = "Филиал с таким наименованием/кодом уже существует"), //
            @ApiResponse(code = 500, message = "Внутренняя ошибка сервера")
    })
    public String create(@ModelAttribute TaskDataDTO taskDataDTO, HttpServletRequest req) throws IOException, MessagingException, InvalidFormatException {
        return taskService.create(taskDataDTO, req, log);
    }

    @CrossOrigin(origins = "*")
    @PostMapping("/createByAdmin")
    @PreAuthorize("hasAuthority('admin')")
    @ApiOperation(value = "${FilialController.create}")
    @ApiResponses(value = {//
            @ApiResponse(code = 400, message = "Что-то пошло не так"), //
            @ApiResponse(code = 403, message = "Доступ ограничен"), //
            @ApiResponse(code = 422, message = "Филиал с таким наименованием/кодом уже существует"), //
            @ApiResponse(code = 500, message = "Внутренняя ошибка сервера")
    })
    public String createByAdmin(@ModelAttribute TaskDataDTO taskDataDTO) throws IOException, MessagingException, InvalidFormatException {
        return taskService.createByAdmin(taskDataDTO);
    }

    @CrossOrigin(origins = "*")
    @PostMapping("/update")
    @PreAuthorize("hasAuthority('admin') or hasAuthority('client')")
    @ApiOperation(value = "${FilialController.update}")
    @ApiResponses(value = {//
            @ApiResponse(code = 400, message = "Что-то пошло не так"), //
            @ApiResponse(code = 403, message = "Доступ ограничен"), //
            @ApiResponse(code = 422, message = "Филиал с таким наименованием/кодом уже существует"), //
            @ApiResponse(code = 500, message = "Внутренняя ошибка сервера")
    })
    public String update(@ModelAttribute TaskDataDTO taskDataDTO) throws IOException {
        return taskService.update(taskDataDTO);
    }

    @CrossOrigin(origins = "*")
    @PostMapping("/updateStatus/{taskId}/{status}")
    @PreAuthorize("hasAuthority('admin') or hasAuthority('executor')")
    @ApiOperation(value = "${FilialController.updateStatus}")
    @ApiResponses(value = {//
            @ApiResponse(code = 400, message = "Что-то пошло не так"), //
            @ApiResponse(code = 403, message = "Доступ ограничен"), //
            @ApiResponse(code = 422, message = "Филиал с таким наименованием/кодом уже существует"), //
            @ApiResponse(code = 500, message = "Внутренняя ошибка сервера")
    })
    public String updateStatus(@PathVariable Integer taskId, @PathVariable String status) {
        return taskService.updateStatus(taskId, status);
    }

    @CrossOrigin(origins = "*")
    @PostMapping("/updateStatus/{taskId}/{status}/{date}")
    @PreAuthorize("hasAuthority('admin') or hasAuthority('executor')")
    @ApiOperation(value = "${FilialController.updateStatus}")
    @ApiResponses(value = {//
            @ApiResponse(code = 400, message = "Что-то пошло не так"), //
            @ApiResponse(code = 403, message = "Доступ ограничен"), //
            @ApiResponse(code = 422, message = "Филиал с таким наименованием/кодом уже существует"), //
            @ApiResponse(code = 500, message = "Внутренняя ошибка сервера")
    })
    public String updateStatusHui(@PathVariable Integer taskId, @PathVariable String status, @PathVariable String date) {
        return taskService.updateStatus(taskId, status, date);
    }

    @CrossOrigin(origins = "*")
    @GetMapping("/get/{id}")
    @PreAuthorize("hasAuthority('admin') or hasAuthority('client') or hasAuthority('executor')")
    @ApiOperation(value = "${TaskController.getById}")
    @ApiResponses(value = {//
            @ApiResponse(code = 400, message = "Что-то пошло не так"), //
            @ApiResponse(code = 403, message = "Доступ ограничен"), //
            @ApiResponse(code = 422, message = "Филиал с таким наименованием/кодом уже существует"), //
            @ApiResponse(code = 500, message = "Внутренняя ошибка сервера")
    })
    public TaskDataDTO getById(@ApiParam("id") @PathVariable Integer id) throws IOException {
        return taskService.getById(id);
    }

    @CrossOrigin(origins = "*")
    @GetMapping("/my")
    @PreAuthorize("hasAuthority('admin') or hasAuthority('client') or hasAuthority('executor')")
    @ApiOperation(value = "${TaskController.my}")
    @ApiResponses(value = {//
            @ApiResponse(code = 400, message = "Что-то пошло не так"), //
            @ApiResponse(code = 403, message = "Доступ ограничен"), //
            @ApiResponse(code = 422, message = "Филиал с таким наименованием/кодом уже существует"), //
            @ApiResponse(code = 500, message = "Внутренняя ошибка сервера")
    })
    public List<HashMap<String, String>> getUserTasks(HttpServletRequest req) {
        return taskService.getUserTasks(req);
    }

    @CrossOrigin(origins = "*")
    @GetMapping("/all")
    @PreAuthorize("hasAuthority('admin')")
    @ApiOperation(value = "${TaskController.all}")
    @ApiResponses(value = {//
            @ApiResponse(code = 400, message = "Что-то пошло не так"), //
            @ApiResponse(code = 403, message = "Доступ ограничен"), //
            @ApiResponse(code = 422, message = "Филиал с таким наименованием/кодом уже существует"), //
            @ApiResponse(code = 500, message = "Внутренняя ошибка сервера")
    })
    public List<TaskGridDataDTO> getAllTasks() throws MessagingException, FileNotFoundException {
        return taskService.getAll();
    }

    @CrossOrigin(origins = "*")
    @GetMapping("/getPhotoById/{id}")
    @ApiOperation(value = "${TaskController.getImageById}")
    @ApiResponses(value = {//
            @ApiResponse(code = 400, message = "Что-то пошло не так"), //
            @ApiResponse(code = 403, message = "Доступ ограничен"), //
            @ApiResponse(code = 500, message = "Внутренняя ошибка сервера")
    })
    public @ResponseBody ResponseEntity<InputStreamResource> getImage(@ApiParam("id") @PathVariable Integer id) throws IOException {
        return taskService.getPhotoById(id);
    }

    @CrossOrigin(origins = "*")
    @GetMapping("/getDocumentByTaskId/{id}")
    @ApiOperation(value = "${TaskController.getDocumentByTaskId}")
    @ApiResponses(value = {//
            @ApiResponse(code = 400, message = "Что-то пошло не так"), //
            @ApiResponse(code = 403, message = "Доступ ограничен"), //
            @ApiResponse(code = 500, message = "Внутренняя ошибка сервера")
    })
    public @ResponseBody ResponseEntity<InputStreamResource> getDocumentByTaskId(@ApiParam("id") @PathVariable Integer id) throws IOException {
        return taskService.getDocumentByTaskId(id);
    }

    @CrossOrigin(origins = "*")
    @PostMapping("/loadEquipmentsFromExcel")
    @PreAuthorize("hasAuthority('admin')")
    @ApiOperation(value = "${TaskController.loadEquipmentsFromExcel}")
    @ApiResponses(value = {//
            @ApiResponse(code = 400, message = "Что-то пошло не так"), //
            @ApiResponse(code = 403, message = "Доступ ограничен"), //
            @ApiResponse(code = 500, message = "Внутренняя ошибка сервера")
    })
    public String loadEquipmentsFromExcel(@ModelAttribute LoadEquipmentsDTO loadEquipmentsDTO, HttpServletRequest req) throws IOException {
        return taskService.loadEquipmentsFromExcel(loadEquipmentsDTO, req);
    }

    @CrossOrigin(origins = "*")
    @GetMapping("/allExcel")
    @PreAuthorize("hasAuthority('admin') or hasAuthority('executor')")
    @ApiOperation(value = "${TaskController.allExcel}")
    @ApiResponses(value = {//
            @ApiResponse(code = 400, message = "Что-то пошло не так"), //
            @ApiResponse(code = 403, message = "Доступ ограничен"), //
            @ApiResponse(code = 500, message = "Внутренняя ошибка сервера")
    })
    public @ResponseBody ResponseEntity<InputStreamResource> getAllTasksToExcel(HttpServletRequest req) throws IOException {
        return taskService.getAllToExcel(req);
    }

    @CrossOrigin(origins = "*")
    @GetMapping("/report")
    //@PreAuthorize("hasAuthority('admin')")
    @ApiOperation(value = "${TaskController.report}")
    @ApiResponses(value = {//
            @ApiResponse(code = 400, message = "Что-то пошло не так"), //
            @ApiResponse(code = 403, message = "Доступ ограничен"), //
            @ApiResponse(code = 422, message = "Филиал с таким наименованием/кодом уже существует"), //
            @ApiResponse(code = 500, message = "Внутренняя ошибка сервера")
    })
    public List<ReportDataDTO> getReport() {
        return taskService.getReport();
    }

    @CrossOrigin(origins = "*")
    @GetMapping("/report/{filial}")
    //@PreAuthorize("hasAuthority('admin')")
    @ApiOperation(value = "${TaskController.report}")
    @ApiResponses(value = {//
            @ApiResponse(code = 400, message = "Что-то пошло не так"), //
            @ApiResponse(code = 403, message = "Доступ ограничен"), //
            @ApiResponse(code = 422, message = "Филиал с таким наименованием/кодом уже существует"), //
            @ApiResponse(code = 500, message = "Внутренняя ошибка сервера")
    })
    public HashMap<String, Object> getReportObjects(@ApiParam("id") @PathVariable String filial) {
        return taskService.getReportObjects(filial);
    }
}
