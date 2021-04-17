package com.richotaru.authenticationapi.domain.model.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
public class AccountAuthDto {
    @NotBlank(message = "Client Key cannot be empty")
    private String clientKey;
    @NotBlank(message = "Workspace Code cannot be empty")
    private String workspaceCode;
    @NotBlank(message = "Username cannot be empty")
    private String username;
    @NotBlank(message = "Password Code cannot be empty")
    private String password;

}
