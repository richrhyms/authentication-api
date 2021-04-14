package com.richotaru.authenticationapi.domain.model.dto;

import com.richotaru.authenticationapi.enumeration.AccessModeConstant;
import com.richotaru.authenticationapi.enumeration.ClientSystemTypeConstant;
import com.richotaru.authenticationapi.enumeration.GenericStatusConstant;
import com.richotaru.authenticationapi.enumeration.WorkSpaceAccountTypeConstant;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
public class ClientSystemUpdateDto {
    @NotBlank(message = "Client Key cannot be empty")
    private String clientKey;
    private String displayName;
    private ClientSystemTypeConstant systemType;
    private WorkSpaceAccountTypeConstant workSpaceAccountType;
    private AccessModeConstant accessMode;
    private GenericStatusConstant status;
}
