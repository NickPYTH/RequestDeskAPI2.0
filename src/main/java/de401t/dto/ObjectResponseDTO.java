package de401t.dto;

import de401t.model.Equipment;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class ObjectResponseDTO {
    @ApiModelProperty(position = 0)
    private Integer id;
    @ApiModelProperty(position = 1)
    private String name;
    @ApiModelProperty(position = 2)
    private FilialDataDTO filial;
    @ApiModelProperty(position = 2)
    private String filialString;
    @ApiModelProperty(position = 3)
    private List<EquipmentDataDTO> equipments;
}
