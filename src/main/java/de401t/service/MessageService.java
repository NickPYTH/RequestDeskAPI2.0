package de401t.service;

import de401t.dto.MessageDataDTO;
import de401t.exception.CustomException;
import de401t.model.Message;
import de401t.model.Role;
import de401t.model.Task;
import de401t.model.User;
import de401t.repository.*;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.*;


@Service
@RequiredArgsConstructor
public class MessageService {

    private final UserService userService;
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final MessageRepository messageRepository;

    // 2021-03-24 16:48:05
    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public String create(MessageDataDTO messageDataDTO, HttpServletRequest req) {
        User userSender = userService.whoami(req);
        Set<Role> roles = Set.copyOf(roleRepository.findAllByCode(2));
        User userReceiver = userRepository.findByRolesIn(roles);
        Task task = taskRepository.getById(messageDataDTO.getTaskId());
        Message message = new Message();
        message.setMessage(messageDataDTO.getMessage());
        Timestamp currentDate = new Timestamp(System.currentTimeMillis());
        message.setDate(currentDate);
        message.setTask(task);
        message.setUserFrom(userSender);
        message.setUserTo(userReceiver);
        messageRepository.save(message);
        throw new CustomException("Сообщение отправлено", HttpStatus.CREATED);
    }

    public List<MessageDataDTO> getMyByTask(Integer id, HttpServletRequest req) {
        User userSender = userService.whoami(req);
        Task task = taskRepository.getById(id);
        List<MessageDataDTO> messages = new ArrayList<>();
        for (Message message : messageRepository.getAllByTask(task)) {
            MessageDataDTO messageDataDTO = new MessageDataDTO();
            messageDataDTO.setMessage(message.getMessage());
            messageDataDTO.setDate(sdf.format(message.getDate()));
            messageDataDTO.setFromMe(Objects.equals(message.getUserFrom().getId(), userSender.getId()));
            messages.add(messageDataDTO);
        }
        return messages;
    }

    public List<MessageDataDTO> getByTask(Integer id) {
        Task task = taskRepository.getById(id);
        List<MessageDataDTO> messages = new ArrayList<>();
        for (Message message : messageRepository.getAllByTask(task)) {
            MessageDataDTO messageDataDTO = new MessageDataDTO();
            messageDataDTO.setMessage(message.getMessage());
            messageDataDTO.setDate(sdf.format(message.getDate()));
            messageDataDTO.setUserFrom(message.getUserFrom().getName());
            messageDataDTO.setUserRole(message.getUserFrom().getRoles().get(0).getName());
            messageDataDTO.setUserTo(message.getUserTo().getName());
            messages.add(messageDataDTO);
        }
        return messages;
    }
}
