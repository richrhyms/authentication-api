package com.richotaru.authenticationapi.domain.model.pojo;

import com.richotaru.authenticationapi.domain.entity.ClientSystem;
import com.richotaru.authenticationapi.domain.entity.ClientUser;
import com.richotaru.authenticationapi.domain.entity.PortalAccount;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class ClientSystemPojo {
    private String clientName;
    private String clientKey;
    private String displayName;
    private Timestamp dateRegistered;
    private List<ClientUser> users = new ArrayList<>();
    private String jwtToken;
    private Timestamp expirationDate;
    private PortalAccount portalAccount;

    public ClientSystemPojo(ClientSystem clientSystem) {
        BeanUtils.copyProperties(clientSystem, this);
    }
}
