package de401t.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.List;

@Data
@NoArgsConstructor
public class UserDataDTO {

    @ApiModelProperty(position = 0)
    private String username;

    @ApiModelProperty(position = 1)
    private String password;

    @ApiModelProperty(position = 2)
    private String name;

    @ApiModelProperty(position = 3)
    private String surname;

    @ApiModelProperty(position = 4)
    private String secondName;

    @ApiModelProperty(position = 5)
    private List<HashMap<String, String>> objects;

    @ApiModelProperty(position = 5)
    private String objectsString;

    @ApiModelProperty(position = 5)
    private List<HashMap<String, String>> filials;

    @ApiModelProperty(position = 5)
    private List<Integer> objectsCode;

    @ApiModelProperty(position = 6)
    private String email;

    @ApiModelProperty(position = 7)
    private String phone;

    @ApiModelProperty(position = 8)
    private Integer role;

    @ApiModelProperty(position = 8)
    private String roleString;

    @ApiModelProperty(position = 9)
    private Integer id;

}
