package com.richotaru.authenticationapi.domain.model.pojo;

import com.richotaru.authenticationapi.entity.ClientSystem;
import com.richotaru.authenticationapi.enumeration.ClientSystemTypeConstant;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

import java.sql.Timestamp;


@Data
@NoArgsConstructor
public class ClientSystemPojo {
    private ClientSystemTypeConstant type;
    private String code;
    private String displayName;
    private Timestamp lastUpdatedAt;

    public ClientSystemPojo(ClientSystem clientSystem) {
        BeanUtils.copyProperties(clientSystem, this);
    }
}
