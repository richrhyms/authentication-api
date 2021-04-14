package com.richotaru.authenticationapi.domain.model.dto;

import com.richotaru.authenticationapi.enumeration.WorkSpaceTypeConstant;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
public class WorkSpaceCreationDto {
    @NotBlank
    private String clientKey;
    @NotBlank
    private String systemCode;
    @NotBlank
    private WorkSpaceTypeConstant accountType;
    @NotBlank
    private String displayName;
}
