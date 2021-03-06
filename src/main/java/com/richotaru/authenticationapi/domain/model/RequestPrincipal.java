package com.richotaru.authenticationapi.domain.model;

import com.richotaru.authenticationapi.domain.entity.ClientSystem;
import com.richotaru.authenticationapi.domain.entity.ClientUser;
import com.richotaru.authenticationapi.domain.entity.PortalAccount;
import com.richotaru.authenticationapi.domain.enums.AccountTypeConstant;
import com.richotaru.authenticationapi.domain.model.pojo.ClientSystemPojo;
import com.richotaru.authenticationapi.domain.model.pojo.ClientUserPojo;
import com.richotaru.authenticationapi.domain.model.pojo.PortalAccountPojo;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

@Data
@NoArgsConstructor
public class RequestPrincipal {
    public static final String AUTH_TOKEN_NAME = "jwt_token";
    private String accountCode;
    private String displayName;
    private String username;
    private String password;
    private String jwtToken;
    private String roles;
    private AccountTypeConstant accountType;
    private PortalAccountPojo portalAccount;
    private String ipAddress;

    public RequestPrincipal(PortalAccountPojo accountPojo) {
        BeanUtils.copyProperties(accountPojo, this);
        this.portalAccount = accountPojo;
    }
}
