package com.richotaru.authenticationapi.domain.model;

import com.richotaru.authenticationapi.domain.entity.ClientSystem;
import com.richotaru.authenticationapi.domain.entity.PortalAccount;
import com.richotaru.authenticationapi.domain.model.pojo.ClientSystemPojo;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RequestPrincipal {
    public static final String AUTH_TOKEN_NAME = "jwt_token";
    private ClientSystemPojo clientPojo;
    private ClientSystem client;
    private String jwtToken;
    private String idAddress;
    private PortalAccount portalAccount;

    public RequestPrincipal(ClientSystem clientSystem) {
        this.clientPojo = new ClientSystemPojo(clientSystem);
        this.client = clientSystem;
        if(clientSystem.getPortalAccount().getJwtToken() != null){
            this.portalAccount = clientSystem.getPortalAccount();
            this.jwtToken = clientSystem.getPortalAccount().getJwtToken();
        }
    }
}
