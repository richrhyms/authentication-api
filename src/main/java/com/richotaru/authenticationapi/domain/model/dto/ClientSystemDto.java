package com.richotaru.authenticationapi.domain.model.dto;

import com.richotaru.authenticationapi.domain.enums.RoleConstant;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Data
@NoArgsConstructor
public class ClientSystemDto {
    @NotBlank
    private String clientName;
    @NotBlank
    private String clientKey;
    @NotBlank
    private String displayName;
    @NotNull
    private Set<RoleConstant> roles;
}
