package com.richotaru.authenticationapi.service;

import com.richotaru.authenticationapi.domain.model.dto.ClientSystemDto;
import com.richotaru.authenticationapi.domain.model.dto.ClientSystemUpdateDto;
import com.richotaru.authenticationapi.domain.model.pojo.ClientSystemPojo;

public interface ClientSystemService {
    ClientSystemPojo createClientSystem(ClientSystemDto dto);
    ClientSystemPojo updateClientSystem(Long clientSystemId, ClientSystemUpdateDto dto);
    boolean isValidKey(String clientKey);
}
