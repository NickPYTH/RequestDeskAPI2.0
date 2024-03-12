package de401t.service;

import de401t.dto.UserDataDTO;
import de401t.exception.CustomException;
import de401t.model.Obj;
import de401t.model.Role;
import de401t.model.User;
import de401t.repository.ObjectRepository;
import de401t.repository.RoleRepository;
import de401t.repository.UserRepository;
import de401t.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;
    private final ObjectRepository objectRepository;
    private final RoleRepository roleRepository;

    public HashMap<String, String> login(String username, String password) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
            HashMap<String, String> response = new HashMap<>();
            User user = userRepository.findByUsername(username);
            response.put("token", jwtTokenProvider.createToken(username, user.getRoles()));
            response.put("role", user.getRoles().get(0).getCode().toString());

            return response;
        } catch (AuthenticationException e) {
            throw new CustomException("Invalid username/password supplied", HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    public String register(UserDataDTO userDataDTO) {
        ModelMapper modelMapper = new ModelMapper();
        TypeMap<UserDataDTO, User> propertyMapper = modelMapper.createTypeMap(UserDataDTO.class, User.class);
        propertyMapper.addMappings(mapper -> mapper.skip(User::setObjects));
        propertyMapper.addMappings(mapper -> mapper.skip(User::setRoles));
        User user = modelMapper.map(userDataDTO, User.class);
        if (!userRepository.existsByUsername(user.getUsername())) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            List<Obj> objects = new ArrayList<>();
            for (Integer objectCode : userDataDTO.getObjectsCode())
                objects.add(objectRepository.findById(objectCode).get());
            user.setObjects(objects);
            List<Role> roles = new ArrayList<>();
            roles.add(roleRepository.findByCode(userDataDTO.getRole()));
            user.setRoles(roles);
            userRepository.save(user);
            throw new CustomException("Created", HttpStatus.CREATED);
        } else {
            throw new CustomException("Username is already in use", HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    public String update(UserDataDTO userDataDTO) {
        Optional<User> userOpt = userRepository.findById(userDataDTO.getId());
        if (!userOpt.isPresent())
            throw new CustomException("User does not exist", HttpStatus.UNPROCESSABLE_ENTITY);
        User user = userOpt.get();
        List<Obj> objects = new ArrayList<>();
        for (Integer objectCode : userDataDTO.getObjectsCode())
            objects.add(objectRepository.findById(objectCode).get());
        user.setObjects(objects);
        List<Role> roles = new ArrayList<>();
        roles.add(roleRepository.findByCode(userDataDTO.getRole()));
        user.setRoles(roles);
        user.setName(userDataDTO.getName());
        user.setSurname(userDataDTO.getSurname());
        user.setSecondName(user.getSecondName());
        user.setUsername(userDataDTO.getUsername());
        user.setEmail(userDataDTO.getEmail());
        user.setPhone(user.getPhone());
        if (userDataDTO.getPassword().trim().length() > 0)
            user.setPassword(passwordEncoder.encode(userDataDTO.getPassword()));
        userRepository.save(user);
        throw new CustomException("Updated", HttpStatus.OK);
    }

    public String delete(String username) {
        userRepository.deleteByUsername(username);
        throw new CustomException("User deleted", HttpStatus.OK);
    }

    public User search(String username) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new CustomException("The user doesn't exist", HttpStatus.NOT_FOUND);
        }
        return user;
    }

    public User whoami(HttpServletRequest req) {
        return userRepository.findByUsername(jwtTokenProvider.getUsername(jwtTokenProvider.resolveToken(req)));
    }

    public List<UserDataDTO> getUsers() {
        ModelMapper modelMapper = new ModelMapper();
        TypeMap<User, UserDataDTO> propertyMapper = modelMapper.createTypeMap(User.class, UserDataDTO.class);
        propertyMapper.addMappings(mapper -> mapper.skip(UserDataDTO::setObjects));
        propertyMapper.addMappings(mapper -> mapper.skip(UserDataDTO::setPassword));
        List<UserDataDTO> response = new ArrayList<>();
        for (User user : userRepository.findAll()) {
            UserDataDTO userDataDTO = modelMapper.map(user, UserDataDTO.class);
            List<HashMap<String, String>> objects = new ArrayList<>();
            String objectsString = "";
            for (Obj object : user.getObjects()) {
                HashMap<String, String> pair = new HashMap<>();
                pair.put("id", object.getId().toString());
                pair.put("name", object.getName());
                pair.put("filialAndObj", object.getFilial().getName() + ' ' + object.getName());
                objectsString += object.getFilial().getName() + ' ' + object.getName() + ", ";
                objects.add(pair);
            }
            userDataDTO.setObjects(objects);
            userDataDTO.setObjectsString(objectsString);
            Optional<Role> roleOpt = user.getRoles().stream().findFirst();
            roleOpt.ifPresent(role -> userDataDTO.setRole(role.getCode()));
            roleOpt.ifPresent(role -> userDataDTO.setRoleString(role.getNameRu()));
            response.add(userDataDTO);
        }
        return response;
    }

}
