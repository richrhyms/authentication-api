package com.richotaru.authenticationapi.domain.model;

import com.richotaru.authenticationapi.domain.enums.AccountTypeConstant;
import com.richotaru.authenticationapi.domain.model.pojo.WorkSpaceUserPojo;
import com.richotaru.authenticationapi.entity.WorkSpace;
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
    private String jwtToken;
    private AccountTypeConstant accountType;
    private WorkSpaceUserPojo workSpaceUser;
    private WorkSpace workSpace;
    private String ipAddress;

    public RequestPrincipal(WorkSpaceUserPojo userPojo) {
        BeanUtils.copyProperties(userPojo, this);
        this.workSpaceUser = userPojo;
    }
}
