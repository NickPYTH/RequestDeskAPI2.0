package de401t.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class TaskDataDTO {
    @ApiModelProperty(position = 0)
    private MultipartFile photo0;
    @ApiModelProperty(position = 1)
    private MultipartFile photo1;
    @ApiModelProperty(position = 2)
    private MultipartFile photo2;
    @ApiModelProperty(position = 3)
    private String equipment;
    @ApiModelProperty(position = 4)
    private String title;
    @ApiModelProperty(position = 5)
    private String description;
    @ApiModelProperty(position = 6)
    private String date;
    @ApiModelProperty(position = 7)
    private String status;
    @ApiModelProperty(position = 8)
    private List<String> photos;
    @ApiModelProperty(position = 9)
    private Integer id;
    @ApiModelProperty(position = 10)
    private String oldPhotosIds;
    @ApiModelProperty(position = 11)
    private String object;
    @ApiModelProperty(position = 12)
    private String objectId;
    @ApiModelProperty(position = 13)
    private Integer creator;
    @ApiModelProperty(position = 14)
    private String completedDate;
}
