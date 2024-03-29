package com.richotaru.authenticationapi.domain.model.dto;

import com.richotaru.authenticationapi.enumeration.AccessModeConstant;
import com.richotaru.authenticationapi.enumeration.ClientSystemTypeConstant;
import com.richotaru.authenticationapi.enumeration.WorkSpaceAccountTypeConstant;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
public class ClientSystemDto {
    @NotBlank(message = "Client Key cannot be empty")
    private String clientKey;
    @NotBlank(message = "Client Display Name cannot be empty")
    private String displayName;
    @NotNull(message = "System Type cannot be null")
    private ClientSystemTypeConstant systemType;
    @NotNull(message = "Workspace Mode cannot be null")
    private WorkSpaceAccountTypeConstant workSpaceAccountType;
    @NotNull(message = "Client Type cannot be null")
    private AccessModeConstant accessMode;
}
