package com.richotaru.authenticationapi.domain.model.pojo;

import com.richotaru.authenticationapi.domain.enums.AccountTypeConstant;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
public class PortalAccountAuthPojo {
    private String jwtToken;
    private Date expirationDate;
    private AccountTypeConstant accountTypeConstant;
    private List<SimpleGrantedAuthority> roles;
}
