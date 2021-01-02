package com.richotaru.authenticationapi.domain.model.pojo;

import com.richotaru.authenticationapi.domain.enums.AccountTypeConstant;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
public class PortalAccountAuthPojo {
    private String jwtToken;
    private Date expirationDate;
    private AccountTypeConstant accountTypeConstant;
}
