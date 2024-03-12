package de401t.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class ReportDataDTO {

    @ApiModelProperty(position = 0)
    private String filial;

    @ApiModelProperty(position = 1)
    private String status;

    @ApiModelProperty(position = 2)
    private Integer value;

}
