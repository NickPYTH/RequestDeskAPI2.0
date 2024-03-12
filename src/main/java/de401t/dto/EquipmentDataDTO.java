package de401t.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class EquipmentDataDTO {

    @ApiModelProperty(position = 0)
    private Integer id;
    @ApiModelProperty(position = 1)
    private String name;
    @ApiModelProperty(position = 2)
    private String code;
    @ApiModelProperty(position = 3)
    private Integer obj;
    @ApiModelProperty(position = 4)
    private String objName;
    @ApiModelProperty(position = 5)
    private String filial;
    @ApiModelProperty(position = 6)
    private String filialAndObj;
}
