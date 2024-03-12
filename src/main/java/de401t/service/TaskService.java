package de401t.service;

import de401t.dto.LoadEquipmentsDTO;
import de401t.dto.ReportDataDTO;
import de401t.dto.TaskDataDTO;
import de401t.dto.TaskGridDataDTO;
import de401t.exception.CustomException;
import de401t.model.*;
import de401t.repository.*;
import lombok.RequiredArgsConstructor;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final DefaultEmailService defaultEmailService;
    private final UserService userService;
    private final TaskRepository taskRepository;
    private final PhotoRepository photoRepository;
    private final EquipmentRepository equipmentRepository;
    private final StatusRepository statusRepository;
    private final RoleRepository roleRepository;
    private final FilialRepository filialRepository;
    private final ObjectRepository objectRepository;
    private final UserRepository userRepository;
    private final DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

    public LocalDateTime convertToLocalDateTimeViaInstant(Date dateToConvert) {
        return dateToConvert.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
    }

    public String create(TaskDataDTO taskDataDTO, HttpServletRequest req, Logger log) throws IOException, MessagingException, InvalidFormatException {
        Task task = new Task();
        task.setName(taskDataDTO.getTitle());
        task.setDescription(taskDataDTO.getDescription());
        Date date = new Date();
        task.setDate(date);
        String[] equipmentCode = taskDataDTO.getEquipment().split("#");
        try {
            Obj obj = objectRepository.getById(Integer.valueOf(taskDataDTO.getObject()));
            List<Equipment> equipments = equipmentRepository.findByNameAndCodeAndObj(equipmentCode[0], equipmentCode[1], obj);
            task.setEquipment(equipments.get(0));
        } catch (Exception e) {
            List<Equipment> equipments = equipmentRepository.findByNameAndCode(equipmentCode[0], equipmentCode[1]);
            task.setEquipment(equipments.get(0));
        }
        task.setCreator(userService.whoami(req));
        task.setStatus(statusRepository.getByCode(0));
        List<Photo> photos = new ArrayList<>();
        if (taskDataDTO.getPhoto0() != null) {
            Photo photo = new Photo();
            photo.setData(taskDataDTO.getPhoto0().getBytes());
            photo.setName(taskDataDTO.getPhoto0().getName());
            photos.add(photo);
            photoRepository.save(photo);
        }
        if (taskDataDTO.getPhoto1() != null) {
            Photo photo = new Photo();
            photo.setData(taskDataDTO.getPhoto1().getBytes());
            photo.setName(taskDataDTO.getPhoto1().getName());
            photos.add(photo);
            photoRepository.save(photo);
        }
        if (taskDataDTO.getPhoto2() != null) {
            Photo photo = new Photo();
            photo.setData(taskDataDTO.getPhoto2().getBytes());
            photo.setName(taskDataDTO.getPhoto2().getName());
            photos.add(photo);
            photoRepository.save(photo);
        }
        task.setPhotos(photos);
        taskRepository.save(task);
        Thread createDocumentThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    createDocumentByTask(task);
                } catch (IOException | MessagingException | InvalidFormatException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        createDocumentThread.start();

        throw new CustomException("", HttpStatus.CREATED);
    }

    public void createDocumentByTask(Task task) throws IOException, MessagingException, InvalidFormatException {
        XSSFWorkbook workbook = new XSSFWorkbook(new FileInputStream("template.xlsx"));
        XSSFSheet sheet = workbook.getSheetAt(0);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        String formatDateTime = convertToLocalDateTimeViaInstant(task.getDate()).format(formatter);
        Cell taskNumberCell = sheet.getRow(1).getCell(8);
        taskNumberCell.setCellValue(task.getId());
        Cell brokeDateCell = sheet.getRow(6).getCell(4);
        brokeDateCell.setCellValue(formatDateTime);
        Cell taskDateCell = sheet.getRow(6).getCell(8);
        taskDateCell.setCellValue(formatDateTime);
        Cell taskPriorityCell = sheet.getRow(6).getCell(10);
        taskPriorityCell.setCellValue("Нормальный");
        Cell fioCell = sheet.getRow(8).getCell(8);
        fioCell.setCellValue(task.getCreator().getSurname() + ' ' + task.getCreator().getName() + ' ' + task.getCreator().getSecondName());
        Cell phoneCell = sheet.getRow(8).getCell(9);
        phoneCell.setCellValue(task.getCreator().getPhone());
        Cell emailCell = sheet.getRow(8).getCell(10);
        emailCell.setCellValue(task.getCreator().getEmail());
        Cell equipmentNameCell = sheet.getRow(9).getCell(8);
        equipmentNameCell.setCellValue(task.getEquipment().getName());
        Cell equipmenCodeCell = sheet.getRow(11).getCell(8);
        equipmenCodeCell.setCellValue(task.getEquipment().getCode());
        Cell equipmenLocationCell = sheet.getRow(12).getCell(8);
        equipmenLocationCell.setCellValue(task.getEquipment().getObj().getFilial().getName() + ' ' + task.getEquipment().getObj().getName());
        Cell taskTitleCell = sheet.getRow(13).getCell(8);
        taskTitleCell.setCellValue(task.getName());
        if (task.getDescription() != null) {
            Cell taskDescriptionCell = sheet.getRow(14).getCell(8);
            taskDescriptionCell.setCellValue(task.getDescription());
        }

        // Crating output stream and writing the updated workbook
        FileOutputStream os = new FileOutputStream("/root/result.xlsx");
        workbook.write(os);
        // Close the workbook and output stream
        workbook.close();
        os.close();
        FileInputStream a = new FileInputStream("/root/result.xlsx");
        task.setDocument(a.readAllBytes());
        taskRepository.save(task);
        //defaultEmailService.sendEmailWithAttachment(userRepository.findByUsername("admin").getEmail(), "Заявка " + task.getEquipment().getObj().getFilial().getName() + " " + task.getEquipment().getObj().getName(), task.getEquipment().getObj().getFilial().getName() + " " + task.getEquipment().getObj().getName(), "result.xlsx");
    }

    public String createByAdmin(TaskDataDTO taskDataDTO) throws IOException, MessagingException, InvalidFormatException {
        Task task = new Task();
        task.setName(taskDataDTO.getTitle());
        task.setDescription(taskDataDTO.getDescription());
        Date currentDate = Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant());

        Date date = new Date();
        System.out.println(dateFormat.format(date));
        task.setDate(currentDate);
        String[] equipmentCode = taskDataDTO.getEquipment().split("#");
        Obj obj = objectRepository.getById(Integer.valueOf(taskDataDTO.getObject()));
        List<Equipment> equipments = equipmentRepository.findByNameAndCodeAndObj(equipmentCode[0], equipmentCode[1], obj);
        task.setEquipment(equipments.get(0));
        task.setCreator(userRepository.getById(taskDataDTO.getCreator()));
        task.setStatus(statusRepository.getByCode(0));
        List<Photo> photos = new ArrayList<>();
        if (taskDataDTO.getPhoto0() != null) {
            Photo photo = new Photo();
            photo.setData(taskDataDTO.getPhoto0().getBytes());
            photo.setName(taskDataDTO.getPhoto0().getName());
            photos.add(photo);
            photoRepository.save(photo);
        }
        if (taskDataDTO.getPhoto1() != null) {
            Photo photo = new Photo();
            photo.setData(taskDataDTO.getPhoto1().getBytes());
            photo.setName(taskDataDTO.getPhoto1().getName());
            photos.add(photo);
            photoRepository.save(photo);
        }
        if (taskDataDTO.getPhoto2() != null) {
            Photo photo = new Photo();
            photo.setData(taskDataDTO.getPhoto2().getBytes());
            photo.setName(taskDataDTO.getPhoto2().getName());
            photos.add(photo);
            photoRepository.save(photo);
        }
        task.setPhotos(photos);
        taskRepository.save(task);
        try {
            createDocumentByTask(task);
        } catch (Exception e) {

        }
        throw new CustomException("", HttpStatus.CREATED);
    }

    public String update(TaskDataDTO taskDataDTO) throws IOException {
        Task task = taskRepository.getById(taskDataDTO.getId());
        task.setName(taskDataDTO.getTitle());
        task.setDescription(taskDataDTO.getDescription());
        String[] equipmentCode = taskDataDTO.getEquipment().split("#");
        try {
            Obj obj = objectRepository.getById(Integer.valueOf(taskDataDTO.getObject()));
            List<Equipment> equipments = equipmentRepository.findByNameAndCodeAndObj(equipmentCode[0], equipmentCode[1], obj);
            task.setEquipment(equipments.get(0));
        } catch (Exception e) {
            List<Equipment> equipments = equipmentRepository.findByNameAndCode(equipmentCode[0], equipmentCode[1]);
            task.setEquipment(equipments.get(0));
        }
        List<Photo> photos = task.getPhotos();
        List<Photo> newPhotos = new ArrayList<>();
        String[] oldPhotosIds = taskDataDTO.getOldPhotosIds().split(",");
        photos.stream().filter(photo -> Arrays.asList(oldPhotosIds).contains(photo.getId().toString())).forEach(p -> {
            newPhotos.add(p);
        });
        if (taskDataDTO.getPhoto0() != null) {
            Photo photo = new Photo();
            photo.setData(taskDataDTO.getPhoto0().getBytes());
            photo.setName(taskDataDTO.getPhoto0().getName());
            newPhotos.add(photo);
            photoRepository.save(photo);
        }
        if (taskDataDTO.getPhoto1() != null) {
            Photo photo = new Photo();
            photo.setData(taskDataDTO.getPhoto1().getBytes());
            photo.setName(taskDataDTO.getPhoto1().getName());
            newPhotos.add(photo);
            photoRepository.save(photo);
        }
        if (taskDataDTO.getPhoto2() != null) {
            Photo photo = new Photo();
            photo.setData(taskDataDTO.getPhoto2().getBytes());
            photo.setName(taskDataDTO.getPhoto2().getName());
            newPhotos.add(photo);
            photoRepository.save(photo);
        }
        task.setPhotos(newPhotos);
        taskRepository.save(task);
        throw new CustomException("", HttpStatus.OK);
    }

    public TaskDataDTO getById(Integer id) {
        Task task = taskRepository.getById(id);
        TaskDataDTO response = new TaskDataDTO();
        response.setId(task.getId());
        response.setTitle(task.getName());
        response.setDescription(task.getDescription());
        response.setEquipment(task.getEquipment().getName() + "#" + task.getEquipment().getCode());
        response.setStatus(task.getStatus().getName());
        response.setObject(task.getEquipment().getObj().getFilial().getCode() + " " + task.getEquipment().getObj().getFilial().getName() + " " + task.getEquipment().getObj().getName());
        response.setObjectId(task.getEquipment().getObj().getId().toString());
        if (task.getTaskCompleteDate() != null)
            response.setCompletedDate(task.getTaskCompleteDate().toString());
        List<String> photos = new ArrayList<>();
        for (Photo photo : task.getPhotos()) photos.add(photo.getId().toString());
        response.setPhotos(photos);
        return response;
    }

    public List<HashMap<String, String>> getUserTasks(HttpServletRequest req) {
        User user = userService.whoami(req);
        Role adminRole = roleRepository.findByCode(2);
        List<HashMap<String, String>> response = new ArrayList<>();
        if (user.getRoles().contains(adminRole)) { // executor tasks
            for (Task task : taskRepository.findAll()) {
                HashMap<String, String> tmp = new HashMap<>();
                tmp.put("id", task.getId().toString());
                tmp.put("title", task.getName());
                tmp.put("description", task.getDescription());
                tmp.put("date", task.getDate().toString().split(" ")[0]);
                tmp.put("status", task.getStatus().getName());
                tmp.put("equipmentName", task.getEquipment().getName());
                tmp.put("equipmentCode", task.getEquipment().getCode());
                tmp.put("object", task.getEquipment().getObj().getName());
                tmp.put("filial", task.getEquipment().getObj().getFilial().getName());
                response.add(tmp);
            }
            return response;
        } else { // client tasks
            for (Task task : taskRepository.getByCreator(user)) {
                HashMap<String, String> tmp = new HashMap<>();
                tmp.put("id", task.getId().toString());
                tmp.put("title", task.getName());
                tmp.put("description", task.getDescription());
                tmp.put("date", task.getDate().toString().split(" ")[0]);
                tmp.put("status", task.getStatus().getName());
                tmp.put("equipmentName", task.getEquipment().getName());
                tmp.put("equipmentCode", task.getEquipment().getCode());
                tmp.put("object", task.getEquipment().getObj().getName());
                tmp.put("filial", task.getEquipment().getObj().getFilial().getName());
                response.add(tmp);
            }
            return response;
        }
    }

    public List<TaskGridDataDTO> getAll() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        List<TaskGridDataDTO> response = new ArrayList<>();
        for (Task task : taskRepository.findAll()) {
            TaskGridDataDTO taskDTO = new TaskGridDataDTO();
            taskDTO.setId(task.getId());
            taskDTO.setObject(task.getEquipment().getObj().getName());
            taskDTO.setFilialAndObject(task.getEquipment().getObj().getFilial().getName() + ' ' + task.getEquipment().getObj().getName());
            taskDTO.setEquipment(task.getEquipment().getName() + '#' + task.getEquipment().getCode());
            taskDTO.setCreator(task.getCreator().getSurname() + " " + task.getCreator().getName() + " " + task.getCreator().getSecondName());
            taskDTO.setTitle(task.getName());
            taskDTO.setDescription(task.getDescription());
            taskDTO.setDate(dateFormat.format(task.getDate()));
            taskDTO.setTime(dateFormat.format(task.getDate()));
            taskDTO.setStatus(task.getStatus().getNameRu());
            response.add(taskDTO);
        }
        return response;
    }

    public @ResponseBody ResponseEntity<InputStreamResource> getPhotoById(Integer id) {
        InputStream targetStream = new ByteArrayInputStream(photoRepository.getById(id).getData());
        return ResponseEntity.ok().contentType(MediaType.IMAGE_PNG).body(new InputStreamResource(targetStream));
    }

    public @ResponseBody ResponseEntity<InputStreamResource> getDocumentByTaskId(Integer id) {
        InputStream targetStream = new ByteArrayInputStream(taskRepository.getById(id).getDocument());
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Description", "File Transfer");
        headers.add("Content-Disposition", "attachment; filename=Request.xlsx");
        headers.add("Content-Transfer-Encoding", "binary");
        headers.add("Connection", "Keep-Alive");
        headers.setContentType(
                MediaType.parseMediaType("application/excel"));
        return ResponseEntity.ok().headers(headers).body(new InputStreamResource(targetStream));
    }

    public String updateStatus(Integer taskId, String status, String dateStr) {
        Task task = taskRepository.getById(taskId);
        Timestamp date = Timestamp.valueOf(dateStr + " 00:00:00");
        task.setTaskCompleteDate(date);
        Status statusModel = statusRepository.getByName(status);
        task.setStatus(statusModel);
        taskRepository.save(task);
        return "ok";
        //throw new CustomException("", HttpStatus.OK);
    }

    public String updateStatus(Integer taskId, String status) {
        Task task = taskRepository.getById(taskId);
        Status statusModel = statusRepository.getByName(status);
        task.setStatus(statusModel);
        taskRepository.save(task);
        return "ok";
        //throw new CustomException("", HttpStatus.OK);
    }

    public String loadEquipmentsFromExcel(LoadEquipmentsDTO loadEquipmentsDTO, HttpServletRequest req) throws IOException {
        FileInputStream fis = (FileInputStream) loadEquipmentsDTO.getFile().getInputStream();
        HSSFWorkbook wb = new HSSFWorkbook(fis);
        HSSFSheet sheet = wb.getSheetAt(0);
        equipmentRepository.deleteAll(); // Удаляем все оборудования
        for (Row row : sheet) {
            int filialCode = (int) row.getCell(0).getNumericCellValue();
            String filialName = row.getCell(1).getStringCellValue().trim();
            String objectName = row.getCell(2).getStringCellValue().trim();
            String equipmentCode = row.getCell(3).getStringCellValue().trim();
            String equipmentName = row.getCell(4).getStringCellValue().trim();
            String equipmentDescription = row.getCell(5).getStringCellValue().trim();

            // Создание/Получение филиала
            Filial filial = null;
            if (!filialRepository.existsByCode(filialCode)) { // Есть ли филиал по коду
                filial = new Filial();
                filial.setCode(filialCode);
                filial.setName(filialName);
                filialRepository.save(filial);
            } else {
                filial = filialRepository.getByCode(filialCode);
            }
            // -----------------------

            // Создание/Получение объекта питания
            Obj obj = null;
            if (!objectRepository.existsByNameAndFilial(objectName, filial)) { // Есть ли объект по наименованию
                obj = new Obj();
                obj.setName(objectName);
                obj.setFilial(filial);
                objectRepository.save(obj);
            } else {
                obj = objectRepository.getByNameAndFilial(objectName, filial);
            }
            // -----------------------

            // Создание оборудования
            Equipment equipment = new Equipment();
            equipment.setObj(obj);
            equipment.setCode(equipmentCode);
            equipment.setName(equipmentName);
            equipment.setDescription(equipmentDescription);

            equipmentRepository.save(equipment);
            // -----------------------

            // Добавления оборудования в объект
            List<Equipment> newEquipments;
            if (obj.getEquipments() == null) {
                newEquipments = new ArrayList<>();
            } else {
                newEquipments = obj.getEquipments();
            }
            newEquipments.add(equipment);
            obj.setEquipments(newEquipments);
            objectRepository.save(obj);
            // -----------------------

        }
        return "ok";
    }

    public @ResponseBody ResponseEntity<InputStreamResource> getAllToExcel(HttpServletRequest req) throws IOException {
        Workbook report = new HSSFWorkbook();
        Sheet tasksSheet = report.createSheet("Заявки");
        Integer rowCounter = 0;

        Row row = tasksSheet.createRow(rowCounter);

        Cell taskId = row.createCell(0);
        taskId.setCellValue("Номер");

        Cell createDate = row.createCell(1);
        createDate.setCellValue("Дата");

        Cell taskName = row.createCell(2);
        taskName.setCellValue("Поломка");

        Cell taskDescription = row.createCell(3);
        taskDescription.setCellValue("Описание поломки");

        Cell taskCreator = row.createCell(4);
        taskCreator.setCellValue("ФИО заявителя");

        Cell taskEquipmentCode = row.createCell(5);
        taskEquipmentCode.setCellValue("Код оборудование");

        Cell taskEquipmentName = row.createCell(6);
        taskEquipmentName.setCellValue("Наименование оборудования");

        Cell taskFilial = row.createCell(7);
        taskFilial.setCellValue("Филиал");

        Cell taskObject = row.createCell(8);
        taskObject.setCellValue("Объект");
        rowCounter += 1;
        for (Task task : taskRepository.findAll()) {
            row = tasksSheet.createRow(rowCounter);

            taskId = row.createCell(0);
            taskId.setCellValue(task.getId());

            createDate = row.createCell(1);
            DataFormat format = report.createDataFormat();
            CellStyle dateStyle = report.createCellStyle();
            dateStyle.setDataFormat(format.getFormat("dd.mm.yyyy"));
            createDate.setCellStyle(dateStyle);
            createDate.setCellValue(task.getDate());

            taskName = row.createCell(2);
            taskName.setCellValue(task.getName());

            taskDescription = row.createCell(3);
            taskDescription.setCellValue(task.getDescription());

            taskCreator = row.createCell(4);
            taskCreator.setCellValue(task.getCreator().getSurname() + " " + task.getCreator().getName() + " " + task.getCreator().getSecondName());

            taskEquipmentCode = row.createCell(5);
            taskEquipmentCode.setCellValue(task.getEquipment().getCode());

            taskEquipmentName = row.createCell(6);
            taskEquipmentName.setCellValue(task.getEquipment().getName());

            taskFilial = row.createCell(7);
            taskFilial.setCellValue(task.getEquipment().getObj().getFilial().getName());

            taskObject = row.createCell(8);
            taskObject.setCellValue(task.getEquipment().getObj().getName());

            // Меняем размер столбца
            tasksSheet.autoSizeColumn(1);
            rowCounter += 1;
        }

        // Записываем всё в файл
        report.write(new FileOutputStream("Заявки.xls"));
        report.close();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Description", "File Transfer");
        headers.add("Content-Disposition", "attachment; filename=Request.xlsx");
        headers.add("Content-Transfer-Encoding", "binary");
        headers.add("Connection", "Keep-Alive");
        headers.setContentType(
                MediaType.parseMediaType("application/excel"));
        return ResponseEntity.ok().headers(headers).body(new InputStreamResource(new FileInputStream("Заявки.xls")));
    }

    public List<ReportDataDTO> getReport() {
        List<ReportDataDTO> data = new ArrayList<>();
        for (Task task : taskRepository.findAll()) {
            ReportDataDTO record = new ReportDataDTO();
            record.setFilial(task.getEquipment().getObj().getFilial().getName());
            record.setValue(1);
            record.setStatus(task.getStatus().getNameRu());
            data.add(record);
        }
        return data;
    }

    public HashMap<String, Object> getReportObjects(String filial) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        HashMap<String, Object> response = new HashMap<>();
        List<ReportDataDTO> data = new ArrayList<>();
        List<TaskGridDataDTO> tasks = new ArrayList<>();
        for (Task task : taskRepository.findAll()) {
            ReportDataDTO record = new ReportDataDTO();
            String filialName = task.getEquipment().getObj().getFilial().getName();
            if (filialName.equals(filial)) {
                record.setFilial(task.getEquipment().getObj().getName());
                record.setValue(1);
                record.setStatus(task.getStatus().getNameRu());
                data.add(record);

                TaskGridDataDTO taskDTO = new TaskGridDataDTO();
                taskDTO.setId(task.getId());
                taskDTO.setObject(task.getEquipment().getObj().getName());
                taskDTO.setFilialAndObject(task.getEquipment().getObj().getFilial().getName() + ' ' + task.getEquipment().getObj().getName());
                taskDTO.setEquipment(task.getEquipment().getName() + '#' + task.getEquipment().getCode());
                taskDTO.setCreator(task.getCreator().getSurname() + " " + task.getCreator().getName() + " " + task.getCreator().getSecondName());
                taskDTO.setTitle(task.getName());
                taskDTO.setDescription(task.getDescription());
                taskDTO.setDate(dateFormat.format(task.getDate()));
                taskDTO.setTime(dateFormat.format(task.getDate()));
                taskDTO.setStatus(task.getStatus().getNameRu());
                tasks.add(taskDTO);
            }
        }

        response.put("data", data);
        response.put("tasks", tasks);
        return response;
    }
}
