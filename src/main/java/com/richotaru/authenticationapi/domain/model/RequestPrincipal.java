package com.richotaru.authenticationapi.domain.model;

import com.richotaru.authenticationapi.domain.entity.ClientSystem;
import com.richotaru.authenticationapi.domain.model.pojo.ClientSystemPojo;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RequestPrincipal {
    private ClientSystemPojo clientPojo;
    private ClientSystem client;
    private String jwtToken;

    public RequestPrincipal(ClientSystem clientSystem) {
        this.clientPojo = new ClientSystemPojo(clientSystem);
        this.client = clientSystem;
        if(clientSystem.getPortalAccount().getJwtToken() != null){
            this.jwtToken = clientSystem.getPortalAccount().getJwtToken();
        }
    }
    public RequestPrincipal(ClientSystemPojo clientSystemPojo) {
        this.clientPojo = clientSystemPojo;
        if(clientSystemPojo.getPortalAccount().getJwtToken() != null){
            this.jwtToken = clientSystemPojo.getPortalAccount().getJwtToken();
        }
    }
}
