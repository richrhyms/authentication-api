package com.richotaru.authenticationapi.domain.model.dto;

import com.richotaru.authenticationapi.enumeration.GenericStatusConstant;
import com.richotaru.authenticationapi.enumeration.WorkSpaceTypeConstant;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
public class WorkSpaceUpdateDto {
    @NotBlank
    private String clientKey;
    private WorkSpaceTypeConstant accountType;
    private String displayName;
    private GenericStatusConstant status;
}
