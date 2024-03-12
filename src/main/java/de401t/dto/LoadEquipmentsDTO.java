package de401t.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class LoadEquipmentsDTO {
    @ApiModelProperty(position = 0)
    private MultipartFile file;
}
