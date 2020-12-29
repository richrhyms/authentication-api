package com.richotaru.authenticationapi.domain.model.dto;

import com.richotaru.authenticationapi.domain.enums.RoleConstant;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Data
@NoArgsConstructor
public class ClientUserDto {
    @NotBlank
    private String firstName;
    @NotBlank
    private String lastName;
    @NotBlank
    private String displayName;
    @NotBlank
    private String password;
    @NotBlank
    private String phoneNumber;
    @NotBlank
    private String email;
    @NotNull
    private Set<RoleConstant> roles;
}
