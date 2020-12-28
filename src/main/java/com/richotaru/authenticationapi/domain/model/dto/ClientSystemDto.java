package com.richotaru.authenticationapi.domain.model.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.sql.Timestamp;

@Data
@NoArgsConstructor
public class ClientSystemDto {
    @NotBlank
    private String clientName;
    @NotBlank
    private String clientKey;
    @NotBlank
    private String displayName;
}
