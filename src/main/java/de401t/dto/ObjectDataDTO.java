package de401t.dto;

import de401t.model.Equipment;
import de401t.model.Filial;
import de401t.model.Obj;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
public class ObjectDataDTO {

    @ApiModelProperty(position = 0)
    private String name;
    @ApiModelProperty(position = 1)
    private Integer id;
    @ApiModelProperty(position = 2)
    private List<EquipmentDataDTO> equipments;
    @ApiModelProperty(position = 3)
    private FilialDataDTO filial;

}
