package com.richotaru.authenticationapi.domain.model.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ClientSystemAuthDto {
    private String username;
    private String password;
}
