package com.richotaru.authenticationapi.domain.model.dto;

import com.richotaru.authenticationapi.enumeration.WorkSpaceTypeConstant;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
public class WorkSpaceCreationDto {
    @NotBlank(message = "Client Key cannot be empty")
    private String clientKey;
    @NotBlank(message = "System Code cannot be empty")
    private String systemCode;
    @NotNull(message = "Account Type cannot be empty")
    private WorkSpaceTypeConstant accountType;
    @NotBlank(message = "Display Name cannot be empty")
    private String displayName;
}
