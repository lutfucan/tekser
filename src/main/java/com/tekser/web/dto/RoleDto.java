package com.tekser.web.dto;

import com.tekser.customAnnotations.ValidRoleName;
import lombok.Data;

@Data
public class RoleDto {
    Long id;
    @ValidRoleName
    String name;
}
