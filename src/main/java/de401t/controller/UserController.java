package de401t.controller;

import de401t.dto.UserDataDTO;
import de401t.service.UserService;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/users")
@Api(tags = "users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final ModelMapper modelMapper;

    @CrossOrigin(origins = "*")
    @PostMapping("/signin")
    @ApiOperation(value = "${UserController.signin}")
    @ApiResponses(value = {//
            @ApiResponse(code = 400, message = "Что-то пошло не так"), //
            @ApiResponse(code = 422, message = "Пароль или имя пользователя введены не верно")})
    public HashMap<String, String> signin(
            @ApiParam("Username") @RequestParam String username, //
            @ApiParam("Password") @RequestParam String password) {
        return userService.login(username, password);
    }

    @CrossOrigin(origins = "*")
    @PostMapping("/create")
    //@PreAuthorize("hasAuthority('admin')")
    @ApiOperation(value = "${UserController.signup}")
    @ApiResponses(value = {//
            @ApiResponse(code = 400, message = "Что-то пошло не так"), //
            @ApiResponse(code = 403, message = "Доступ огрнаничен"), //
            @ApiResponse(code = 422, message = "Имя пользователя уже занято")})
    public String signup(@ApiParam("Signup User") @RequestBody UserDataDTO userDataDTO) {
        return userService.register(userDataDTO);
    }

    @CrossOrigin(origins = "*")
    @PostMapping("/update")
    @ApiOperation(value = "${UserController.signup}")
    @ApiResponses(value = {//
            @ApiResponse(code = 400, message = "Что-то пошло не так"), //
            @ApiResponse(code = 403, message = "Доступ огрнаничен"), //
            @ApiResponse(code = 422, message = "Имя пользователя уже занято")})
    public String update(@ApiParam("Signup User") @RequestBody UserDataDTO userDataDTO) {
        return userService.update(userDataDTO);
    }

    @CrossOrigin(origins = "*")
    @DeleteMapping(value = "/{username}")
    @ApiOperation(value = "${UserController.delete}", authorizations = {@Authorization(value = "apiKey")})
    @ApiResponses(value = {//
            @ApiResponse(code = 400, message = "Что-то пошло не так"), //
            @ApiResponse(code = 403, message = "Доступ ограничен"), //
            @ApiResponse(code = 404, message = "Пользователь не существует"), //
            @ApiResponse(code = 500, message = "Внутренняя ошибка сервера")})
    public String delete(@ApiParam("Username") @PathVariable String username) {
        return userService.delete(username);
    }

    @CrossOrigin(origins = "*")
    @GetMapping(value = "/{username}")
    @ApiOperation(value = "${UserController.search}", response = UserDataDTO.class, authorizations = {@Authorization(value = "apiKey")})
    @ApiResponses(value = {//
            @ApiResponse(code = 400, message = "Что-то пошло не так"),
            @ApiResponse(code = 403, message = "Доступ ограничен"),
            @ApiResponse(code = 404, message = "Пользователь не существует"),
            @ApiResponse(code = 500, message = "Внутренняя ошибка сервера")})
    public UserDataDTO search(@ApiParam("Username") @PathVariable String username) {
        return modelMapper.map(userService.search(username), UserDataDTO.class);
    }

    @CrossOrigin(origins = "*")
    @GetMapping(value = "/all")
    @ApiOperation(value = "${UserController.getUsers}", response = UserDataDTO.class,
            responseContainer = "List",
            authorizations = {@Authorization(value = "apiKey")})
    public List<UserDataDTO> getUsers() {
        return userService.getUsers();
    }

    @CrossOrigin(origins = "*")
    @GetMapping(value = "/me")
    @ApiOperation(value = "${UserController.me}", response = UserDataDTO.class, authorizations = {@Authorization(value = "apiKey")})
    @ApiResponses(value = {//
            @ApiResponse(code = 400, message = "Что-то пошло не так"),
            @ApiResponse(code = 403, message = "Доступ ограничен"),
            @ApiResponse(code = 500, message = "Внутренняя ошибка сервера")})
    public UserDataDTO whoami(HttpServletRequest req) {
        return modelMapper.map(userService.whoami(req), UserDataDTO.class);
    }

    @CrossOrigin(origins = "*")
    @GetMapping("/verify")
    public HashMap<String, String> verify() {
        HashMap<String, String> response = new HashMap<>();
        response.put("status", "ok");
        return response;
    }

}
