package de401t.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MessageDataDTO {

    @ApiModelProperty(position = 0)
    private Integer id;
    @ApiModelProperty(position = 1)
    private String message;
    @ApiModelProperty(position = 2)
    private String date;
    @ApiModelProperty(position = 3)
    private Integer taskId;
    @ApiModelProperty(position = 4)
    private String userFrom;
    @ApiModelProperty(position = 5)
    private String userRole;
    @ApiModelProperty(position = 6)
    private String userTo;
    @ApiModelProperty(position = 7)
    private Boolean fromMe;
}
