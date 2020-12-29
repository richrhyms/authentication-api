package com.richotaru.authenticationapi.domain.model;

import com.richotaru.authenticationapi.domain.entity.ClientSystem;
import com.richotaru.authenticationapi.domain.entity.PortalUser;
import com.richotaru.authenticationapi.domain.model.pojo.ClientSystemPojo;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class RequestPrincipal {
    private ClientSystemPojo client;
    private String jwtToken;

    public RequestPrincipal(ClientSystem clientSystem) {
        this.client = new ClientSystemPojo(clientSystem);
        this.jwtToken = clientSystem.getJwtToken();
    }
}
