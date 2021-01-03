package com.richotaru.authenticationapi.domain.model.pojo;


import com.richotaru.authenticationapi.domain.entity.ClientSystem;
import com.richotaru.authenticationapi.domain.entity.ClientUser;
import com.richotaru.authenticationapi.domain.entity.PortalAccount;
import com.richotaru.authenticationapi.domain.enums.AccountTypeConstant;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

@Data
@NoArgsConstructor
public class PortalAccountPojo {
    private String accountCode;
    private String displayName;
    private String username;
    private String password;
    private String jwtToken;
    private String roles;
    private AccountTypeConstant accountType;
    private ClientSystem client;
    private ClientUser user;


    public PortalAccountPojo(PortalAccount portalAccount) {
        BeanUtils.copyProperties(portalAccount, this);

    }

}
