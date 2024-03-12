package de401t.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class TaskGridDataDTO {
    @ApiModelProperty(position = 0)
    private Integer id;
    @ApiModelProperty(position = 1)
    private String object;
    @ApiModelProperty(position = 2)
    private String equipment;
    @ApiModelProperty(position = 3)
    private String creator;
    @ApiModelProperty(position = 4)
    private String title;
    @ApiModelProperty(position = 5)
    private String description;
    @ApiModelProperty(position = 6)
    private String date;
    @ApiModelProperty(position = 7)
    private String time;
    @ApiModelProperty(position = 8)
    private String status;
    @ApiModelProperty(position = 9)
    private String filialAndObject;
}
