package de401t.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class EquipmentUniquesDataDTO {

    @ApiModelProperty(position = 0)
    private List<String> names;
    @ApiModelProperty(position = 1)
    private List<String> codes;
    @ApiModelProperty(position = 2)
    private List<String> objects;
}
