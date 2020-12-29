package com.richotaru.authenticationapi.domain.model.pojo;

import com.richotaru.authenticationapi.domain.entity.ClientSystem;
import com.richotaru.authenticationapi.domain.entity.ClientUser;
import com.richotaru.authenticationapi.domain.entity.PortalAccount;
import com.richotaru.authenticationapi.domain.enums.RoleConstant;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Set;


@Data
@NoArgsConstructor
public class ClientUserPojo {
    private String firstName;
    private String lastName;
    private String displayName;
    private String username;
    private String password;
    private String dateRegistered;
    private String phoneNumber;
    private String email;
    private Set<RoleConstant> roles;
    private ClientSystem clientSystem;
    private PortalAccount portalAccount;


    public ClientUserPojo(ClientUser clientUser) {
        BeanUtils.copyProperties(clientUser, this);

    }

}
