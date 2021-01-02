package com.richotaru.authenticationapi.service;

import com.richotaru.authenticationapi.domain.entity.ClientSystem;
import com.richotaru.authenticationapi.domain.model.dto.ClientSystemDto;
import com.richotaru.authenticationapi.domain.model.pojo.ClientSystemPojo;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface ClientSystemService {
    ClientSystemPojo createClientSystem(ClientSystemDto dto);
    ClientSystem getClient(String username) throws UsernameNotFoundException;
}
