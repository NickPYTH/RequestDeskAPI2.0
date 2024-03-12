package de401t.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class FilialResponseDTO {
    @ApiModelProperty(position = 0)
    private Integer id;
    @ApiModelProperty(position = 1)
    private String name;
    @ApiModelProperty(position = 2)
    private Integer code;
}
