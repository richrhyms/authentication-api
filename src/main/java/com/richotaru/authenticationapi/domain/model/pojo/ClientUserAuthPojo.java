package com.richotaru.authenticationapi.domain.model.pojo;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
public class ClientUserAuthPojo {
    private String jwtToken;
    private Date expirationDate;
}
