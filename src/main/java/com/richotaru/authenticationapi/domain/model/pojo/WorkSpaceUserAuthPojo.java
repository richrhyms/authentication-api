package com.richotaru.authenticationapi.domain.model.pojo;

import com.richotaru.authenticationapi.enumeration.AccessModeConstant;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
public class WorkSpaceUserAuthPojo {
    private String workspaceCode;
    private String username;
    private String jwtToken;
    private Date expirationDate;
    private AccessModeConstant accessMode;
    private boolean isValid;
}
