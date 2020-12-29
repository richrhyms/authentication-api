package com.richotaru.authenticationapi.service;

import com.richotaru.authenticationapi.domain.model.dto.ClientSystemAuthDto;
import com.richotaru.authenticationapi.domain.model.dto.ClientSystemDto;
import com.richotaru.authenticationapi.domain.model.pojo.ClientSystemAuthPojo;
import com.richotaru.authenticationapi.domain.model.pojo.ClientSystemPojo;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface ClientSystemService {
    ClientSystemPojo createClientSystem(ClientSystemDto dto);
    ClientSystemAuthPojo authenticateClient(ClientSystemAuthDto dto) throws UsernameNotFoundException;
    ClientSystemPojo getAuthenticatedClient(String username) throws UsernameNotFoundException;
}
