package com.richotaru.authenticationapi.domain.model.dto;

import com.richotaru.authenticationapi.domain.enums.AccountTypeConstant;
import com.richotaru.authenticationapi.domain.enums.RoleConstant;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.util.Set;

@Data
@NoArgsConstructor
public class AccountCreationDto {
    @NotBlank
    private AccountTypeConstant accountType;
    @NotBlank
    private String displayName;
    @NotBlank
    private String username;
    @NotBlank
    private String password;
    @NotBlank
    private Set<RoleConstant> roles;
}
