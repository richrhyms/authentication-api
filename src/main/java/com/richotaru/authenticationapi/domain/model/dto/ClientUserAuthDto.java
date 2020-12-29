package com.richotaru.authenticationapi.domain.model.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ClientUserAuthDto {
    private String username;
    private String password;
}
