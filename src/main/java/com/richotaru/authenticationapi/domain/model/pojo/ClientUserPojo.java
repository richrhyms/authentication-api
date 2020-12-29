package com.richotaru.authenticationapi.domain.model.pojo;

import com.richotaru.authenticationapi.domain.entity.ClientSystem;
import com.richotaru.authenticationapi.domain.entity.PortalUser;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

import javax.persistence.Column;
import javax.persistence.OneToMany;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
public class ClientSystemPojo {
    private String clientName;
    private String clientKey;
    private String displayName;
    private Timestamp dateRegistered;
    private List<PortalUser> users = new ArrayList<>();
    private String jwtToken;
    private Timestamp expirationDate;

    public ClientSystemPojo(ClientSystem clientSystem) {
        BeanUtils.copyProperties(clientSystem, this);
    }
}
